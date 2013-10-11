package se.stagehand

import scala.swing._

/**
 * @author ${user.name}
 */
object App {
    
  def main(args : Array[String]) {
    val frame = new Frame
    val panel = new BorderPanel
    
    frame.contents = panel
    
    frame.visible = true
    
  }

}
