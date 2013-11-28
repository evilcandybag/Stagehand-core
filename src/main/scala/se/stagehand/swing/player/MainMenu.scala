package se.stagehand.swing.player

import scala.swing._
import se.stagehand.swing.lib.FileIO


class MainMenu extends MenuBar {
  val load = new Button("Load") {
    action = Action.apply("Load") (() =>{
      val xml = FileIO.loadXML("stage.xml")
      Player.panel.fromXML(xml)
      
    })
  }
  contents += load
}
