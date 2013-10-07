package se.stagehand.plugins

import java.io.File
import org.clapper.classutil.ClassFinder
import grizzled.slf4j.Logger

object PluginManager {
  
  var pluginMap = Map[String, EffectPlugin]()
  val logger = Logger(PluginManager.getClass())
  
  def init() {
    val classpath = List(".").map(new File(_))
    val finder = ClassFinder(classpath)
    val classes = finder.getClasses
    val classMap = ClassFinder.classInfoMap(classes)
    val plugins = ClassFinder.concreteSubclasses("se.stagehand.plugins.EffectPlugin", classMap)
 
    plugins.foreach {
      pluginString =>
        val plugin = Class.forName(pluginString.name).newInstance().asInstanceOf[EffectPlugin]
        pluginMap += (plugin.name -> plugin)
    }
  }
  
  def getPlugin(name: String) {
    
  }
}