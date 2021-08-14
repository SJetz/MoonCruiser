package cga.exercise.components.mooncruiser.physic

import cga.exercise.components.geometry.Transformable
import org.joml.Matrix4f
import org.joml.Vector3f

open class Cube(pos: Vector3f) : Transformable(){
    lateinit var myVertices : Array<Vector3f>
    private var defaultSize : Vector3f = Vector3f(1f,1f,1f)
    var openList = mutableListOf<Cube>()
    var closedList = mutableListOf<Cube>()
    var globalPosition : Vector3f = Vector3f()
    var globalRotation : Matrix4f = Matrix4f()
    var formerSize : Vector3f = Vector3f(1f,1f,1f)
    var formerPosition : Vector3f = Vector3f(0f,0f,0f)

init {
        setPosition(pos)
    }

    private fun setPosition(position: Vector3f){
        globalPosition = position
        myVertices = arrayOf<Vector3f>(
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

    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
        super.rotateAroundPoint(pitch, yaw, roll, altMidpoint)
    }

    override fun scaleLocal(scale: Vector3f) {
        defaultSize = scale
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            super.scaleLocal(scale)
        }else{
            defaultSize = formerSize
        setPosition(globalPosition)}
    }

    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        super.rotateLocal(pitch, yaw, roll)
    }

    override fun translateGlobal(deltaPos: Vector3f) {
        globalPosition = globalPosition.add(deltaPos)
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            super.translateGlobal(deltaPos)
        }else{
            globalPosition = formerPosition
        setPosition(globalPosition)}
    }

    override fun translateLocal(deltaPos: Vector3f) {
        globalPosition = globalPosition.add(deltaPos)
        setPosition(globalPosition)
        if (!PhysicManager.checkCollision(this)){
            super.translateLocal(deltaPos)
        }else{
            globalPosition = formerPosition
            setPosition(globalPosition)}
    }


}



