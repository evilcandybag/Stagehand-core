package se.stagehand.swing.player

import scala.swing._
import scala.swing.BorderPanel.Position
import se.stagehand.plugins.PluginManager

object Player extends SimpleSwingApplication {

  val panel = new PlayerPanel
  PluginManager.init
  val sidebar = new SideBar
  val ui = new BorderPanel {
    layout(panel) = BorderPanel.Position.Center
    layout(sidebar) = BorderPanel.Position.East
  }
  
  def top = new MainFrame {
    title = "Stagehand Player"
    contents = ui
    menuBar = new MainMenu
  }
  
  
}