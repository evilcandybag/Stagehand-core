package se.stagehand.lib.scripting

import scala.xml.Elem

/**
 * Common supertrait for all Stagehand components. Contains methods for handling instructions. 
 */
trait StagehandComponent {
  
  val displayName:String
  
  /**
   * Generate instruction set for the plugin. Used inside the editor to generate 
   * instructions to be read by the player. 
   */
  def generateInstructions: Elem
  
  /**
   * Load instructions for the player. 
   */
  def readInstructions(in: Elem)
  
  /**
   * Execute the given instructions over the assigned targets. TODO: How to define
   * targets for the effect. 
   */
  def executeInstructions: Unit
}