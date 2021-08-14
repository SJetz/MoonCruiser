package cga.exercise.components.geometry

import cga.exercise.components.mooncruiser.physic.Cube
import org.joml.Matrix4f
import org.joml.Vector3f

open class Transformable(var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null): ITransformable {



    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        modelMatrix.rotateXYZ(pitch, yaw, roll)
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
        val tmp = Matrix4f()
        tmp.translate(altMidpoint)
        tmp.rotateXYZ(pitch, yaw, roll)
        tmp.translate(Vector3f(altMidpoint).negate())
        modelMatrix = tmp.mul(modelMatrix)
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    override fun translateLocal(deltaPos: Vector3f) {
        modelMatrix.translate(deltaPos)
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    override fun translateGlobal(deltaPos: Vector3f) {
        modelMatrix = Matrix4f().translate(deltaPos).mul(modelMatrix)
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    override fun scaleLocal(scale: Vector3f) {
        modelMatrix.scale(scale)
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    override fun getPosition(): Vector3f {
        return Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32())
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    override fun getWorldPosition(): Vector3f {
        val wmat = getWorldModelMatrix()
        return Vector3f(wmat.m30(), wmat.m31(), wmat.m32())
    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    override fun getXAxis(): Vector3f {
        return Vector3f(
                modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02()
        ).normalize()
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    override fun getYAxis(): Vector3f {
        return Vector3f(
                modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12()
        ).normalize()
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    override fun getZAxis(): Vector3f {
        return Vector3f(
                modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22()
        ).normalize()
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    override fun getWorldXAxis(): Vector3f {
        val wmat = getWorldModelMatrix()
        return Vector3f(
                wmat.m00(), wmat.m01(), wmat.m02()
        ).normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    override fun getWorldYAxis(): Vector3f {
        val wmat = getWorldModelMatrix()
        return Vector3f(
                wmat.m10(), wmat.m11(), wmat.m12()
        ).normalize()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    override fun getWorldZAxis(): Vector3f {
        val wmat = getWorldModelMatrix()
        return Vector3f(
                wmat.m20(), wmat.m21(), wmat.m22()
        ).normalize()
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    override fun getWorldModelMatrix(): Matrix4f {
        val worldMatrix = Matrix4f(modelMatrix)
        parent?.getWorldModelMatrix()?.mul(modelMatrix, worldMatrix)
        return worldMatrix
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    override fun getLocalModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }
}