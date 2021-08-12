package cga.exercise.components.geometry

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow


/**
 * Extends Transformable such that the object can render Mesh objects transformed by Transformable
 */
open class Renderable() : Transformable(parent = null), IRenderable {
    var myMeshes: MutableList<Mesh> = mutableListOf()
    var myShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    lateinit var myCamera : TronCamera

    /**
     * Renders all meshes attached to this Renderable, applying the transformation matrix to
     * each of them
     */
    override fun render(dt: Float, t: Float) {
        myShader.use()
        myShader.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (m in myMeshes) {
            m.render(myShader)
        }
    }

    override fun update(dt: Float, window: GameWindow){}

    override fun setShader(shader: ShaderProgram) {
        myShader = shader
    }

    override fun init(camera: TronCamera){
        myCamera = camera

    }
}
