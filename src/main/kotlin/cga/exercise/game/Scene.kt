package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.mooncruiser.GameObjects.Car
import cga.exercise.components.mooncruiser.GameObjects.Ground
import cga.exercise.components.mooncruiser.ObjectManager
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

    private val objectManager : ObjectManager




    private val staticShader: ShaderProgram


    //Lights
    private val bikePointLight: PointLight
    private val bikeSpotLight: SpotLight


    private val bikePointLight2: PointLight
    private val bikePointLight3: PointLight
    //camera
    private val camera: TronCamera
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true


    //scene setup
    init {

        objectManager = ObjectManager()

        var ground = Ground()
        ground.init()
        objectManager.addObject(ground)

        var car = Car()
        car.init()
        objectManager.addObject(car)



        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")


        //setup camera
        camera = TronCamera(
                custom(window.framebufferWidth, window.framebufferHeight),
                Math.toRadians(90.0f),
                0.1f,
                100.0f
        )
        camera.parent = car


        //move camera a little bit in z direction
        camera.rotateLocal(Math.toRadians(-35.0f), 0.0f, 0.0f)
        camera.translateLocal(Vector3f(0.0f, 0.0f, 4.0f))


        //bike point light
        bikePointLight = PointLight(Vector3f(0.0f, 2.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        bikePointLight.parent = car
        //bike spot light
        bikeSpotLight = SpotLight(Vector3f(1.0f, 1.0f, 1.0f), Vector3f(0.0f, 1.0f, -2.0f), Math.toRadians(20.0f), Math.toRadians(30.0f))
        bikeSpotLight.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        bikeSpotLight.parent = car

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

        objectManager.render()

    }

    fun update(dt: Float, t: Float) { //camera update
        objectManager.update(dt,window)
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawangle = (xpos - oldMouseX).toFloat() * 0.002f
            val pitchangle = (ypos - oldMouseY).toFloat() * 0.0005f
            if (!window.getKeyState(GLFW_KEY_LEFT_ALT)) {
                //car.rotateLocal(0.0f, yawangle, 0.0f)
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