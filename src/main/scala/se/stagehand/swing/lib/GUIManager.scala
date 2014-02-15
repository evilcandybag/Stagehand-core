package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Button
import se.stagehand.plugins.ComponentGUI
import se.stagehand.plugins.Plugin
import se.stagehand.plugins.PluginManager
import scala.collection.immutable.ListSet
import scala.swing.Component
import scala.xml.Elem
import se.stagehand.swing.gui.ComponentLinkerGUI
import se.stagehand.swing.player.PlayerScriptNode
import se.stagehand.lib.scripting.StagehandComponent
import se.stagehand.lib.scripting.Effect
import se.stagehand.swing.player.PlayerGUIElement


/**
 * Object that keeps track of the GUI components for GUI-classes. I will attempt
 * to limit all the slasky GUI coding to here. 
 */
object GUIManager {
  
  var _glass:ComponentLinkerGUI = null
  
  def glass:ComponentLinkerGUI = {
    if (_glass == null) {
      _glass = new ComponentLinkerGUI
      _glass.setVisible(true)
    }
    _glass
  }
  
  var gotScript:Option[ScriptComponent] = None

  private var sgui: Map[Class[_],ComponentGUI] = Map()
  private var _sn: Set[ComponentNode[_]] = ListSet()
  
  def register(c: Class[_],s: ComponentGUI) {
    if (!sgui.contains(c)) {
      sgui += (c -> s)
    }
  }
  
  def register(gui:ComponentNode[_]) {
    _sn += gui
  }
  def unregister(gui:ComponentNode[_]) {
    _sn -= gui
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
    //menuItems are always there, no need to register...
  }
  def editorNode(sc: ScriptComponent): EditorScriptNode[_] = {
    val gui = getGUI[ScriptGUI](sc.getClass).editorNode(sc)
    register(gui)
    gui
  }
  
  def playerNode(sc: ScriptComponent): PlayerGUIElement[_] = {
    getGUI[ScriptGUI](sc.getClass).playerNode(sc)
  }
  
  def editorItem(e: Effect): EditorEffectItem[_] = {
    getGUI[EffectGUI](e.getClass).editorItem(e)
  }
  def playerItem(e: Effect): PlayerEffectItem[_] = {
    getGUI[EffectGUI](e.getClass).playerItem(e)
  }
  
  
  def componentByID[T <: ComponentNode[_]](id:Int):Option[T] = _sn.find(ec => 
    ec.component.asInstanceOf[StagehandComponent].id == id).map(_.asInstanceOf[T])
  
  def components = _sn.toList
  
  def guiXML:Elem = 
    <nodes>
	  {
    	_sn.filter(_.isInstanceOf[ScriptNode[_]])map(_.asInstanceOf[ScriptNode[_]].locationXML)
	  }
    </nodes>
  
  private def init {
    PluginManager.scriptPlugins.foreach(x => x.guis.foreach(y => 
      register(y.peer,y)
    ))
    PluginManager.effectPlugins.foreach(x => x.guis.foreach(y => 
      register(y.peer,y)
    ))
  }
}