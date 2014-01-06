package ${groupId}

import se.stagehand.gui.components._

object ${artifactId}GUI extends ScriptGUI[${artifactId}] {
  def menuItem(script: ScriptComponent) = {
    checkScript(script)
    new ${artifactId}Button(script.asInstanceOf[${artifactId}])
  }
  def editorNode(script: ScriptComponent) = {
    checkScript(script)
    new ${artifactId}Node(script.asInstanceOf[${artifactId}])
  }
  
  def playerNode(script: ScriptComponent) = {
    checkScript(script)
    new ${artifactId}PlayerNode(script.asInstanceOf[${artifactId}])

  }
}

class ${artifactId}Button(script: ${artifactId}) extends AbstractScriptButton[${artifactId}](script) {
  
}

class ${artifactId}Node(script: ${artifactId}) extends EditorScriptNode[${artifactId}](script) {
  
}
class ${artifactId}PlayerNode(script: ${artifactId) extends PlayerScriptNode[${artifactId}](script) {
}