package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.plugins._
import java.awt.event.MouseEvent.{BUTTON1,BUTTON2}
import se.stagehand.swing.gui.NullPanel
import scala.swing.event._
import se.stagehand.swing.lib._
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.Log

class EditorPanel extends NullPanel {
    private val log = Log.getLog(this.getClass())
	preferredSize = new Dimension(500,500)
	border = Swing.EtchedBorder(Swing.Raised)
	listenTo(mouse.clicks)	
	
	reactions += {
	  case MouseClicked(_,_,_,c,_) if c == BUTTON2 => {
	    GUIManager.gotScript = None
	    ConnectorButton.active = None
	  }
	  case e:MouseClicked if GUIManager.gotScript.isDefined => {
	    
	    val script = ID.newInstance(GUIManager.gotScript.get)
	    val gui = GUIManager.editorNode(script)
//	    val gui = new Label("" + e.point)
	    log.debug("EP " + e.point.toString() + " " + gui)
	
	    add(gui,e.point)
	    GUIManager.register(gui)
	    GUIManager.gotScript = None
	    
	    gui.revalidate
	    gui.repaint
	    
	    repaint
	    revalidate
	  }
	  case e:MouseClicked if ConnectorButton.active.isDefined => {
	    ConnectorButton.active = None
	  }
	}
}