package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader.loadModel
import cga.framework.OBJLoader.loadOBJ
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    private val bike: Renderable
    private val ground: Renderable
    //Lights
    private val bikePointLight: PointLight
    private val bikeSpotLight: SpotLight
    private val groundMaterial: Material
    private val groundColor: Vector3f

    private val bikePointLight2: PointLight
    private val bikePointLight3: PointLight
    //camera
    private val camera: TronCamera
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true

    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
        //load textures
        val groundDiff = Texture2D("assets/textures/ground_diff.png", true)
        groundDiff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundSpecular = Texture2D("assets/textures/ground_spec.png", true)
        groundSpecular.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundEmit = Texture2D("assets/textures/ground_emit.png", true)
        groundEmit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        groundMaterial = Material(groundDiff, groundEmit, groundSpecular, 60f, Vector2f(64.0f, 64.0f))



        //load an object and create a mesh
        val gres = loadOBJ("assets/models/ground.obj")
        //OBJLoader.OBJResult sres = OBJLoader.loadOBJ("assets/models/sphere.obj", false, false);
        //Create the mesh
        val stride = 8 * 4
        val atr1 = VertexAttribute(3, GL_FLOAT, stride, 0) //position attribute
        val atr2 = VertexAttribute(2, GL_FLOAT, stride, 3 * 4) //texture coordinate attribut
        val atr3 = VertexAttribute(3, GL_FLOAT, stride, 5 * 4) //normal attribute
        val vertexAttributes = arrayOf(atr1, atr2, atr3)
        //Create renderable
        ground = Renderable()
        for (m in gres.objects[0].meshes) {
            val mesh = Mesh(m.vertexData, m.indexData, vertexAttributes, groundMaterial)
            ground.meshes.add(mesh)
        }
        bike = loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(-90.0f), Math.toRadians(90.0f), 0.0f) ?: throw IllegalArgumentException("Could not load the model")
        bike.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))
        //for (OBJLoader.OBJMesh m : sres.objects.get(0).meshes) {
        //    Mesh mesh = new Mesh(m.getVertexData(), m.getIndexData(), vertexAttributes, bikeMaterial);
        //    bike.meshes.add(mesh);
        //}
        //setup camera
        camera = TronCamera(
                custom(window.framebufferWidth, window.framebufferHeight),
                Math.toRadians(90.0f),
                0.1f,
                100.0f
        )
        camera.parent = bike
        //move camera a little bit in z direction
        camera.rotateLocal(Math.toRadians(-35.0f), 0.0f, 0.0f)
        camera.translateLocal(Vector3f(0.0f, 0.0f, 4.0f))
        //bike point light
        bikePointLight = PointLight(Vector3f(0.0f, 2.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        bikePointLight.parent = bike
        //bike spot light
        bikeSpotLight = SpotLight(Vector3f(1.0f, 1.0f, 1.0f), Vector3f(0.0f, 1.0f, -2.0f), Math.toRadians(20.0f), Math.toRadians(30.0f))
        bikeSpotLight.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        bikeSpotLight.parent = bike
        groundColor = Vector3f(0.0f, 1.0f, 0.0f)

        bikePointLight2 = PointLight(Vector3f(0.0f, 2.0f, 2.0f), Vector3f(-10.0f, 2.0f, -10.0f))
        bikePointLight3 = PointLight(Vector3f(2.0f, 0.0f, 0.0f), Vector3f(10.0f, 2.0f, 10.0f))

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        //glFrontFace(GL_CCW); GLError.checkThrow()
        //glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()
        camera.bind(staticShader)
        val changingColor = Vector3f(Math.abs(Math.sin(t)), 0f, Math.abs(Math.cos(t)))
        bikePointLight.lightColor = changingColor
        staticShader.setUniform("shadingColor", changingColor)
        bikePointLight.bind(staticShader, "bikePointLight")
        bikePointLight2.bind(staticShader, "bikePointLight2")
        bikePointLight3.bind(staticShader, "bikePointLight3")
        bikeSpotLight.bind(staticShader, "bikeSpotLight", camera.calculateViewMatrix())
        bike.render(staticShader)
        staticShader.setUniform("shadingColor", groundColor)
        ground.render(staticShader)
    }

    fun update(dt: Float, t: Float) { //camera update
        val movemul = 5.0f
        val rotatemul = 2.0f
        if (window.getKeyState(GLFW_KEY_W)) {
            bike.translateLocal(Vector3f(0.0f, 0.0f, -dt * movemul))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            bike.translateLocal(Vector3f(0.0f, 0.0f, dt * movemul))
        }
        if (window.getKeyState(GLFW_KEY_A) and window.getKeyState(GLFW_KEY_W)) {
            bike.rotateLocal(0.0f, dt * rotatemul, 0.0f)
        }
        if (window.getKeyState(GLFW_KEY_D) and window.getKeyState(GLFW_KEY_W)) {
            bike.rotateLocal( 0.0f, -dt * rotatemul,0.0f)
        }
        if (window.getKeyState(GLFW_KEY_F)) {
            bikeSpotLight.rotateLocal(Math.PI.toFloat() * dt, 0.0f, 0.0f)
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawangle = (xpos - oldMouseX).toFloat() * 0.002f
            val pitchangle = (ypos - oldMouseY).toFloat() * 0.0005f
            if (!window.getKeyState(GLFW_KEY_LEFT_ALT)) {
                bike.rotateLocal(0.0f, yawangle, 0.0f)
            }
            else{
                camera.rotateAroundPoint(0.0f, yawangle, 0.0f, Vector3f(0.0f, 0.0f, 0.0f))
            }
        } else firstMouseMove = false
        oldMouseX = xpos
        oldMouseY = ypos
    }

    fun cleanup() {}
}