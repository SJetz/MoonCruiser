package cga.exercise.components.mooncruiser.GameObjects

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.texture.Texture2D
import cga.framework.GameWindow
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.lwjgl.opengl.GL11

class Ground : Renderable(){



    override fun update(dt: Float, window: GameWindow) {}

    override fun init(camera: TronCamera) {
        //load textures
        val groundDiff = Texture2D("assets/textures/ground_diff.png", true)
        groundDiff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        val groundSpecular = Texture2D("assets/textures/ground_spec.png", true)
        groundSpecular.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        val groundEmit = Texture2D("assets/textures/ground_emit.png", true)
        groundEmit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
        var groundMaterial = Material(groundDiff, groundEmit, groundSpecular, 60f, Vector2f(64.0f, 64.0f))

        //load an object and create a mesh
        val gres = OBJLoader.loadOBJ("assets/models/ground.obj")

        //Create the mesh
        val stride = 8 * 4
        val atr1 = VertexAttribute(3, GL11.GL_FLOAT, stride, 0) //position attribute
        val atr2 = VertexAttribute(2, GL11.GL_FLOAT, stride, 3 * 4) //texture coordinate attribut
        val atr3 = VertexAttribute(3, GL11.GL_FLOAT, stride, 5 * 4) //normal attribute
        val vertexAttributes = arrayOf(atr1, atr2, atr3)

        //Create renderable
        for (m in gres.objects[0].meshes) {
            val mesh = Mesh(m.vertexData, m.indexData, vertexAttributes, groundMaterial)
            this.myMeshes.add(mesh)
        }
    }
}