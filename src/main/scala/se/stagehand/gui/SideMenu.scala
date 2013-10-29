package se.stagehand.gui

import scala.swing._
import se.stagehand.plugins.PluginManager
import se.stagehand.gui.components._

/**
 * Fetches all programming components and adds them 
 */
class SideMenu extends BoxPanel(Orientation.Vertical) {
  
  border = Swing.EtchedBorder(Swing.Raised)
  PluginManager.scriptPlugins.foreach( x =>  
    x.scriptcomponents.foreach( y => 
      contents += y
    )
  )
  

}