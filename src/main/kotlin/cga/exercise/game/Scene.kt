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
    var debuff = Debuff(Vector3f(0f,0f,0f), Vector3f(0f,0f,0f))
    var listOfDebuffs = mutableListOf<Debuff>()
    var k = 0f
    val powerUpNumber = 10f
    val debuffNumber = 5f
    var debuffCounter = 0f
    var ground = Ground()

    //timer
    val timer = 2.0f
    var timeHasPast = 0.0f
    val topspeed = 25.0f
    val lowspeed = 5.0f
    val defaultSpeed = 15.0f

    //obecjtmanger
    lateinit var objectManager : ObjectManager

    //shader
    private val toonShader = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")

    var shader: ShaderProgram = toonShader

    //camera
    lateinit var cameraFront : TronCamera
    lateinit var cameraBack : TronCamera
    lateinit var cameraActive : TronCamera

    //mouse
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true

    fun load(){
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
        ground = Ground()
        ground.init(cameraActive)
        objectManager.addObject(ground)
        ground.setShader(shader)

        car = Car(defaultSpeed)
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
        while (x < debuffNumber){
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
        powerupStarter = PowerUp(Vector3f(Math.random().toFloat()*60,1f,Math.random().toFloat()*60))
        powerupStarter.init(cameraActive)
        powerupStarter.setShader(shader)
        objectManager.addObject(powerupStarter)

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()
    }
    //scene setup
    init {
        load()
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        objectManager.render(dt,t)

        shader.use()
        cameraActive.bind(shader)

    }

    fun update(dt: Float, t: Float) {

        objectManager.update(dt,window)

        if(timeHasPast<0){
            timeHasPast = 0f
        }else
            timeHasPast -= dt

        if (timeHasPast <= 0f){
            car.movemul = defaultSpeed
        }

        if(window.getKeyState(GLFW_KEY_LEFT_SHIFT)) {
            cameraActive = cameraBack
        }else
            cameraActive = cameraFront


            if(PhysicManager.checkForCollision(car,powerupStarter) <= 2f ){
                objectManager.removeObject(powerupStarter)
                timeHasPast = timer
                car.movemul = topspeed
                if (k < powerUpNumber) {
                    powerupStarter.setPoition(Vector3f(Math.random().toFloat() * 60, 1f, Math.random().toFloat() * 60))
                    objectManager.addObject(powerupStarter)
                    k++
                    if(k == powerUpNumber){
                        resetScene()
                        println("VICTORY")
                    }
                }
            }

        var debuffKicker = Debuff(Vector3f(0f,0f,0f), Vector3f(0f,0f,0f))
        for(i in listOfDebuffs){
            if (PhysicManager.checkForCollision(car,i) <= 2f){
                debuffKicker = i
                objectManager.removeObject(i)
                debuffCounter++
                timeHasPast = timer
                car.movemul = lowspeed
                   if(debuffCounter == debuffNumber){
                        resetScene()
                        println("DEFEATE")
                   }
            }
        }
        if (listOfDebuffs.contains(debuffKicker)){
            listOfDebuffs.remove(debuffKicker)
        }

        if(car.getPosition().x >= 108f ){
            car.setPoition(Vector3f(car.getPosition().x-8,0f,car.getPosition().z))
        }

        if(car.getPosition().x <= -108f ){
            car.setPoition(Vector3f(car.getPosition().x+8,0f,car.getPosition().z))
        }

        if(car.getPosition().z >= 108f ){
            car.setPoition(Vector3f(car.getPosition().x,0f,car.getPosition().z-8))
        }

        if(car.getPosition().z <= -108f ){
            car.setPoition(Vector3f(car.getPosition().x,0f,car.getPosition().z+8))
        }

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun resetScene(){
        car = Car(0f)
        powerupStarter = PowerUp(Vector3f(0f,0f,0f))
        debuff = Debuff(Vector3f(0f,0f,0f), Vector3f(0f,0f,0f))
        listOfDebuffs = mutableListOf<Debuff>()
        k = 0f
        debuffCounter = 0f

        //timer
        timeHasPast = 0.0f

        objectManager.reset()
        load()
    }

    fun cleanup() {}
}