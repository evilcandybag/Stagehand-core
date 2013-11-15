package se.stagehand.gui

import scala.swing._
import scala.swing.event.MouseClicked
import java.awt.event.MouseEvent.{BUTTON1,BUTTON2}
import se.stagehand.gui.components.GUIManager
import se.stagehand.plugins._
import se.stagehand.gui.components.ConnectorButton

class EditorPanel extends NullPanel {
	preferredSize = new Dimension(500,500)
	border = Swing.EtchedBorder(Swing.Raised)
	listenTo(mouse.clicks)	
	
	reactions += {
	  case MouseClicked(_,_,_,c,_) if c == BUTTON2 => {
	    GUIManager.gotScript = None
	    ConnectorButton.active = None
	  }
	  case e:MouseClicked if GUIManager.gotScript.isDefined => {
	    
	    val script = Plugin.newInstance(GUIManager.gotScript.get)
	    val gui = GUIManager.editorNode(script)
//	    val gui = new Label("" + e.point)
	    println("EP " + e.point.toString() + " " + gui)
	
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