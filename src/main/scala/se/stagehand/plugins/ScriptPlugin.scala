package se.stagehand.plugins

/**
 * Trait for plugins containing scripting components.
 */
trait ScriptPlugin {
  
  def scriptcomponents: Array[ScriptComponent]

}