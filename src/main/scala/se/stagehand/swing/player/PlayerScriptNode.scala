package se.stagehand.swing.player

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.BorderPanel
import se.stagehand.swing.lib.ScriptNode
import scala.swing._
import java.awt.Color
import scala.swing.event.MouseClicked
import java.awt.event.MouseEvent.{BUTTON1}


abstract class PlayerScriptNode[T <: ScriptComponent](sc:T) extends BorderPanel with ScriptNode[T] {
  private val _script: T = sc
  def script = _script
  
  border = Swing.CompoundBorder(Swing.EtchedBorder(Swing.Raised),Swing.EtchedBorder(Swing.Raised))

  def title:String
  
  layout(new Label(title)) = BorderPanel.Position.North
  listenTo(mouse.clicks)
  reactions += {
    case e:MouseClicked if e.peer.getButton == BUTTON1 => {
      script.executeInstructions()
    }
  }
  
  revalidate
  repaint
  peer.setSize(preferredSize)
}