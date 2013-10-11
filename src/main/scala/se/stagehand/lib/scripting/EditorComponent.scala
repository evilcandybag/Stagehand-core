package se.stagehand.lib.scripting

import scala.swing._
import scala.xml._

/**
 * The editor part of an effect. Contains instructions on how to display and 
 * configure the effect in the editor and how to save it.  
 */
trait EditorComponent {
  /**
   * Get the GUI component for configuring this effect. 
   */
  def getEditorGUI:Panel
  
  /**
   * Generate instruction set for the plugin. Used inside the editor to generate 
   * instructions to be read by the player. 
   */
  def generateInstructions: Elem

}