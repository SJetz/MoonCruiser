package cga.exercise.components.mooncruiser.physic

class PhysicManager {

    companion object Instance{
        var listOfAllCubes = mutableListOf<Cube>()

        fun checkCollision(j : Cube) : Boolean{
            for (i in listOfAllCubes){
                    if (i.closedList.contains(i) || i==j)
                        continue
                    if (CollisionsDetection.CubeinCube(i,j)){
                        return true
                    }else{
                        i.closedList.add(j)
                        j.closedList.add(i)
                        return false
                    }
                }
            return false
        }
    }
}