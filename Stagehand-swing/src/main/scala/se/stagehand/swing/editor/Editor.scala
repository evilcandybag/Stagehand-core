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
  
  val panel = new EditorPanel
  val side = new SideMenu
  val menu = new MainMenu
  private var _frame: MainFrame = null
  def frame = _frame
  
  def top = new MainFrame {
    title = "Stagehand Editor"
    contents = ui
    menuBar = menu
    peer.setGlassPane(GUIManager.glass)
    peer.getGlassPane().setVisible(true)
    _frame = this
  }
  
  
  def ui = new BorderPanel {
    layout(side) = West
    layout(panel) = Center
  }
}
