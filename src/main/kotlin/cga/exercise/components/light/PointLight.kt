package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(var lightColor: Vector3f, position: Vector3f) : Transformable(), IPointLight {
    var att = Vector3f(1.0f, 0.5f, 0.2f)

    init {
        translateGlobal(position)
    }

    override fun bind(shaderProgram: ShaderProgram, name: String) {
        shaderProgram.setUniform(name + "Color", lightColor)
        shaderProgram.setUniform(name + "Position", getWorldPosition())
        //println(getWorldPosition())
        shaderProgram.setUniform(name + "AttParams", att)
    }
}