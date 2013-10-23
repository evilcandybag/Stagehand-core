package se.stagehand.gui.components

import se.stagehand.lib.scripting._
import scala.swing._
import se.stagehand.gui._
import se.stagehand.plugins._

object ScriptGUI {
  implicit def menuItem(s:ScriptComponent): Button = {
    new Button() {
      text = s.displayName
    }
  }
  implicit def menuList(l: Array[ScriptPlugin]): Array[Button] = {
    (l.map(_.scriptcomponents.map(menuItem))).flatten
  }
  implicit def editorNode(s:ScriptComponent): EditorNode = {
    null
  }
}