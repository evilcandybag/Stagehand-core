package se.stagehand.gui.components

import se.stagehand.gui.EditorNode
import se.stagehand.lib.scripting.ScriptComponent
import scala.swing._

/**
 * Class for defining nodes in the editor graph for a script. 
 */
abstract class AbstractScriptNode[T <: ScriptComponent](peer: T) extends Panel {

}