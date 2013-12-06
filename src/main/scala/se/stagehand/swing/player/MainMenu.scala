package se.stagehand.swing.player

import scala.swing._
import se.stagehand.swing.lib.FileIO


class MainMenu extends MenuBar {
  val load = new Button("Load") {
    action = new Action("Load") {
      def apply {
        val xml = FileIO.loadXML("stage.xml")
        Player.panel.fromXML(xml)
        
        Player.panel.contents.foreach(x => println("c " + x.getClass.getName))
      }
    }
  }
  contents += load
}      