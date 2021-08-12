package cga.exercise.components.mooncruiser.GameObjects

import cga.exercise.components.geometry.Renderable
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW


class Car() : Renderable() {

    //private val carShader : ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

    override fun update(dt: Float, window: GameWindow) {
        val movemul = 5.0f
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
        if (window.getKeyState(GLFW.GLFW_KEY_F)) {
            rotateLocal(Math.PI.toFloat() * dt, 0.0f, 0.0f)
        }

    }

    override fun init() {
        super.myMeshes = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(-90.0f), Math.toRadians(90.0f), 0.0f) ?: throw IllegalArgumentException("Could not load the model")
        this.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

    }

}