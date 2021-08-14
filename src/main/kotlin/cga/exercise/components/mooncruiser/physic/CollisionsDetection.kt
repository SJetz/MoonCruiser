package cga.exercise.components.mooncruiser.physic

import cga.exercise.components.geometry.Renderable
import org.joml.Vector3f

class CollisionsDetection {

    companion object Foo {

        var defaultVector1 : Vector3f = Vector3f()
        var defaultVector2 : Vector3f = Vector3f()
        var defaultVector3 : Vector3f = Vector3f()
        var defaultVector4 : Vector3f = Vector3f()
        var defaultVector5 : Vector3f = Vector3f()
        var defaultVector6 : Vector3f = Vector3f()

        var defaultVector_1 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_2 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_3 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_4 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_5 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_6 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_7 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_8 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_9 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_10 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_11 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_12 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_13 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_14 : Vector3f = Vector3f(0f,0f,0f)
        var defaultVector_15 : Vector3f = Vector3f(0f,0f,0f)


        fun CubeinCube(cube1 : Cube, cube2 : Cube): Boolean {
            val a1 = (defaultVector1.add(cube1.myVertices[0]).sub(cube1.myVertices[1]))
            val a2 = (defaultVector2.add(cube1.myVertices[0]).sub(cube1.myVertices[3]))
            val a3 = (defaultVector3.add(cube1.myVertices[0]).sub(cube1.myVertices[4]))

            val b1 = (defaultVector4.add(cube2.myVertices[0]).sub(cube2.myVertices[1]))
            val b2 = (defaultVector5.add(cube2.myVertices[0]).sub(cube2.myVertices[3]))
            val b3 = (defaultVector6.add(cube2.myVertices[0]).sub(cube2.myVertices[4]))

            var vectors :ArrayList<Vector3f> = arrayListOf(
                defaultVector_1.add(a1).cross(a2).normalize(),
                defaultVector_2.add(a1).cross(a3).normalize(),
                defaultVector_3.add(a2).cross(a3).normalize(),

                defaultVector_4.add(b1).cross(b2).normalize(),
                defaultVector_5.add(b1).cross(b3).normalize(),
                defaultVector_6.add(b2).cross(b3).normalize(),

                defaultVector_7.add(a1).cross(b1).normalize(),
                defaultVector_8.add(a1).cross(b2).normalize(),
                defaultVector_9.add(a1).cross(b3).normalize(),

                defaultVector_10.add(a2).cross(b1).normalize(),
                defaultVector_11.add(a2).cross(b2).normalize(),
                defaultVector_12.add(a2).cross(b3).normalize(),

                defaultVector_13.add(a3).cross(b1).normalize(),
                defaultVector_14.add(a3).cross(b2).normalize(),
                defaultVector_15.add(a3).cross(b3).normalize()
            )

            var j = 0
            while(j < 15){
                var minA = Float.POSITIVE_INFINITY
                var maxA = Float.NEGATIVE_INFINITY
                var minB = Float.POSITIVE_INFINITY
                var maxB = Float.NEGATIVE_INFINITY
                var i  = 0
                while(i < 8){
                    var defaultVectorfA : Vector3f = Vector3f(0f,0f,0f)
                    var defaultVectorfB : Vector3f = Vector3f(0f,0f,0f)

                    var fA = defaultVectorfA.add(vectors[j]).dot(cube1.myVertices[i])
                    var fB = defaultVectorfB.add(vectors[j]).dot(cube2.myVertices[i])

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



