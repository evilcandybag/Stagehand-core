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
import se.stagehand.lib.scripting.Output
import se.stagehand.lib.scripting.Input
import scala.swing.GridPanel
import scala.swing.FlowPanel
import scala.swing.Component
import se.stagehand.swing.lib.ComponentNode

class PlayerPanel extends FlowPanel {
  private val log = Log.getLog(this.getClass())
  
  preferredSize = new Dimension(800,640)
  border = Swing.EtchedBorder(Swing.Raised)
  listenTo(keys)
  deafTo(this)
  focusable = true
  requestFocus
  
  
  reactions += {
    case e:Event => {log.debug(e.toString); publish(e)}
  }
  
  def add(c:Component) = contents += c
  
   def clear {
      val nodes = contents.toIndexedSeq.filter(_ match {
        case cn:ComponentNode[_] => true
        case _ => false
      })
      nodes.foreach(c => c.asInstanceOf[ComponentNode[_]].remove)
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
    var infixes = List[PlayerScriptInfix[_]]()
    val nxml = (xml \\ "nodes") \ "node"
    val nodes = for (n <- nxml if n.label == "node") yield {
      val id = (n \ "id").text.toInt
      val pos = new Point((n \ "locx").text.toInt, (n \ "locy").text.toInt)
      
      val sc = ID.fetch[ScriptComponent](id)
      
      val gui = GUIManager.getGUI[ScriptGUI](sc.getClass())
      
      val pn = gui.playerNode(sc)
      GUIManager.register(pn)
      pn match {
        case node:Sidebarred[_] => {
          Player.sidebar.contents += node
        }
        case node:PlayerScriptNode[_] => {
//          add(node,pos)
          add(node)
        }
        case node:PlayerScriptInfix[_] => {
          infixes = node :: infixes
        }
      }
      
      sc
    } 
    //Connect generated components
    for (n <- scxml) {
      val nid = (n \ "id").text.toInt
      (n \ "outputs").foreach(o => {
        val ids = (o \ "id").map(_.text.toInt)
        log.debug(ids.toString)
        for (in <- ids.map(ID.fetch[ScriptComponent with Input])) {
          val out = ID.fetch[ScriptComponent with Output](nid)
          out.connectOut(in)
        }
        
      })
    }
    //Apply all infix components
    for (infix <- infixes) {
      infix.script match {
        case s: ScriptComponent with Output => s.outputs.foreach(x => {
          val reciever:Option[PlayerScriptNode[_]] = GUIManager.componentByID[PlayerScriptNode[_]](x.id)
          if (reciever.isDefined) {
            reciever.get.addInfix(infix)
          }
        })
      }
    }
    //refresh all components
    GUIManager.components.foreach(_.refresh)
  }
}