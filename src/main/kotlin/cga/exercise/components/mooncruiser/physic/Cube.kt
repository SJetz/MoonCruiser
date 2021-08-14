package cga.exercise.components.mooncruiser.physic

import org.joml.Vector3f

class Cube(position: Vector3f) {
    val myVertices : Array<Vector3f>
    private val defaultSize : Float = 1f
    var openList = mutableListOf<Cube>()
    var closedList = mutableListOf<Cube>()

init {
        myVertices = arrayOf<Vector3f>(
            //unten links hinten
                    Vector3f(position.x-(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z+(0.5f*defaultSize)),
                    //unten rechts hinten
                    Vector3f(position.x+(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z+(0.5f*defaultSize)),
                    //unten rechts vorne
                    Vector3f(position.x+(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z-(0.5f*defaultSize)),
                    //unten links vorne
                    Vector3f(position.x-(0.5f*defaultSize),position.y-(0.5f*defaultSize),position.z-(0.5f*defaultSize)),

                    //oben links hinten
                    Vector3f(position.x-(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z+(0.5f*defaultSize)),
                    //oben rechts hinten
                    Vector3f(position.x+(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z+(0.5f*defaultSize)),
                    //oben rechts vorne
                    Vector3f(position.x+(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z-(0.5f*defaultSize)),
                    //oben links vorne
                    Vector3f(position.x-(0.5f*defaultSize),position.y+(0.5f*defaultSize),position.z-(0.5f*defaultSize))
        )

    }
    }
