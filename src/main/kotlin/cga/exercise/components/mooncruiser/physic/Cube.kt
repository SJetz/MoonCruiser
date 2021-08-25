package cga.exercise.components.mooncruiser.physic

import cga.exercise.components.geometry.*
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

open class Cube() : Transformable(){
    lateinit var myVertices : Array<Vector3f>
    private var defaultSize : Vector3f = Vector3f(1f,1f,1f)
    var globalPosition : Vector3f = Vector3f()
    var globalRotation : Matrix3f = Matrix3f(cos(2f),-sin(2f),0f, sin(2f),cos(2f),0f,0f,0f,1f)
    var formerSize : Vector3f = Vector3f(1f,1f,1f)
    var formerPosition : Vector3f = Vector3f(0f,0f,0f)
    var trigger : Boolean = false
    var collider : Boolean = false


init {
        setPosition(Vector3f(0f,0f,0f))
    }

    private fun setPosition(position: Vector3f){
        globalPosition = position
        myVertices = arrayOf(
            //unten links hinten
            Vector3f(position.x-(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z)),
            //unten rechts hinten
            Vector3f(position.x+(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z)),
            //unten rechts vorne
            Vector3f(position.x+(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z)),
            //unten links vorne
            Vector3f(position.x-(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z)),

            //oben links hinten
            Vector3f(position.x-(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z)),
            //oben rechts hinten
            Vector3f(position.x+(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z)),
            //oben rechts vorne
            Vector3f(position.x+(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z)),
            //oben links vorne
            Vector3f(position.x-(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z))
        )

    }

    private fun setPositionRotation(position: Vector3f,float: Float){
        globalPosition = position
        if(float<0) {
            globalRotation = Matrix3f(cos(-2f), -sin(-2f), 0f, sin(-2f), cos(-2f), 0f, 0f, 0f, 1f)
        }else {globalRotation = Matrix3f(cos(2f),-sin(2f),0f, sin(2f),cos(2f),0f,0f,0f,1f)}

        myVertices = arrayOf(
            //unten links hinten
            (Vector3f(position.x-(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z))).mul(globalRotation),
            //unten rechts hinten
            (Vector3f(position.x+(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z))).mul(globalRotation),
            //unten rechts vorne
            (Vector3f(position.x+(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z))).mul(globalRotation),
            //unten links vorne
            (Vector3f(position.x-(0.5f*defaultSize.x),position.y-(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z))).mul(globalRotation),
            //oben links hinten
            (Vector3f(position.x-(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z))).mul(globalRotation),
            //oben rechts hinten
            (Vector3f(position.x+(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z+(0.5f*defaultSize.z))).mul(globalRotation),
            //oben rechts vorne
            (Vector3f(position.x+(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z))).mul(globalRotation),
            //oben links vorne
            (Vector3f(position.x-(0.5f*defaultSize.x),position.y+(0.5f*defaultSize.y),position.z-(0.5f*defaultSize.z))).mul(globalRotation)
        )

    }

    override fun scaleLocal(scale: Vector3f){
        if (!collider && !trigger){
            super.scaleLocal(scale)
            return
        }
        defaultSize = scale
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            super.scaleLocal(scale)
            formerSize = defaultSize
        }else{
            defaultSize = formerSize
            setPosition(globalPosition)
        }
    }

    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        if (!collider && !trigger) {
            super.rotateLocal(pitch, yaw, roll)
            return
        }
        globalPosition = globalPosition.add(pitch,yaw,roll)
        setPositionRotation(globalPosition,yaw)
        if (!PhysicManager.checkCollision(this)){
            formerPosition = globalPosition
            super.rotateLocal(pitch, yaw, roll)
        }else{
            globalPosition = formerPosition
            setPositionRotation(globalPosition,yaw)
        }
    }

    override fun translateGlobal(deltaPos: Vector3f) {
        if (!collider && !trigger){
            super.translateGlobal(deltaPos)
            return
        }
        globalPosition = globalPosition.add(deltaPos)
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            super.translateGlobal(deltaPos)
            formerPosition = globalPosition
        }else{
            globalPosition = formerPosition
            setPosition(globalPosition)
        }
    }

    override fun translateLocal(deltaPos: Vector3f) {
        if (!collider && !trigger){
            super.translateLocal(deltaPos)
            return
        }
        globalPosition = globalPosition.add(deltaPos)
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            formerPosition = globalPosition
            super.translateLocal(deltaPos)
        }else{
            globalPosition = formerPosition
            setPosition(globalPosition)
        }
    }


}



