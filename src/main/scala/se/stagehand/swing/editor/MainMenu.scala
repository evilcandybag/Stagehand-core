package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.swing.lib.FileIO

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