package se.stagehand.gui.components

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button

/**
 * Object that keeps track of the GUI components for GUI-classes.
 */
object GUIManager {

  private var sgui: Map[Class[_],ScriptGUI[_]] = Map()
  
  def register(c: Class[_],s: ScriptGUI[ScriptComponent]) {
    sgui += (c -> s)
  }
  
  def getGUI(c: Class[_]): ScriptGUI[_] = {
    sgui.apply(c)
  }
  
  implicit def menuItem(sc: ScriptComponent): Button = {
    sgui.apply(sc.getClass()).menuItem(sc)
  }
  
}