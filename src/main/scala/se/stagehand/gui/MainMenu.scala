package se.stagehand.gui

import scala.swing._
import se.stagehand.lib.scripting.ID
import se.stagehand.gui.components.GUIManager
import scala.xml.PrettyPrinter
import se.stagehand.editor.FileIO

class MainMenu extends MenuBar {
  val file = new Menu("File")
  val save = new Button("Save") {
    action = new Action("Save") {
      def apply {
        FileIO.saveXML("stage.xml") //TODO: Let users specify file names later
      }
    }
  }
  
  contents += save
}