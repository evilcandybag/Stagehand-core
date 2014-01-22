package se.stagehand.swing.gui

import scala.swing._
import se.stagehand.swing.lib.Vector2

object GUIUtils {
  /**
   * Get the center of a rectangle in world coordinates.
   */
  def rectCenter(rect:Rectangle) = {
    Vector2( rect.x + rect.width / 2, rect.y + rect.height / 2 )
  }
  /**
   * Get the center of a rectangle in local coordinates.
   */
  def centerOf(rect:Rectangle) {
    Vector2( rect.width / 2, rect.height / 2)
  }
  
}