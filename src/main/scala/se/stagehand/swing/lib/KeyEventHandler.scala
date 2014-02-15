package se.stagehand.swing.lib

import java.awt.KeyboardFocusManager
import java.awt.KeyEventDispatcher
import scala.swing.Publisher
import java.awt.event.KeyListener
import java.awt.event.{KeyEvent => JKeyEvent}
import scala.swing.event._

/**
 * Trap KeyEvents globally, and send them on to any listeners.
 */
object KeyEventHandler extends Publisher {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventDispatcher(
    new KeyEventDispatcher() {
      override def dispatchKeyEvent(e: JKeyEvent):Boolean = {
        publish (e.getID match {
          case JKeyEvent.KEY_PRESSED  => new KeyPressed(e)
          case JKeyEvent.KEY_TYPED    => new KeyTyped(e)
          case JKeyEvent.KEY_RELEASED => new KeyReleased(e)
        })
        
        false
      }
    }  
  )
  
  def register(p:Publisher) {
    p.listenTo(this)
  }
}