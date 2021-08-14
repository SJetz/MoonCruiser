package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.mooncruiser.gameObjects.*
import cga.exercise.components.mooncruiser.ObjectManager
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {

    var car = Car()

    //obecjtmanger
    private val objectManager : ObjectManager

    //shader
    private val staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    private val toonShader = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_pixel.glsl")
    private val simpleShader = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")


    //camera
    val camera : TronCamera

    //mouse
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true

    //scene setup
    init {
        //setup camera
        camera = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            100.0f
        )
/*
        //create light
       var direction = Vector3f(0.2f,-0.5f,-1.0f)
        var intensity = 1
        var diffuse = Vector4f(1f,1f,1f,1f)
        var color = Vector4f(1f,1f,1f,1f)
        var ambientcolor = Vector4f(0.4f,0.4f,0.4f,1f)
        var specularcolor = Vector4f(0.75f,0.75f,0.75f,1f)
        var glossines = 32f
        var rimcolor = Vector4f(1f,1f,1f,1f)
        var rimamount = 0.716f
        var rimthreshhold = 0.1f

        toonShader.setUniform("direction",direction)
        toonShader.setUniform("intensity",intensity)
        toonShader.setUniform("diffuse", diffuse)
        toonShader.setUniform("color",color)
        toonShader.setUniform("ambientcolor",ambientcolor)
        toonShader.setUniform("specularcolor",specularcolor)
        toonShader.setUniform("glossiness", glossines)
        toonShader.setUniform("rimcolor", rimcolor)
        toonShader.setUniform("rimamount", rimamount)
        toonShader.setUniform("rimthreshhold", rimthreshhold)
*/

        //move camera a little bit in z direction
        camera.rotateLocal(Math.toRadians(-35.0f), 0.0f, 0.0f)
        camera.translateLocal(Vector3f(0.0f, 0.0f, 4.0f))

        //init objectmanager
        objectManager = ObjectManager()

        //init gameobjects and assigne to objectmanager and set shaders
        var ground = Ground()
        ground.init(camera)
        objectManager.addObject(ground)
        ground.setShader(staticShader)

        var car = Car()
        car.init(camera)
        objectManager.addObject(car)
        car.setShader(staticShader)
        camera.parent = car

        var skybox = Skybox()
        skybox.init(camera)
        objectManager.addObject(skybox)
        skybox.setShader(staticShader)

        var debuff = Debuff()
        debuff.init(camera)
        objectManager.addObject(debuff)
        debuff.setShader(staticShader)

        var powerup = PowerUp()
        powerup.init(camera)
        objectManager.addObject(powerup)
        powerup.setShader(staticShader)

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        objectManager.render(dt,t)

        staticShader.use()
        camera.bind(staticShader)

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
                car.rotateLocal(0.0f, yawangle, 0.0f)
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