package se.stagehand.gui

import scala.swing._
import se.stagehand.plugins._
import java.io.File
import scala.swing.BorderPanel.Position._

/**
 * @author ${user.name}
 */
object App extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title = "Stagehand Editor"
    contents = ui
  }
  
  def ui = new BorderPanel {
   
    layout(new SideMenu) = West
    layout(new EditorPanel) = Center
  }
}
