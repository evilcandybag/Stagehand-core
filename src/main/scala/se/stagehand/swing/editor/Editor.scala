package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.plugins._
import scala.swing.BorderPanel.Position._
import se.stagehand.swing.gui._
import se.stagehand.swing.lib.GUIManager

/**
 * @author ${user.name}
 */
object Editor extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title = "Stagehand Editor"
    contents = ui
    menuBar = new MainMenu
    peer.setGlassPane(GUIManager.glass)
    peer.getGlassPane().setVisible(true)
  }
  
  def ui = new BorderPanel {
    layout(new SideMenu) = West
    layout(new EditorPanel) = Center
  }
}
