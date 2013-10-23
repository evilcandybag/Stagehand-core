package se.stagehand.gui

import scala.swing._
import se.stagehand.plugins.PluginManager
import se.stagehand.gui.components._

/**
 * Fetches all programming components and adds them 
 */
class SideMenu extends BoxPanel(Orientation.Vertical) {
  
  border = Swing.EmptyBorder(5,5,5,5)
  
  ScriptGUI.menuList(PluginManager.scriptPlugins).foreach(contents += _)

}