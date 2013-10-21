package se.stagehand

import scala.swing._
import se.stagehand.plugins._
import java.io.File
import scala.swing.BorderPanel.Position._

/**
 * @author ${user.name}
 */
object App extends SimpleSwingApplication {
  
  val plugins = PluginManager.getPlugins
  
  def top = new MainFrame {
    title = "Stagehand Editor"
    contents = ui
  }
  
  def ui = new BorderPanel {
   
    layout(sidemenu) = West
  }
  
  def sidemenu = {
    val pan = new FlowPanel() {
      border = Swing.EmptyBorder(5,5,5,5)
    }
    plugins.foreach( pi =>
      pi.getEffects.foreach(ef => 
      	pan.contents += ef
      )
    )
    
    pan
  }
  

}
