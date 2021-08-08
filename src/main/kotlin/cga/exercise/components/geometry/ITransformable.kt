package cga.exercise.components.geometry

import org.joml.Matrix4f
import org.joml.Vector3f

interface ITransformable {
    fun rotateLocal(pitch: Float, yaw: Float, roll: Float)
    fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f)

    fun translateLocal(deltaPos: Vector3f)
    fun translateGlobal(deltaPos: Vector3f)

    fun scaleLocal(scale: Vector3f)

    fun getPosition(): Vector3f
    fun getWorldPosition(): Vector3f
    fun getXAxis(): Vector3f
    fun getYAxis(): Vector3f
    fun getZAxis(): Vector3f
    fun getWorldXAxis(): Vector3f
    fun getWorldYAxis(): Vector3f
    fun getWorldZAxis(): Vector3f

    fun getWorldModelMatrix(): Matrix4f
    fun getLocalModelMatrix(): Matrix4f
}