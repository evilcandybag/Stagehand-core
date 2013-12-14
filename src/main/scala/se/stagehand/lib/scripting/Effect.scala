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
abstract class Effect extends StagehandComponent {
  
  /**
   * Trigger this effect 
   */
  def trigger: Unit
  
  
}