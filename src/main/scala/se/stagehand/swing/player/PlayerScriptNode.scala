package se.stagehand.swing.player

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.BorderPanel
import se.stagehand.swing.lib.ScriptNode

abstract class PlayerScriptNode[T <: ScriptComponent](sc:T) extends BorderPanel with ScriptNode[T] {
  private val _script: T = sc
  def script = _script

}