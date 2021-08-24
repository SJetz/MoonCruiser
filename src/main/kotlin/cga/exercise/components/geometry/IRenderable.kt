package cga.exercise.components.geometry

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow

interface IRenderable {
    fun render(dt: Float, t: Float)
    fun update(dt: Float, window: GameWindow)
    fun setShader(shaderProgram: ShaderProgram)
    fun init(camera: TronCamera)

}