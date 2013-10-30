package ${groupId}

import se.stagehand.gui.components._

object ${artifactId}GUI extends ScriptGUI[${artifactId}] {
  implicit def menuItem(script: ${artifactId}) = {
    new ${artifactId}Button(script)
  }
  implicit def editorNode(script: ${artifactId}) = {
    new ${artifactId}Node(script)
  }
}

class ${artifactId}Button(peer: ${artifactId}) extends AbstractScriptButton[${artifactId}](peer) {
  
}

class ${artifactId}Node(peer: ${artifactId}) extends AbstractScriptNode[${artifactId}](peer) {
  
}