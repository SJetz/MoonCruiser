package cga.exercise.components.mooncruiser.physic

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
    }
}

