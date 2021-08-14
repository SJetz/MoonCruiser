package cga.exercise.components.mooncruiser.physic

import cga.exercise.components.geometry.Renderable
import org.joml.Vector3f

class CollisionsDetection {

    companion object Foo {
        fun CubeinCube(cube1 : Cube, cube2 : Cube): Boolean {
            var a1 = (cube1.myVerticies[0].sub(cube1.myVerticies[1]))
            var a2 = (cube1.myVerticies[0].sub(cube1.myVerticies[3]))
            var a3 = (cube1.myVerticies[0].sub(cube1.myVerticies[4]))

            var b1 = (cube2.myVerticies[0].sub(cube2.myVerticies[1]))
            var b2 = (cube2.myVerticies[0].sub(cube2.myVerticies[3]))
            var b3 = (cube2.myVerticies[0].sub(cube2.myVerticies[4]))

            var vectors :Array<Vector3f> = arrayOf()

            vectors[0] = a1.cross(a2).normalize()
            vectors[1] = a1.cross(a3).normalize()
            vectors[2] = a2.cross(a3).normalize()

            vectors[3] = b1.cross(b2).normalize()
            vectors[4] = b1.cross(b3).normalize()
            vectors[5] = b2.cross(b3).normalize()

            vectors[6] = a1.cross(b1).normalize()
            vectors[7] = a1.cross(b2).normalize()
            vectors[8] = a1.cross(b3).normalize()

            vectors[9] = a2.cross(b1).normalize()
            vectors[10] = a2.cross(b2).normalize()
            vectors[11] = a2.cross(b3).normalize()

            vectors[12] = a3.cross(b1).normalize()
            vectors[13] = a3.cross(b2).normalize()
            vectors[14] = a3.cross(b3).normalize()

            var j = 0
            while(j < 15){
                var minA = Float.POSITIVE_INFINITY
                var maxA = Float.NEGATIVE_INFINITY
                var minB = Float.POSITIVE_INFINITY
                var maxB = Float.NEGATIVE_INFINITY
                var i : Int = 0
                while(i < 8){
                    var fA = vectors[j].dot(cube1.myVerticies[i])
                    var fB = vectors[j].dot(cube2.myVerticies[i])

                    if (fA <= minA)
                        minA = fA

                    if(fA >= maxA)
                        maxA = fA

                    if (fB <= minB)
                        minB =fB

                    if(fB >= maxB)
                        maxB = fB

                    i++
                }
                if ((minA < minB && maxA < minB) || (minA > maxB && maxA > maxB))
                    return false
                j++
            }
            return true
        }
    }
}



