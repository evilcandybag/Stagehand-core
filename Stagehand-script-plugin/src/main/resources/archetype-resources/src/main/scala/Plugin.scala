package ${groupId}

import se.stagehand.plugins._
import se.stagehand.lib.scripting._

class ${artifactId}Plugin extends ScriptPlugin {

  val name = "${artifactId}"
    
  val scriptcomponents: Array[ScriptComponent] = Array(new ${artifactId}Script)
  
}