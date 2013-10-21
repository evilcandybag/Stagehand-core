package se.stagehand.plugins

import se.stagehand.lib.scripting._

/**
 * ScriptComponents define rules for how Effects are activated together.
 */
trait ScriptComponent {
  
  /**
   * The name of the Effect as shown in the editor. 
   */
  def displayName:String
  
  val editorComponent: EditorComponent
  
  val playerComponent: PlayerComponent
}