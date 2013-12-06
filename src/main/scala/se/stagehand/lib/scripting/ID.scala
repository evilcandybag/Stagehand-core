package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet

/**
 * Generates unique id:s for components
 */
object ID {
  
  private var counter:Int = 0
  private var pool: Map[Int,StagehandComponent] = Map()
  private var prototypes: Set[Class[_]] = ListSet()
  
  def unique:Int = {
    counter += 1
    counter
  }
  
  def add(k: Int, sc: StagehandComponent) {
    if (!pool.contains(k)) {
      poolAdd(k,sc)
    } else {
      throw new IllegalArgumentException("Component ID needs to be unique!")
    }
    
  }
  def add(sc:StagehandComponent): Int = {
    val i = unique
    poolAdd(i, sc)
    return i
  }
  private def poolAdd(i:Int, sc:StagehandComponent) {
    //if I cannot find a prototype of the same class, add one.
    //in practice, the first component that is created is the prototype
    val p = prototypes.contains(sc.getClass)
    if (!p) {
      prototypes += sc.getClass
    }
    pool += (i -> sc)
  }
  def newInstance[T <: StagehandComponent](name:String):T = {
    val p = prototypes.find(x => x.getName == name)
    if (p.isDefined) {
      return p.get.newInstance().asInstanceOf[T]
    } else {
      throw new IllegalArgumentException("class not found")
    }
  }
  
  def newInstance[T <: StagehandComponent](c: Class[_]):T = {
    newInstance[T](c.getName)
  }
  
  def newInstance[T <: StagehandComponent](sc: T):T = {
    newInstance[T](sc.getClass)
  }
  
  def fetch[T <: StagehandComponent](i:Int): T = {
    val sc = pool.apply(i)
    return sc.asInstanceOf[T]
  }
  def allXML = <components>{for (s <- pool.values) yield s.generateInstructions}</components>

}