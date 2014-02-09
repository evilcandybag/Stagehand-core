package se.stagehand.swing.editor

import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import se.stagehand.lib.Log
import se.stagehand.lib.scripting.ScriptComponent
import java.awt.event.MouseEvent
import se.stagehand.swing.lib.GUIManager
import se.stagehand.swing.lib.ConnectorButton
import javax.swing.SwingUtilities
import java.awt.Cursor

object SelectionManager {
  private val log = Log.getLog(this.getClass)
  
  private var _selected:Selected = Nothing()
  private val toolkit = Toolkit.getDefaultToolkit()
  private type ConnectorType = ConnectorButton[_ <: ConnectorButton[_]]
  private val glass = GUIManager.glass
  
  //Attempt to catch all system mouse events. 
  private val eventMask:Long = AWTEvent.MOUSE_EVENT_MASK;
  toolkit.addAWTEventListener( new AWTEventListener() {
  	def eventDispatched(event: AWTEvent) {
  	  event match {
  	    case e:MouseEvent => {
  	      import MouseEvent.{BUTTON2, BUTTON3,MOUSE_PRESSED}
  	      e.getID() match {
  	        case MOUSE_PRESSED if SwingUtilities.isRightMouseButton(e) => {
  	          cancelSelection
  	        }
  	        case _ => {}
  	      }
  	    }
  	    case e => log.error("Catching unknown event: " + e)
  	  }
    }
  }, eventMask);
  
  /**
   * Select a script 
   */
  def selectScript(script: ScriptComponent) = {
    cancelSelection
    GUIManager.gotScript = Some(script)
    Editor.frame.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
  }
  def selectConnector(con: ConnectorType) {
    _selected match {
      case Connector(c) => {
        ConnectorButton.connectOrDisconnect(con, c)
        cancelSelection
      }
      case _=> {
        cancelSelection
        _selected = Connector(con)
        glass.selected(con.peer)
      }
    }
  }
  
  def cancelSelection {
    Editor.frame.cursor = Cursor.getDefaultCursor()
    glass.deselected()
    GUIManager.gotScript = None
    _selected = Nothing()
  }
  
  private sealed trait Selected
  private case class Script extends Selected
  private case class Connector(con: ConnectorType) extends Selected
  private case class Nothing extends Selected

  
  
  
  
}