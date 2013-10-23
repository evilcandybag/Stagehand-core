package se.stagehand.plugins

import se.stagehand.lib.scripting.ScriptComponent

/**
 * Trait for plugins containing scripting components.
 */
trait ScriptPlugin extends Plugin {
  
  def scriptcomponents: Array[ScriptComponent]

}