package se.stagehand.gui.components

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button
import se.stagehand.plugins.ComponentGUI
import se.stagehand.plugins.Plugin
import se.stagehand.plugins.PluginManager
import se.stagehand.gui.EditorNode


/**
 * Object that keeps track of the GUI components for GUI-classes. I will attempt
 * to limit all the slasky GUI coding to here. 
 */
object GUIManager {
  
  var gotScript:Option[ScriptComponent] = None

  private var sgui: Map[Class[_],ComponentGUI] = Map()
  
  def register(c: Class[_],s: ComponentGUI) {
    if (!sgui.contains(c)) {
      sgui += (c -> s)
    }
  }
  
  def registered(c: Class[_]): Boolean = {
    sgui.contains(c)
  }
  
  def getGUI[T <: ComponentGUI](c: Class[_]): T = {
    if(!sgui.contains(c)) {
      init
    }
    sgui.apply(c).asInstanceOf[T]
  }
  
  def menuItem(sc: ScriptComponent): Button = {
    getGUI[ScriptGUI](sc.getClass).menuItem(sc)
  }
  def editorNode(sc: ScriptComponent): EditorNode = {
    getGUI[ScriptGUI](sc.getClass).editorNode(sc)
  }
  
  private def init {
    PluginManager.scriptPlugins.foreach(x => x.guis.foreach(y => 
      register(y.peer,y)
    ))
    PluginManager.effectPlugins.foreach(x => x.guis.foreach(y => 
      register(y.peer,y)
    ))
  }
}