package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button
import scala.swing.event.MouseClicked
import scala.swing.Action
import se.stagehand.swing.editor.SelectionManager

/**
 * Class for defining the button shown as a menu item for scripts.
 */
abstract class EditorScriptButton(val script: ScriptComponent) extends Button {
  
  type peertype
  text = script.componentName
  peer.setToolTipText(script.description)
  
  
  action = new Action(script.componentName) {
    def apply = {
      SelectionManager.selectScript(script)
    }
  }
}