package se.stagehand.gui.components

import se.stagehand.lib.scripting._
import scala.swing._
import se.stagehand.gui._
import se.stagehand.plugins._

/**
 * Wrapper trait for adding GUI to ScriptComponents. Have an object implement this
 * trait to enable automatic wrapping of your ScriptComponents.
 */
trait ScriptGUI[T <: ScriptComponent] {
  
  implicit def menuItem(script: T): AbstractScriptButton[T]
  implicit def editorNode(script: T): AbstractScriptNode[T]
  
}