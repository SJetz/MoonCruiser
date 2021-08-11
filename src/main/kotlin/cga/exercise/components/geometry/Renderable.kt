package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow


/**
 * Extends Transformable such that the object can render Mesh objects transformed by Transformable
 */
open abstract class Renderable() : Transformable(parent = null), IRenderable {
    protected var myShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    var myMeshes : MutableList<Mesh> = mutableListOf()

    /**
     * Renders all meshes attached to this Renderable, applying the transformation matrix to
     * each of them
     */
    override fun render() {
        myShader.use()
        myShader.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (m in myMeshes) {
            m.render(myShader)
        }
    }

    abstract override fun update(dt: Float, window: GameWindow)


    override fun setShader(shaderProgram: ShaderProgram) {
        myShader = shaderProgram
    }

    abstract override fun init()
}
