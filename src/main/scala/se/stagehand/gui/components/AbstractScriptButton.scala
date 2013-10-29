package se.stagehand.gui.components

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button

/**
 * Class for defining the button shown as a menu item for scripts.
 */
abstract class AbstractScriptButton[T <: ScriptComponent](peer: T) extends Button {
  text = peer.displayName
}