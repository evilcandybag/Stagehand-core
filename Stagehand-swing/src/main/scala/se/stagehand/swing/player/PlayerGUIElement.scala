package se.stagehand.swing.player

import se.stagehand.lib.scripting.ScriptComponent
import se.stagehand.swing.lib.ComponentNode
import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.BorderPanel
import se.stagehand.swing.lib.ScriptNode
import scala.swing._
import java.awt.Color
import scala.swing.event.MouseClicked
import java.awt.event.MouseEvent.{BUTTON1}
import se.stagehand.lib.scripting.Output

/**
 * Trait for representing any of the two PlayerScriptNode or PlayerScriptInfix
 */
trait PlayerGUIElement[+T <: ScriptComponent] extends ScriptNode[T] {
 
}

/**
 * Infixes appear not as independent nodes, but as infixes in connected nodes.
 */
abstract class PlayerScriptInfix[T <: ScriptComponent with Output](sc:T) extends BoxPanel(Orientation.Vertical) with PlayerGUIElement[T] {
  private val _script: T = sc
  def script = _script
  
  def infix:Component
  contents += infix
}

abstract class PlayerScriptNode[T <: ScriptComponent](sc:T) extends BorderPanel with ScriptNode[T] with PlayerGUIElement[T] {
  private val _script: T = sc
  def script = _script
  val infixes = new ContentPanel("infixes")
  
  border = Swing.CompoundBorder(Swing.EtchedBorder(Swing.Raised),Swing.EtchedBorder(Swing.Raised))

  def title:String
  
  
  layout(infixes) = BorderPanel.Position.South
  layout(new Label(title)) = BorderPanel.Position.North
  
  listenTo(mouse.clicks)
  reactions += {
    case e:MouseClicked if e.peer.getButton == BUTTON1 => {
      script.executeInstructions()
    }
  }
  
  revalidate
  repaint
  
  
  def addInfix(infix:PlayerScriptInfix[_]) {
    infixes.contents += infix
    revalidate
    repaint
  }
  
  peer.setSize(preferredSize)
  
}
