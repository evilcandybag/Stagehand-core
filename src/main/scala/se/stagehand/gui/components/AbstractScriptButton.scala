package se.stagehand.gui.components

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button

/**
 * Class for defining the button shown as a menu item for scripts.
 */
abstract class AbstractScriptButton(peer: ScriptComponent) extends Button {
  
  type peertype
  
  text = peer.displayName
}