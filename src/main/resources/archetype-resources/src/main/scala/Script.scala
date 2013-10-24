package ${groupId}

import se.stagehand.lib.scripting._
import scala.xml.Elem
import scala.swing._

class ${artifactId}Script extends ScriptComponent {
  val displayName = "${artifactId}"
    
  def executeInstructions {}
  def readInstructions(in: Elem) {}
  def generateInstructions = null

}
