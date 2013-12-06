package se.stagehand.swing.player

import scala.swing._
import scala.swing.BorderPanel.Position
import se.stagehand.plugins.PluginManager

object Player extends SimpleSwingApplication {

  val panel = new PlayerPanel
  PluginManager.init
  
  val ui = panel
  
  def top = new MainFrame {
    title = "Stagehand Player"
    contents = ui
    menuBar = new MainMenu
  }
  
  
}