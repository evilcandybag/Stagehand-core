package se.stagehand.swing.gui

import scala.swing._
import se.stagehand.swing.lib.Vector2
import java.awt.MouseInfo
import se.stagehand.lib.Log

class BetterDialog extends Dialog {
  protected val log = Log.getLog(this.getClass())
  
  def centerOn(component:Component) { 
    centerOn(GUIUtils.rectCenter(component.bounds))
  }
  def centerOn(p:Point) {
    val center = 
    location = GUIUtils.rectCenter(bounds).neg + p
  }
  def refresh {
    log.debug("" + preferredSize)
      peer.setSize(preferredSize)
      contents.foreach(_.revalidate)
      repaint
    }
}

object BetterDialog {
  def inputDialog[T](dialog:InputDialog[T], position: Point):T = {
    dialog.centerOn(position)
    dialog.open()
    dialog.selected
  }
  def inputDialog[T](dialog:InputDialog[T]):T = inputDialog[T](dialog,GUIUtils.mouse.getLocation())
  
  abstract class InputDialog[T] extends BetterDialog {
    def selected:T
    
    modal = true
  }
}