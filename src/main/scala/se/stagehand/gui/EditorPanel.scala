package se.stagehand.gui

import scala.swing._
import scala.swing.event.MouseClicked
import se.stagehand.gui.components.GUIManager
import se.stagehand.plugins._

class EditorPanel extends NullPanel {
	preferredSize = new Dimension(500,500)
	border = Swing.EtchedBorder(Swing.Raised)
	listenTo(mouse.clicks)
	
	
	reactions += {
	  case e:MouseClicked if GUIManager.gotScript.isDefined => {
	    
	    val script = Plugin.newInstance(GUIManager.gotScript.get)
	    val gui = GUIManager.editorNode(script)
//	    val gui = new Label("" + e.point)
	    println("EP " + e.point.toString() + " " + gui)
	
	    add(gui,e.point)
	    
	    gui.revalidate
	    gui.repaint
	    
	    repaint
	    revalidate
	  }
	}
}