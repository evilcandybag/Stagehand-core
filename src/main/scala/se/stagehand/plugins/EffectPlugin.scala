package se.stagehand.plugins

import se.stagehand.lib.scripting._


trait EffectPlugin {
  
  /**
   * The name of the Plugin. This is used as an identifier when loading.
   */
  val name:String
  /**
   * Get the effects defined by this plugin. 
   */
  def getEffects(): Array[Effect]
  
}