package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class SpotLight(lightColor: Vector3f, position: Vector3f, var innerCone: Float, var outerCone: Float) : PointLight(lightColor, position), ISpotLight {

    init {
        att = Vector3f(1.0f, 0.5f, 0.2f)
    }

    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f) {
        super.bind(shaderProgram, name)
        shaderProgram.setUniform(name + "Cone", Vector2f(innerCone, outerCone))
        shaderProgram.setUniform(name + "Direction", Vector3f(getWorldZAxis()).negate().mul(Matrix3f(viewMatrix)))
    }
}
