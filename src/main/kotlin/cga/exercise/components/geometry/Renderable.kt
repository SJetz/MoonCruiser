package cga.exercise.components.geometry

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow

open class Renderable() : Transformable(parent = null), IRenderable {
    var myMeshes: MutableList<Mesh> = mutableListOf()
    var myShader: ShaderProgram = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")
    var myCamera : TronCamera = TronCamera()

    override fun render(dt: Float, t: Float) {
        myShader.use()
        myCamera.bind(myShader)
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
