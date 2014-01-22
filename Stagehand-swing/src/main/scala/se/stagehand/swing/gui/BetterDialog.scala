package se.stagehand.swing.gui

import scala.swing._
import se.stagehand.swing.lib.Vector2

class BetterDialog extends Dialog {
  def centerOn(component:Component) { 
    centerOn(GUIUtils.rectCenter(component.bounds))
  }
  def centerOn(p:Point) {
    val center = 
    location = GUIUtils.rectCenter(bounds).neg + p
  }
}