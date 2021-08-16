package cga.exercise.components.mooncruiser.GameObjects

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.mooncruiser.physic.PhysicManager
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW


class Car() : Renderable() {

    lateinit var carPointLight : PointLight
    lateinit var carSpotLight :SpotLight
    lateinit var groundColor : Vector3f
    lateinit var carPointLight2 : PointLight
    lateinit var carPointLight3 : PointLight

    var translateVector = Vector3f(0f,0f,0f)

    override fun update(dt: Float, window: GameWindow) {
        val movemul = 15.0f
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
        PhysicManager.listOfAllCubes.add(this)
        this.collider = true

        translateLocal(translateVector)

        myCamera = carCam
        super.myMeshes = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(-90.0f), Math.toRadians(90.0f), 0.0f) ?: throw IllegalArgumentException("Could not load the model")
        this.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

        //bike point light
        carPointLight = PointLight(Vector3f(0.0f, 2.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        carPointLight.parent = this

        //bike spot light
        carSpotLight = SpotLight(Vector3f(1.0f, 1.0f, 1.0f), Vector3f(0.0f, 1.0f, -2.0f), Math.toRadians(20.0f), Math.toRadians(30.0f))
        carSpotLight.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        carSpotLight.parent = this
        groundColor = Vector3f(1.0f, 1.0f, 1.0f)
        carPointLight2 = PointLight(Vector3f(0.0f, 2.0f, 2.0f), Vector3f(-10.0f, 2.0f, -10.0f))
        carPointLight3 = PointLight(Vector3f(2.0f, 0.0f, 0.0f), Vector3f(10.0f, 2.0f, 10.0f))
    }

    override fun render(dt: Float, t: Float) {
        super.render(dt, t)
        val changingColor = Vector3f(Math.abs(Math.sin(t)), 0f, Math.abs(Math.cos(t)))
        carPointLight.lightColor = changingColor
        myShader.setUniform("shadingColor", changingColor)
        carPointLight.bind(myShader, "bikePointLight")
        carPointLight2.bind(myShader, "bikePointLight2")
        carPointLight3.bind(myShader, "bikePointLight3")
        carSpotLight.bind(myShader, "bikeSpotLight", myCamera.calculateViewMatrix())
        myShader.setUniform("shadingColor", groundColor)
    }

}