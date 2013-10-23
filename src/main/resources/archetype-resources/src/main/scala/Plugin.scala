package ${groupId}

import se.stagehand.plugins._
import se.stagehand.lib.scripting.Effect

class ${artifactId}Plugin extends ScriptPlugin {

  val name = "${artifactId}"
    
  def getEffects(): Array[ScriptComponent] = Array(new ${artifactId}Script)
  
}