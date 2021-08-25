package cga.exercise.components.mooncruiser.GameObjects

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.light.SpotLight
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW


class Car(movespeed : Float) : Renderable(){

    lateinit var groundColor : Vector3f
    var x = Vector3f(0f, 0f, 0f)
    var y = Vector3f(50.0f, 50.0f, 50.0f)

    //Scheinwerferlicht_1 An/Aus
    lateinit var lightOn : SpotLight
    lateinit var lightOff : SpotLight

    //Scheinwerferlicht_2 An/Aus
    lateinit var lightOn2 : SpotLight
    lateinit var lightOff2 : SpotLight

    //Status der beiden Scheinwerfer (An oder Aus)
    lateinit var activeLight : SpotLight
    lateinit var activeLight2 : SpotLight

    var buttonPressed : Boolean = false
    var movemul = movespeed

    override fun init(carCam: TronCamera) {
        // PhysicManager.listOfAllCubes.add(this)
        // this.collider = true

        lightOff= SpotLight(Vector3f(0.0f, 0.0f, 0.0f), Vector3f(-0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))
        lightOff2= SpotLight(Vector3f(0.0f, 0.0f, 0.0f), Vector3f(-0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))

        myCamera = carCam
        super.myMeshes = ModelLoader.loadModel("assets/car/car/sportcar.017.fbx", Math.toRadians(-90.0f), Math.toRadians(180.0f), 0.0f) ?: throw IllegalArgumentException("Could not load the model")
        this.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

        //car Scheinwerfer
        lightOn = SpotLight(Vector3f(50.0f, 50.0f, 50.0f), Vector3f(-0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))
        lightOn.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        lightOn.parent = this

        //car Scheinwerfer 2
        lightOn2 = SpotLight(Vector3f(50.0f, 50.0f, 50.0f), Vector3f(0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))
        lightOn2.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        lightOn2.parent = this

        groundColor = Vector3f(1.0f, 1.0f, 1.0f)

        activeLight = lightOn
        activeLight2 = lightOn2
    }

    override fun update(dt: Float, window: GameWindow) {
        val rotatemul = 2.0f

        if (window.getKeyState(GLFW.GLFW_KEY_W)) {
            translateLocal(Vector3f(0.0f, 0.0f, -dt * movemul))
        }
        if (window.getKeyState(GLFW.GLFW_KEY_S)) {
            translateLocal(Vector3f(0.0f, 0.0f, dt * movemul))
        }
        if (window.getKeyState(GLFW.GLFW_KEY_A) and window.getKeyState(GLFW.GLFW_KEY_W)) {
            rotateLocal(0.0f, dt * rotatemul, 0.0f)
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D) and window.getKeyState(GLFW.GLFW_KEY_W)) {
            rotateLocal( 0.0f, -dt * rotatemul,0.0f)
        }
        if (window.getKeyState(GLFW.GLFW_KEY_A) and window.getKeyState(GLFW.GLFW_KEY_S)) {
            rotateLocal(0.0f, dt * rotatemul, 0.0f)
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D) and window.getKeyState(GLFW.GLFW_KEY_S)) {
            rotateLocal( 0.0f, -dt * rotatemul,0.0f)
        }
        if(window.getKeyState(GLFW.GLFW_KEY_SPACE)&& !buttonPressed) {
           if(activeLight==lightOn&& activeLight2 ==lightOn2){
               activeLight = lightOff
               activeLight2 = lightOff2
           }else{
               activeLight = lightOn
               activeLight2 = lightOn2
           }
        }
        buttonPressed = window.getKeyState(GLFW.GLFW_KEY_SPACE)
    }

    override fun render(dt: Float, t: Float) {
        super.render(dt, t)

        val changingColor = Vector3f(Math.abs(Math.sin(t)), 0f, Math.abs(Math.cos(t)))

        myShader.setUniform("shadingColor", changingColor)

        activeLight.bind(myShader, "bikeSpotLight", myCamera.calculateViewMatrix())
        activeLight2.bind(myShader, "bikeSpotLight2", myCamera.calculateViewMatrix())

        myShader.setUniform("shadingColor", groundColor)
    }

    fun setPosition(deltaPosition : Vector3f){
        modelMatrix.identity()
        modelMatrix.translate(deltaPosition)
    }

}