package se.stagehand

import scala.swing._
import se.stagehand.plugins._
import java.io.File

/**
 * @author ${user.name}
 */
object App {
    
  def main(args : Array[String]) {
    val frame = new Frame
    val panel = new BorderPanel
    
    PluginManager.init
    
    frame.contents = panel
    
    frame.visible = true
    
  }

}
