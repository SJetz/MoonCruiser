package cga.exercise.components.mooncruiser.physic

import org.joml.Vector3f

class Cube {
    val myVerticies = arrayOf<Vector3f>()
    val defaultSize : Float = 1f

    fun Cube (position : Vector3f){

        //unten links hinten
        myVerticies[0] = Vector3f(position.x-(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z+(0.5f*defaultSize))
        //unten rechts hinten
        myVerticies[1] = Vector3f(position.x+(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z+(0.5f*defaultSize))
        //unten rechts vorne
        myVerticies[2] = Vector3f(position.x+(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z-(0.5f*defaultSize))
        //unten links vorne
        myVerticies[3] = Vector3f(position.x-(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z-(0.5f*defaultSize))

        //oben links hinten
        myVerticies[4] = Vector3f(position.x-(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z+(0.5f*defaultSize))
        //oben rechts hinten
        myVerticies[5] = Vector3f(position.x+(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z+(0.5f*defaultSize))
        //oben rechts vorne
        myVerticies[6] = Vector3f(position.x+(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z-(0.5f*defaultSize))
        //oben links vorne
        myVerticies[7] = Vector3f(position.x-(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z-(0.5f*defaultSize))
    }
}