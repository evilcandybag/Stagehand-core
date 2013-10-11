package se.stagehand.lib.scripting

import scala.swing.Panel
import scala.xml._

/**
 * An effect is the smallest component in Stagehand scripting. It represents a 
 * command that can be sent to a device. 
 * Effects have an editor component where the commands are defined, and player 
 * component where commands are executed. 
 */
trait Effect {
  /**
   * The name of the Effect as shown in the editor. 
   */
  def displayName:String
  
  val editorComponent: EditorComponent
  
  val playerComponent: PlayerComponent
}