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

class PlayerPanel extends NullPanel {
  preferredSize = new Dimension(500,500)
  border = Swing.EtchedBorder(Swing.Raised)
  
  def fromXML(xml:Node) {
    
    val scxml = (xml \\ "scripts")
    val scripts = scxml.map(ScriptComponent.fromXML(_))
    
    val nxml = (xml \\ "nodes")
    val nodes = for (n <- nxml if n.text == "node") yield {
      val id = (n \ "id").text.toInt
      val pos = new Point((n \ "locx").text.toInt, (n \ "locy").text.toInt)
      
      val sc = ID.fetch[ScriptComponent](id)
      
      val gui = GUIManager.getGUI[ScriptGUI](sc.getClass())
      
      val pn = gui.playerNode(sc)
      GUIManager.register(pn)
      add(pn,pos)
      
      sc
    } 
    
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