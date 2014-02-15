package se.stagehand.swing.lib

import se.stagehand.lib.scripting._
import scala.swing._
import se.stagehand.swing.gui._
import se.stagehand.plugins._
import se.stagehand.swing.player.PlayerScriptNode
import se.stagehand.swing.player.PlayerGUIElement

/**
 * Wrapper trait for adding GUI to ScriptComponents. Have an object implement this
 * trait to enable automatic wrapping of your ScriptComponents.
 */
trait ScriptGUI extends ComponentGUI {
  
  /**
   * Verification method that ought to be called at the start of any implementation of
   * menuItem, editorNode or playerNode, to ensure that the supplied script is of the right type.
   */
  protected def checkScript(script: ScriptComponent) {
    if (script.getClass != peer)
      throw new IllegalArgumentException("Invalid script type: " + script.getClass.getName + ", expected: " + peer.getName)
  }
  
  def register {
    GUIManager.register(peer, this)
  }
  
  def menuItem(script: ScriptComponent): Button
  def editorNode(script: ScriptComponent): EditorScriptNode[_]
  def playerNode(script: ScriptComponent): PlayerGUIElement[ScriptComponent]
  
}