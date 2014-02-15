package se.stagehand.swing.player

import scala.swing._
import se.stagehand.swing.lib.FileIO
import se.stagehand.lib.Log


class MainMenu extends MenuBar {
  private val log = Log.getLog(this.getClass())
  val load = new Button("Load") {
    action = new Action("Load") {
      def apply {
        val xml = FileIO.loadXML("stage.xml")
        Player.panel.fromXML(xml)
        
        Player.panel.contents.foreach(x => log.debug("c " + x.getClass.getName))
      }
    }
  }
  contents += load
}      