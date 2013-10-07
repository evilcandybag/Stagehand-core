package se.stagehand.plugins

import se.stagehand.lib.scripting._


trait EffectPlugin {
  
  val name:String
  def getEffects(): List[Effect]
  
}