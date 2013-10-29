package se.stagehand.lib.scripting

import scala.xml._

/**
 * Common supertrait for all Stagehand components. Contains methods for handling instructions. 
 */
abstract class StagehandComponent(ident: Int) {
  def this() = this(ID.unique)
  
  val id = ident
  ID.add(id, this)
  
  val displayName:String
  
  /**
   * Generate instruction set for the plugin. Used inside the editor to generate 
   * instructions to be read by the player. Should look like this:
   * <effect or script>
   * 	<class name>
   *  		<various properties/>
   *  	<class name>
   * </effect or script>
   */
  def generateInstructions: Elem
  
  /**
   * Load instructions for the player. 
   */
  def readInstructions(in: Elem)
  
  /**
   * Execute the given instructions over the assigned targets. 
   */
  def executeInstructions: Unit
  
  
  /**
   * Helper for creating XML thingies
   */
  def addChild(newChild: Node) (implicit n: Node) = n match {
  	case Elem(prefix, label, attribs, scope, child @ _*) =>
  	  Elem(prefix, label, attribs, scope, child ++ newChild : _*)
  	case _ => error("Can only add children to elements!")
  }
  def idXML: Elem = <id>{id}</id>
  
}