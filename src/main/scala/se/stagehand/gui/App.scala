package se.stagehand.gui

import scala.swing._
import se.stagehand.plugins._
import java.io.File
import scala.swing.BorderPanel.Position._

/**
 * @author ${user.name}
 */
object App extends SimpleSwingApplication {

  val glass = new ComponentLinkerGUI
  
  def top = new MainFrame {
    title = "Stagehand Editor"
    contents = ui
    menuBar = new MainMenu
    peer.setGlassPane(glass)
    peer.getGlassPane().setVisible(true)
  }
  
  def ui = new BorderPanel {
    layout(new SideMenu) = West
    layout(new EditorPanel) = Center
  }
}
