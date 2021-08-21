package cga.exercise.components.mooncruiser.physic

import cga.exercise.components.geometry.Renderable
import org.joml.Math

class PhysicManager {

    companion object Instance {
        var listOfAllCubes = mutableListOf<Cube>()

        fun checkCollision(j: Cube): Boolean {
            for (i in listOfAllCubes) {
                if ( i == j )
                    continue
                if (CollisionsDetection.CubeinCube(i, j)) {
                    return true
                }

            }
            return false
        }

        fun checkForCollision(renderable1: Renderable, renderable2: Renderable) : Float{
            val xDistance=renderable1.getPosition().x-renderable2.getPosition().x
            val yDistance=renderable1.getPosition().y-renderable2.getPosition().y
            val zDistance=renderable1.getPosition().z-renderable2.getPosition().z
            return Math.sqrt((xDistance*xDistance).toDouble()+ (yDistance*yDistance).toDouble()+(zDistance*zDistance).toDouble()).toFloat()
    }

}
}



