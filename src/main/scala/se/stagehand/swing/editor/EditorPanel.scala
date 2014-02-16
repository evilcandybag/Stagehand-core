package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.plugins._
import java.awt.event.MouseEvent.{BUTTON1,BUTTON2}
import se.stagehand.swing.gui.NullPanel
import scala.swing.event._
import se.stagehand.swing.lib._
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.Log
import scala.xml.Node
import se.stagehand.lib.scripting.StagehandComponent
import se.stagehand.lib.scripting.ScriptComponent
import se.stagehand.lib.scripting.Effect
import se.stagehand.lib.scripting.Input
import se.stagehand.lib.scripting.Output

class EditorPanel extends NullPanel {
    private val log = Log.getLog(this.getClass())
	preferredSize = new Dimension(500,500)
	border = Swing.EtchedBorder(Swing.Raised)
	listenTo(mouse.clicks)	
	
	reactions += {
	  case e:MouseClicked if GUIManager.gotScript.isDefined => {
	    
	    val script = ID.newInstance(GUIManager.gotScript.get)
	    val gui = GUIManager.editorNode(script)
//	    val gui = new Label("" + e.point)
	
	    add(gui,e.point)
	    GUIManager.register(gui)
	    GUIManager.gotScript = None
	    
	    SelectionManager.cancelSelection
	    
	    gui.refresh
	    
	    repaint
	    revalidate
	  }
	}
    
    def clear {
      contents.foreach(_ match {
        case cn:ComponentNode[_] => cn.remove
      })
    }
    
    def fromXML(xml:Node) {
        clear
	    ID.clearInstances
	    val scxml = (xml \\ "components") \ "script"
	    val fxxml = (xml \\ "components") \ "effect"
	    //Create all ScriptComponents
	    val scripts = for (n <- scxml) yield {
	      StagehandComponent.fromXML[ScriptComponent](n)
	    }
	    //Create all Effects
	    val effects = for (n <- fxxml) yield {
	      StagehandComponent.fromXML[Effect](n)
	    }
	    //Read instructions for all loaded StagehandComponents
	    (scxml ++ fxxml).foreach(n => {
	      val id = (n \ "id").text.toInt
	      
	      ID.fetch[StagehandComponent](id).readInstructions(n)
	    })
	    
	    //Generate all PlayerScriptGUIs and place PlayerScriptNodes
	    val nxml = (xml \\ "nodes") \ "node"
	    val nodes = for (n <- nxml if n.label == "node") yield {
	      val id = (n \ "id").text.toInt
	      val pos = new Point((n \ "locx").text.toInt, (n \ "locy").text.toInt)
	      
	      val sc = ID.fetch[ScriptComponent](id)
	      
	      val gui = GUIManager.getGUI[ScriptGUI](sc.getClass())
	      
	      val en = gui.editorNode(sc)
	      GUIManager.register(en)
	      
	      add(en, pos)
	      
	      sc
	    } 
	    //Connect generated components
	    for (n <- scxml) {
	      val nid = (n \ "id").text.toInt
	      (n \ "outputs").foreach(o => {
	        val ids = (o \ "id").map(_.text.toInt)
	        log.debug(ids.toString)
	        for (in <- ids.map(GUIManager.componentByID[InputGUI[_]])) {
	          val out = GUIManager.componentByID[OutputGUI[_]](nid)
	          out match {
	            case Some(o) => {
	              in match {
	                case Some(i) => {
	                  o.outCon.connect(i.inCon)
	                }
	                case None => {log.error("Could not find input node")}
	              }
	            }
	            case None => {log.error("Could not find node with ID: " + nid)}
	          }
	        }
	        
	      })
	    }
	    
	    //refresh all components
	    GUIManager.components.foreach(_.refresh)
  }
}