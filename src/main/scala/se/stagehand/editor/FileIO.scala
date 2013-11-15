package se.stagehand.editor

import se.stagehand.lib.scripting.ID
import se.stagehand.gui.components.GUIManager
import scala.xml.PrettyPrinter
import java.io.PrintWriter

object FileIO {
  def saveXML(s:String) {
    val modelXML = ID.allXML
    val guiXML = GUIManager.guiXML
    val printer = new PrettyPrinter(100,4)
    
    val out = new PrintWriter( s , "UTF-8")
    try { 
      out.println( printer.format(modelXML) )
      out.println( printer.format(guiXML) )
    }
    finally{ out.close }
  }
}