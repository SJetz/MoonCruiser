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
    lateinit var carSpotLight : SpotLight
    lateinit var carSpotLight2 : SpotLight

    //var translateVector = Vector3f(0f,0f,0f)
    var movemul = movespeed

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
    }

    override fun init(carCam: TronCamera) {
       // PhysicManager.listOfAllCubes.add(this)
       // this.collider = true

        myCamera = carCam
        super.myMeshes = ModelLoader.loadModel("assets/car/car/sportcar.017.fbx", Math.toRadians(-90.0f), Math.toRadians(180.0f), 0.0f) ?: throw IllegalArgumentException("Could not load the model")
        this.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

        //car scheinwerfer
        carSpotLight = SpotLight(Vector3f(50.0f, 50.0f, 50.0f), Vector3f(-0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))
        carSpotLight.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        carSpotLight.parent = this

        //car scheinwerfer 2
        carSpotLight2 = SpotLight(Vector3f(50.0f, 50.0f, 50.0f), Vector3f(0.5f, 0.25f, -1.75f), Math.toRadians(10.0f), Math.toRadians(30.0f))
        carSpotLight2.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        carSpotLight2.parent = this


        groundColor = Vector3f(1.0f, 1.0f, 1.0f)
    }

    override fun render(dt: Float, t: Float) {
        super.render(dt, t)

        val changingColor = Vector3f(Math.abs(Math.sin(t)), 0f, Math.abs(Math.cos(t)))

        myShader.setUniform("shadingColor", changingColor)

        carSpotLight.bind(myShader, "bikeSpotLight", myCamera.calculateViewMatrix())
        carSpotLight2.bind(myShader, "bikeSpotLight2", myCamera.calculateViewMatrix())

        myShader.setUniform("shadingColor", groundColor)
    }

    fun setPoition(deltaPosition : Vector3f){
        modelMatrix.identity()
        modelMatrix.translate(deltaPosition)
    }

}