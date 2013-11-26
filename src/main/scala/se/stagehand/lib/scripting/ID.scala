package se.stagehand.lib.scripting

/**
 * Generates unique id:s for components
 */
object ID {
  
  private var counter:Int = 0
  private var pool: Map[Int,StagehandComponent] = Map()
  
  
  def unique:Int = {
    counter += 1
    counter
  }
  
  def add(k: Int, sc: StagehandComponent) {
    println(k + " " + sc)
    if (!pool.contains(k)) {
      pool += (k -> sc)
    } else {
      throw new IllegalArgumentException("Component ID needs to be unique!")
    }
    
  }
  def add(sc:StagehandComponent): Int = {
    val i = unique
    pool += (i -> sc)
    return i
  }
  def fetch[T <: StagehandComponent](i:Int): T = {
    val sc = pool.apply(i)
    return sc.asInstanceOf[T]
  }
  def allXML = <components>{for (s <- pool.values) yield s.generateInstructions}</components>

}