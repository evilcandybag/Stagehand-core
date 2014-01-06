package ${packageId}

import se.stagehand.plugins.EffectPlugin
import se.stagehand.lib.scripting.Effect

class ${artifactId}Plugin extends EffectPlugin {

  val name = "${artifactId}"
    
  def getEffects(): Array[Effect] = Array(new ${artifactId}Effect)
  
}