package cga.exercise.components.mooncruiser

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

    fun render (){
        for (i in listOfGameObjects){
            i.render()
        }
    }
}