package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.mooncruiser.GameObjects.Car
import cga.exercise.components.mooncruiser.GameObjects.Ground
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

    //obecjtmanger
    private val objectManager : ObjectManager

    //shader
    private val staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

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
                //bike.rotateLocal(0.0f, yawangle, 0.0f)
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