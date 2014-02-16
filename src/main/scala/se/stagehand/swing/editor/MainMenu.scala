package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.swing.lib.FileIO
import se.stagehand.lib.Log

class MainMenu extends MenuBar {
  private val log = Log.getLog(this.getClass())
  
  val file = new Menu("File")
  val save = new Button("Save") {
    action = new Action("Save") {
      def apply {
        FileIO.saveXML("stage.xml") //TODO: Let users specify file names later
      }
    }
  }
  val load = new Button("Load") {
    action = new Action("Load") {
      def apply {
        val xml = FileIO.loadXML("stage.xml")
        Editor.panel.fromXML(xml)
        
        Editor.panel.contents.foreach(x => log.debug("c " + x.getClass.getName))
      }
    }
  }
  
  contents += save
  contents += load
}