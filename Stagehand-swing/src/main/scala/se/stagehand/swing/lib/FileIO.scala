package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ID
import scala.xml.PrettyPrinter
import java.io.PrintWriter
import scala.xml.XML
import scala.xml.Elem
import se.stagehand.lib.Log

object FileIO {
  private val log = Log.getLog(this.getClass())
  def saveXML(s:String) {
    val modelXML = ID.allXML
    val guiXML = GUIManager.guiXML
    val printer = new PrettyPrinter(100,4)
    
    val out = new PrintWriter( s , "UTF-8")
    try { 
      out.println("<?xml version=\"1.0\" ?>")
      out.println("<stage>")
      out.println( printer.format(modelXML) )
      log.debug( printer.format(modelXML))
      out.println( printer.format(guiXML) )
      log.debug( printer.format(guiXML))
      out.println("</stage>")
    }
    finally{ out.close }
  }
  
  def loadXML(s:String): Elem = {
    XML.loadFile(s)
  }
}