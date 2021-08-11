package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import java.awt.Window

interface IRenderable {
    fun render()
    abstract fun update(dt: Float, window: GameWindow)
    fun setShader(shaderProgram: ShaderProgram)
    abstract fun init()

}