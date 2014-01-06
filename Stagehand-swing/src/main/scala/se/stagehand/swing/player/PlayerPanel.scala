package se.stagehand.swing.player

import se.stagehand.swing.gui.NullPanel
import java.awt.Dimension
import scala.swing.Swing
import se.stagehand.swing.lib.FileIO
import se.stagehand.lib.scripting.ScriptComponent
import java.awt.Point
import se.stagehand.lib.scripting.ID
import se.stagehand.swing.lib.GUIManager
import se.stagehand.swing.lib.ScriptGUI
import se.stagehand.swing.lib.OutputGUI
import scala.xml._
import java.awt.Color
import se.stagehand.lib.scripting.Effect
import se.stagehand.lib.scripting.StagehandComponent
import scala.swing.event.KeyEvent
import scala.swing.event.Event
import se.stagehand.swing.lib.KeyEventHandler
import se.stagehand.lib.Log

class PlayerPanel extends NullPanel {
  private val log = Log.getLog(this.getClass())
  
  preferredSize = new Dimension(500,500)
  border = Swing.EtchedBorder(Swing.Raised)
  listenTo(keys)
  deafTo(this)
  focusable = true
  requestFocus
  
  
  reactions += {
    case e:Event => {log.debug(e.toString); publish(e)}
  }
  
  def fromXML(xml:Node) {
    ID.clearInstances
    val scxml = (xml \\ "components") \ "script"
    val fxxml = (xml \\ "components") \ "effect"
    val scripts = for (n <- scxml) yield {
      StagehandComponent.fromXML[ScriptComponent](n)
    }
    val effects = for (n <- fxxml) yield {
      StagehandComponent.fromXML[Effect](n)
    }
    (scxml ++ fxxml).foreach(n => {
      val id = (n \ "id").text.toInt
      
      ID.fetch[StagehandComponent](id).readInstructions(n)
    })
    
    
    val nxml = (xml \\ "nodes") \ "node"
    val nodes = for (n <- nxml if n.label == "node") yield {
      val id = (n \ "id").text.toInt
      val pos = new Point((n \ "locx").text.toInt, (n \ "locy").text.toInt)
      
      val sc = ID.fetch[ScriptComponent](id)
      
      val gui = GUIManager.getGUI[ScriptGUI](sc.getClass())
      
      val pn = gui.playerNode(sc)
      GUIManager.register(pn)
      add(pn,pos)
      
      sc
    } 
    //Connect generated components
    for (n <- scxml) {
      val nid = (n \ "id").text.toInt
      (n \ "outputs").foreach(o => {
        val ids = (o \ "id").map(_.text.toInt)
        
        for (id <- ids.map(GUIManager.componentByID(_)) if id.isDefined && id.get.isInstanceOf[OutputGUI[_]]) {
          
        }
        
      })
    }
  }
}