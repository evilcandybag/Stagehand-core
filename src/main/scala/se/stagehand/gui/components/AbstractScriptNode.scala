package se.stagehand.gui.components

import se.stagehand.gui._
import se.stagehand.lib.scripting._
import scala.swing._
import scala.swing.BorderPanel.Position
import scala.swing.event._
import java.awt.Color
import scala.collection.immutable.ListSet
import java.awt.BasicStroke
import java.awt.geom.Line2D

/**
 * Class for defining nodes in the editor graph for a script. 
 */
abstract class AbstractScriptNode(script: ScriptComponent) extends EditorNode(script) {

  val title: Label = new Label(displayName) {
    this.listenTo(mouse.clicks)
    this.reactions += {
      case e:MouseClicked => {
        val s = javax.swing.JOptionPane.showInputDialog(this.peer, "Enter new name", displayName)
        title.text = if (s == null) displayName else s
      }
    }
  }
  
  val pan = new BorderPanel {
    border = Swing.CompoundBorder(Swing.EtchedBorder(Swing.Raised),Swing.EtchedBorder(Swing.Raised))
    opaque = false
    
    layout(title) = Position.North
    reactions += {
      case e:MouseEvent => publish(e)
    }
  }
  layout(pan) = Position.Center
  listenTo(pan)
  
  
  def displayName:String = script.displayName
  def displayName_=(s:String) {
    try {
      script.displayName = s
    } catch {
      case e:IllegalArgumentException => Dialog.showMessage(this, e.getMessage(), "Invalid Name", Dialog.Message.Warning)
    }
    title.text = script.displayName
  }
  
  
}

trait InputGUI extends AbstractScriptNode {
  val inCon = new InputConnector(script.asInstanceOf[ScriptComponent with Input])
  private val _pan = new FlowPanel {
    contents += inCon
  }
  layout(_pan) = Position.North
}

trait OutputGUI extends AbstractScriptNode {
  val outCon = new OutputConnector(script.asInstanceOf[ScriptComponent with Output])
  private val _pan = new FlowPanel {
    contents += outCon
  }
  layout(_pan) = Position.South
}

abstract class ConnectorButton[T <: ConnectorButton[_]] extends Button {
  def middle = new Point(location.x + (bounds.width/2),location.y + (bounds.height/2))
  preferredSize = new Dimension(15,15)

  
  val me = this
  
  protected def disact(c: T)
  protected def conact(c: T)
  
  protected var connections: Set[T] = ListSet()
  def connect(c: T) {
    if (!connected(c)) {
      connections += c
      conact(c)
      ConnectorButton.active = None
      App.glass.linkOrUnlink(this.peer, c.peer)
    }
  }
  def disconnect(c: T) {
    if(connected(c)) {
      connections -= c
      disact(c)
      ConnectorButton.active = None
      App.glass.linkOrUnlink(this.peer, c.peer)
    }
  }
  def connected(c: T) = connections.contains(c) 
  
  override def paint(g:Graphics2D) {
    super.paint(g)
    App.glass.repaint()
  }
  
}

class OutputConnector(val script: ScriptComponent with Output) extends ConnectorButton[InputConnector] {
  background = Color.red
  
  def conact(ic: InputConnector) {
    ic.connect(this)
    script.connectOut(ic.script)
    
    println("connected " + ic.script.id)
    println("connections: " + connections.map(_.script.id))
    
  }
  def disact(ic: InputConnector) {
    ic.disconnect(this)
    script.disconnectOut(ic.script)
    
    println("disconnected " + ic.script.id)
    println("connections: " + connections.map(_.script.id))
  }
  
  action = new Action("") {
    def apply {
      val a = ConnectorButton.active
      if (a.isDefined) {
        if (a.get.isInstanceOf[InputConnector]) {
          val ic = a.get.asInstanceOf[InputConnector]
          if (connected(ic)) disconnect(ic) else connect(ic)
        }
      } else {
        ConnectorButton.active = Some(me)
      }
    }
  }
}

class InputConnector(val script: ScriptComponent with Input) extends ConnectorButton[OutputConnector] {
  background = Color.green

  def conact(oc: OutputConnector) { oc.connect(this) }
  def disact(oc: OutputConnector) { oc.disconnect(this) }
  
  action = new Action("") {
    def apply {
      val a = ConnectorButton.active
      if (a.isDefined) {
        if(a.get.isInstanceOf[OutputConnector]) {
          val oc = a.get.asInstanceOf[OutputConnector]
          if (connected(oc)) disconnect(oc) else connect(oc)
        }
      } else {
        ConnectorButton.active = Some(me)
      }
    }
  }
}

object ConnectorButton {
  var active:Option[ConnectorButton[_]] = None
}