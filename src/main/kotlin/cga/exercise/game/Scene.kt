package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.mooncruiser.GameObjects.*
import cga.exercise.components.mooncruiser.GameObjects.ObjectManager
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
    private val simpleShader = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")
    private val toonShader = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")

    //camera
    var cameraFront : TronCamera
    var cameraBack : TronCamera
    var cameraActive : TronCamera

    //mouse
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true

    //scene setup
    init {

        //setup cameraActive, cameraFront, cameraBack
        cameraFront = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            100.0f
        )

        cameraBack = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            100.0f
        )

        cameraActive = cameraFront

        //move cameras a little bit in z direction
        cameraFront.rotateLocal(Math.toRadians(-35.0f), 0.0f, 0.0f)
        cameraFront.translateLocal(Vector3f(0.0f, 0.0f, 4.0f))

        cameraBack.rotateLocal(Math.toRadians(-145.0f), 0.0f, 110.0f)
        cameraBack.translateLocal(Vector3f(0.0f, 0.0f, 4.0f))



        //init objectmanager
        objectManager = ObjectManager()

        //init gameobjects and assigne to objectmanager and set shaders
        var ground = Ground()
        ground.init(cameraActive)
        objectManager.addObject(ground)
        ground.setShader(toonShader)

        var car = Car()
        car.init(cameraActive)
        objectManager.addObject(car)
        car.setShader(toonShader)
        cameraActive.parent = car
        cameraBack.parent = car

        var skybox = Skybox()
        skybox.init(cameraActive)
        objectManager.addObject(skybox)
        skybox.setShader(toonShader)

        var debuff = Debuff()
        debuff.init(cameraActive)
        objectManager.addObject(debuff)
        debuff.setShader(toonShader)

        var powerup = PowerUp()
        powerup.init(cameraActive)
        objectManager.addObject(powerup)
        powerup.setShader(toonShader)

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        objectManager.render(dt,t)

        toonShader.use()
        cameraActive.bind(toonShader)

    }


    //camera update
    fun update(dt: Float, t: Float) {
        objectManager.update(dt,window)

       if(window.getKeyState(GLFW_KEY_LEFT_SHIFT)) {
           cameraActive = cameraBack
       }else
            cameraActive= cameraFront
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawangle = (xpos - oldMouseX).toFloat() * 0.002f
            val pitchangle = (ypos - oldMouseY).toFloat() * 0.002f
            if (!window.getKeyState(GLFW_KEY_LEFT_ALT)) {
                car.rotateLocal(0.0f, yawangle, 0.0f)
            }
            else{
                cameraActive.rotateAroundPoint(0.0f, yawangle, 0.0f, Vector3f(0.0f, 0.0f, 0.0f))
            }
        } else firstMouseMove = false
        oldMouseX = xpos
        oldMouseY = ypos
    }

    fun cleanup() {}
}