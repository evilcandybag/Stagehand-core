package se.stagehand.swing.player

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.BorderPanel
import se.stagehand.swing.lib.ScriptNode
import scala.swing._
import java.awt.Color

abstract class PlayerScriptNode[T <: ScriptComponent](sc:T) extends BorderPanel with ScriptNode[T] {
  private val _script: T = sc
  def script = _script
  
  border = Swing.CompoundBorder(Swing.EtchedBorder(Swing.Raised),Swing.EtchedBorder(Swing.Raised))

  def title:String
  
  layout(new Label(title)) = BorderPanel.Position.North
  
  revalidate
  repaint
  peer.setSize(preferredSize)
}