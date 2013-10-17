package se.stagehand.plugins

import java.io.File
import org.clapper.classutil.ClassFinder
import grizzled.slf4j.Logger
import javax.swing.filechooser.FileNameExtensionFilter
import java.io.FileFilter
import java.util.jar.JarFile
import java.net.URLClassLoader
import java.util.jar.JarEntry

object PluginManager {
  
  var pluginMap = Map[String, EffectPlugin]()
  val logger = Logger(PluginManager.getClass())
  
//  def init() {
//    val classpath = List(".").map(new File(_))
//    println(classpath)
//    val finder = ClassFinder(classpath)
//    val classes = finder.getClasses
//    val classMap = ClassFinder.classInfoMap(classes)
//    val plugins = ClassFinder.concreteSubclasses("se.stagehand.plugins.EffectPlugin", classMap)
// 
//    plugins.foreach {
//      pluginString =>
//        val plugin = Class.forName(pluginString.name).newInstance().asInstanceOf[EffectPlugin]
//        pluginMap += (plugin.name -> plugin)
//    }
//  }
  
def init() {
    val pluginDir = new File("plugins")
    val files = pluginDir.listFiles(new FileFilter(){
      def accept(x:File) = x.getPath().endsWith("jar")
    })    
    files.map(x => println(x.toString()))
    
    val classes = files.map(loadClasses(_))
//    val finder = ClassFinder(files)
//    val classes = finder.getClasses
//    val classMap = ClassFinder.classInfoMap(classes)
//    val plugins = ClassFinder.concreteSubclasses("se.stagehand.plugins.EffectPlugin", classMap)
//    
//    plugins.foreach {
//      pluginString => val plugin = Class.forName(pluginString.name).newInstance().asInstanceOf[EffectPlugin]
//      pluginMap += (plugin.name -> plugin)
//    }
  }
  
  def getPlugin(name: String) {
    
  }
  
  /**
   * Clumsy way to load classes
   */
  private def loadClasses[T](jar: File):Array[T] = {
    val file = new JarFile(jar)
    val e = file.entries()
    val urls = Array( jar.toURL() )
    val urlLoader = new URLClassLoader(urls)
    
    var classes: List[String] = List()
    var conf: JarEntry = null
    
    while (e.hasMoreElements) {
      val je = e.nextElement
      if (!je.isDirectory() && je.getName().endsWith(".class")) {
        val cn = je.getName.substring(0,je.getName.length-6).replace('/', '.')
        classes = cn :: classes
      } else if (je.getName() == "conf.xml") {
        conf = je
      }
    }
    
    null.asInstanceOf[Array[T]]
  }
}