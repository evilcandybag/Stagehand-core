package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet

/**
 * Generates unique id:s for components
 */
object ID {
  
  private var counter:Int = 0
  private var pool: Map[Int,StagehandComponent] = Map()
  private var prototypes: Map[Class[_],String] = Map()
  
  def unique:Int = {
    counter += 1
    counter
  }
  /**
   * Clear all instances stored in  the pool and reset the unique ID counter.
   * Classes stay loaded so that they can be instantiated by name.
   */
  def clearInstances {
    pool = Map()
    counter = 0
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
      prototypes += (sc.getClass -> sc.componentName)
    }
    pool += (i -> sc)
  }
  def newInstance[T <: StagehandComponent](cname:String, id: Int):T = {
    val mp = prototypes.find(x => x._1.getName == cname)
    if (mp.isDefined) {
      val p = mp.get._1
      return this.newInstance(p,id).asInstanceOf[T]
    } else {
      throw new IllegalArgumentException("class not found " + cname)
    }
  }
  
  def newInstance[T <: StagehandComponent](cname:String): T = {
    newInstance[T](cname,-1)
  }
  
  /**
   * Helper to either construct an object with a new unique id or with a supplied id.
   * @param c - class to instantiate.
   * @param id - new id. If id < 0, generate a unique ID. 
   */
  private def newInstance(c: Class[_], id:Int) = {
    if (id < 0) {
      c.newInstance
    } else {
      val cons = c.getConstructors
      val con = cons.find(_.getParameterTypes.length == 1)
      if (con.isDefined) {
        con.get.newInstance(id.asInstanceOf[java.lang.Object])
      } else {
        throw new IllegalStateException("This shit unreachable, yo")
      }
    }
  }
  
  def newInstance[T <: StagehandComponent](c: Class[_]):T = {
    newInstance[T](c.getName)
  }
  
  def newInstance[T <: StagehandComponent](sc: T):T = {
    newInstance[T](sc.getClass)
  }
  
  /**
   * Get all subclasses of this class, from all StagehandComponents loaded into the program.
   * They come in pairs with their componentName.
   */
  def getAll(c: Class[_]):Seq[Class[_]] = {
    getAllWithNames(c).map(_._1)
  }
  def getAllWithNames(c: Class[_]):Seq[(Class[_],String)] = {
    prototypes.filter(x => c.isAssignableFrom(x._1)).toSeq
  }
  def getName(c: Class[_]):Option[String] = prototypes.get(c)
  
  def fetch[T <: StagehandComponent](i:Int): T = {
    val sc = pool.apply(i)
    return sc.asInstanceOf[T]
  }
  def allXML = <components>{for (s <- pool.values) yield s.generateInstructions}</components>

}