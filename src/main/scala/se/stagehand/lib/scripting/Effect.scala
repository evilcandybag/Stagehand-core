package se.stagehand.lib.scripting

import scala.swing.Panel
import scala.xml._

/**
 * An effect is the smallest component in Stagehand scripting. It represents a 
 * command that can be sent to a device. 
 * Effects have an editor component where the commands are defined, and player 
 * component where commands are executed. 
 */
trait Effect extends StagehandComponent {
  
}