package se.stagehand.plugins

import se.stagehand.lib.scripting._


trait EffectPlugin extends Plugin {
  
  /**
   * Get the effects defined by this plugin. 
   */
  def getEffects(): Array[Effect]
  
}