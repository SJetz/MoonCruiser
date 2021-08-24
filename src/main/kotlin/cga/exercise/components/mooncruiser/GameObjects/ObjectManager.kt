package cga.exercise.components.mooncruiser.GameObjects

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Renderable
import cga.framework.GameWindow

class ObjectManager {

    private val listOfGameObjects = mutableListOf<Renderable>()
    private val listToRemove = mutableListOf<Renderable>()

    fun addObject (renderableObject: Renderable){
        if(renderableObject == null){
            return
        }
        listOfGameObjects.add(renderableObject)
    }

    fun removeObject (renderableObject: Renderable){
        if (renderableObject == null){
            return
        }
        listToRemove.add(renderableObject)
    }

    fun update(dt: Float,window: GameWindow){
       for (i in listOfGameObjects){
           i.update(dt,window)
       }
        for (i in listToRemove){
            listOfGameObjects.remove(i)
        }
        listToRemove.clear()
    }

    fun render (dt: Float, t: Float){
        for (i in listOfGameObjects){
            i.render(dt, t)
        }
    }

    fun reset (){
        listOfGameObjects.clear()
        listToRemove.clear()
    }

    //updated Renderables
    fun setCamera (camera: TronCamera){
        for(n in listOfGameObjects){
            n.myCamera = camera
        }
    }
}