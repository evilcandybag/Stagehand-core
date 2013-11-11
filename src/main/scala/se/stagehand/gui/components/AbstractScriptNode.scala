package se.stagehand.gui.components

import se.stagehand.gui._
import se.stagehand.lib.scripting.ScriptComponent
import scala.swing._
import scala.swing.BorderPanel.Position
import scala.swing.event._

/**
 * Class for defining nodes in the editor graph for a script. 
 */
abstract class AbstractScriptNode(peer: ScriptComponent) extends EditorNode {
  val title: Label = new Label(displayName) {
    listenTo(mouse.clicks)
    reactions += {
      case e:MouseClicked => {
        val s = javax.swing.JOptionPane.showInputDialog(this.peer, "Enter new name", displayName)
        title.text = if (s == null) displayName else s
      }
    }
  }
  layout(title) = Position.North
  
  def displayName:String = peer.displayName
  def displayName_=(s:String) {
    try {
      peer.displayName = s
    } catch {
      case e:IllegalArgumentException => Dialog.showMessage(this, e.getMessage(), "Invalid Name", Dialog.Message.Warning)
    }
    title.text = peer.displayName
  }
  
  
}