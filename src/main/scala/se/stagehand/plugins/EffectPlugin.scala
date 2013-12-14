package se.stagehand.plugins

import se.stagehand.lib.scripting._


trait EffectPlugin extends Plugin {
  
  /**
   * Get the effects defined by this plugin. 
   */
  val effects: Array[Effect]
  
}