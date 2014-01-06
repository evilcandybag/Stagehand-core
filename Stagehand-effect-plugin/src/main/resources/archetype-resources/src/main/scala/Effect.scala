package ${packageId}

import se.stagehand.lib.scripting._
import scala.xml.Elem
import scala.swing._

class ${artifactId}Effect extends Effect {
  val displayName = "${artifactId}"
    
  val editorComponent = new ${artifactId}Editor
  val playerComponent = new ${artifactId}Player

}

class ${artifactId}Editor extends EditorComponent {
  def generateInstructions = null
  def getEditorGUI = null
}

class ${artifactId}Player extends PlayerComponent {
  def executeInstructions {}
  def readInstructions(in: Elem) {}
}