package se.stagehand.gui.components

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button
import scala.swing.event.MouseClicked

/**
 * Class for defining the button shown as a menu item for scripts.
 */
abstract class AbstractScriptButton(peer: ScriptComponent) extends Button {
  
  type peertype
  text = peer.componentName
  listenTo(mouse.clicks)
  
  reactions += {
    case e:MouseClicked => GUIManager.gotScript = Some(peer)
  }
  
}