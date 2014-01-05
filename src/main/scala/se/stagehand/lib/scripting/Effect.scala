package se.stagehand.lib.scripting

import scala.swing.Panel
import scala.xml._
import scala.collection.immutable.ListSet

/**
 * An effect is the smallest component in Stagehand scripting. It represents a 
 * command that can be sent to a device. 
 * Effects have an editor component where the commands are defined, and player 
 * component where commands are executed. 
 */
abstract class Effect(id:Int) extends StagehandComponent(id) {
  def this() = this(ID.unique)
  
  protected val kind = "effect"
  
  /**
   * Trigger this effect 
   */
  def trigger: Unit
  
  def generateInstructions = 
    <effect class={this.getClass.getName}>{idXML}</effect>
}
