package se.stagehand.swing.player

import scala.swing._
import scala.swing.BorderPanel.Position

object Player extends SimpleSwingApplication {

  val panel = new PlayerPanel
  
  def top = new MainFrame {
    title = "Stagehand Player"
    contents = ui
    menuBar = new MainMenu
  }
  
  def ui = new BorderPanel {
    layout(panel) = Position.Center
  }
  
}