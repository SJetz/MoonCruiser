package cga.exercise.game

import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera

import cga.exercise.components.mooncruiser.GameObjects.*
import cga.exercise.components.mooncruiser.GameObjects.ObjectManager
import cga.exercise.components.mooncruiser.physic.PhysicManager

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

    var car = Car(0f)
    var powerupStarter = PowerUp(Vector3f(0f,0f,0f))
    var powerup = PowerUp(Vector3f(0f,0f,0f))
    var debuff = Debuff(Vector3f(0f,0f,0f), Vector3f(0f,0f,0f))
    var listOfDebuffs = mutableListOf<Debuff>()
    var listOfPowerUps = mutableListOf<PowerUp>()

    //obecjtmanger
    private val objectManager : ObjectManager

    //shader
    private val toonShader = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")

    var shader: ShaderProgram = toonShader

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
            1000.0f
        )

        cameraBack = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            1000.0f
        )

        cameraActive = cameraFront

        //move cameras a little bit in z direction
        cameraFront.rotateLocal(Math.toRadians(-35.0f), 0.0f, 0.0f)
        cameraFront.translateLocal(Vector3f(0.0f, 0.0f, 8.0f))

        cameraBack.rotateLocal(Math.toRadians(-145.0f), 0.0f, Math.toRadians(180.0f))
        cameraBack.translateLocal(Vector3f(0.0f, 0.0f, 8.0f))



        //init objectmanager
        objectManager = ObjectManager()

        //init gameobjects and assigne to objectmanager and set shaders
        var ground = Ground()
        ground.init(cameraActive)
        objectManager.addObject(ground)
        ground.setShader(shader)

        car = Car(15f)
        car.init(cameraActive)
        objectManager.addObject(car)
        car.setShader(shader)
        cameraActive.parent = car
        cameraBack.parent = car


        var skybox = Skybox()
        skybox.init(cameraActive)
        objectManager.addObject(skybox)
        skybox.setShader(shader)

        //DEBUFFS
        var x = 0
        while (x < 5){
        debuff = Debuff(Vector3f(Math.random().toFloat()*50, 1f, Math.random().toFloat()*50), Vector3f(Math.random().toFloat()*50, Math.random().toFloat()*50, Math.random().toFloat()*50))
        debuff.init(cameraActive)
        debuff.setShader(shader)
        listOfDebuffs.add(debuff)
        x++
        }
        for(i in listOfDebuffs){
            objectManager.addObject(i)
        }

        //POWERUPS
        powerupStarter = PowerUp(Vector3f(Math.random().toFloat()*50,1f,Math.random().toFloat()*50))
        powerupStarter.init(cameraActive)
        objectManager.addObject(powerupStarter)
        powerupStarter.setShader(shader)

        var y = 0
        while (y < 5){
            powerup = PowerUp(Vector3f(Math.random().toFloat()*50, 1f, Math.random().toFloat()*50))
            powerup.init(cameraActive)
            powerup.setShader(shader)
            listOfPowerUps.add(powerup)
            y++
        }
       /* for(i in listOfPowerUps){
            objectManager.addObject(i)
        }*/


        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        objectManager.render(dt,t)

        shader.use()
        cameraActive.bind(shader)

    }

    //camera update
    fun update(dt: Float, t: Float) {

        objectManager.update(dt,window)

        if(window.getKeyState(GLFW_KEY_LEFT_SHIFT)) {
            cameraActive = cameraBack
        }else
            cameraActive = cameraFront

            if(PhysicManager.checkForCollision(car,powerupStarter) <= 2f ){
                objectManager.removeObject(powerupStarter)
                car.movemul = 25f
            }else{
                for(i in listOfDebuffs){
                if (PhysicManager.checkForCollision(car,i) <= 2f){
                    objectManager.removeObject(i)
                    car.movemul = 10f
                }
                }
            }

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun cleanup() {}
}