package se.stagehand.swing.lib

import se.stagehand.lib.scripting._
import scala.swing._
import scala.swing.BorderPanel.Position
import scala.swing.event._
import java.awt.Color
import scala.collection.immutable.ListSet
import java.awt.BasicStroke
import java.awt.geom.Line2D
import se.stagehand.swing.gui.Movable
import scala.xml.Elem
import se.stagehand.swing.gui.Resizable
import se.stagehand.lib.Log
import se.stagehand.swing.editor.SelectionManager
import javax.swing.SwingUtilities
import se.stagehand.swing.gui.PopupMenu
import java.awt.MouseInfo
import se.stagehand.swing.editor.Editor


/**
 * Class for defining nodes in the editor graph for a script. 
 */
abstract class EditorScriptNode[T <: ScriptComponent](sc: T) extends BorderPanel with Movable with Resizable with ScriptNode[T] {
  protected val log = Log.getLog(this.getClass())
  listenTo(mouse.clicks)
  listenTo(mouse.moves)
  visible = true
  
  opaque = false
 
  private val _script = sc
  def script = _script
  
  protected def me = this
  
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
    opaque = true
    
    layout(title) = Position.North
    reactions += {
      case e:MouseEvent => publish(e)
    }
  }
  layout(pan) = Position.Center
  listenTo(pan)
  listenTo(mouse.clicks)
  
  //Add right click context menu
  reactions += {
    case e: MouseClicked if SwingUtilities.isRightMouseButton(e.peer)  => {
      var cursor = MouseInfo.getPointerInfo.getLocation
      SwingUtilities.convertPointFromScreen(cursor, this.peer)
      contextMenu.show(this, cursor.x, cursor.y)
    }
  }
  
  private val contextMenu = new PopupMenu {
    contents += new MenuItem(new Action("Delete") {
      def apply {
        me.remove
        Editor.panel.remove(me)
      }
    })
  }
  
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

trait InputGUI[T <: ScriptComponent with Input] extends EditorScriptNode[T] {
  val inCon = new InputConnector(script)
  private val _pan = new FlowPanel {
    contents += inCon
    opaque = false
    vGap = 1
  }
  layout(_pan) = Position.North
  
  override def remove {
    inCon.connections.foreach(x => {
      x.disconnect(inCon)
    })
    super.remove
  }
}

trait OutputGUI[T <: ScriptComponent with Output] extends EditorScriptNode[T] {
  val outCon = new OutputConnector(script)
  private val _pan = new FlowPanel {
    contents += outCon
    opaque = false
    vGap = 1
  }
  layout(_pan) = Position.South
  
  override def remove {
    outCon.connections.foreach(x => {
      x.disconnect(outCon)
    })
    super.remove
  }
}

abstract class ConnectorButton[T <: ConnectorButton[_]] extends Button {
  def middle = new Point(location.x + (bounds.width/2),location.y + (bounds.height/2))
  preferredSize = new Dimension(12,12)

  
  val me = this
  
  protected def disact(c: T)
  protected def conact(c: T)
  
  protected var _connections: Set[T] = ListSet()
  def connections = _connections
  
  def connect(c: T) {
    if (!connected(c)) {
      _connections += c
      conact(c)
      GUIManager.glass.link(this.peer, c.peer)
    }
  }
  def disconnect(c: T) {
    if(connected(c)) {
      _connections -= c
      disact(c)
      GUIManager.glass.unlink(this.peer, c.peer)
    }
  }
  def connected(c: T) = _connections.contains(c) 
  
  override def paint(g:Graphics2D) {
    super.paint(g)
    GUIManager.glass.repaint()
  }
  
  action = new Action("") {
    def apply {
      SelectionManager.selectConnector(me)
    }
  }
  
}

class OutputConnector(val script: ScriptComponent with Output) extends ConnectorButton[InputConnector] {
  private val log = Log.getLog(this.getClass())
  background = Color.red
  
  def conact(ic: InputConnector) {
    ic.connect(this)
    script.connectOut(ic.script)
    
    log.debug("connected " + ic.script.id)
    log.debug("connections: " + _connections.map(_.script.id))
    
  }
  def disact(ic: InputConnector) {
    ic.disconnect(this)
    script.disconnectOut(ic.script)
    
    log.debug("disconnected " + ic.script.id)
    log.debug("connections: " + _connections.map(_.script.id))
  }
}

class InputConnector(val script: ScriptComponent with Input) extends ConnectorButton[OutputConnector] {
  background = Color.green

  def conact(oc: OutputConnector) { oc.connect(this) }
  def disact(oc: OutputConnector) { oc.disconnect(this) }
}

object ConnectorButton {
  private lazy val ex = new IllegalArgumentException("Connectors need to be of different types!")
  def connectOrDisconnect(c1: ConnectorButton[_], c2: ConnectorButton[_]) {
    c1 match {
      case ic: InputConnector => c2 match {
        case oc: OutputConnector => if (ic.connected(oc)) ic.disconnect(oc) else ic.connect(oc)
        case _ => throw ex
      }
      case oc: OutputConnector => c2 match {
        case ic: InputConnector => if (oc.connected(ic)) oc.disconnect(ic) else oc.connect(ic)
        case _ => throw ex
      }
    }
  }
}