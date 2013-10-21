package se.stagehand.plugins

import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
import java.io.FileFilter
import java.util.jar.JarFile
import java.net.URLClassLoader
import java.util.jar.JarEntry
import scala.xml.XML

object PluginManager {
  
  private var pluginMap = Map[String, EffectPlugin]()
  
  def init() {
    val pluginDir = new File("plugins")
    val files = pluginDir.listFiles(new FileFilter(){
      def accept(x:File) = x.getPath().endsWith("jar")
    })    
    
    val classes = files.map(loadPluginClass(_))
    
    classes.foreach(plugin => pluginMap += (plugin.name -> plugin))
    
  }
  
  def getPlugin(name: String):Option[EffectPlugin] = {
    if (pluginMap.isEmpty) init
    pluginMap.get(name)
  }
  
  def getPlugins():Array[EffectPlugin] = {
    if (pluginMap.isEmpty) init
    pluginMap.toArray.map(_._2)
  }
  
  /**
   * Clumsy way to load classes
   */
  private def loadPluginClass(jar: File):EffectPlugin = {
    val file = new JarFile(jar)
    val e = file.entries()
    val urls = Array( jar.toURL() )
    val urlLoader = URLClassLoader.newInstance(urls)
    
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
    val stream = file.getInputStream(conf)
    val xml = XML.load(stream)
    val classname = (xml \\ "pluginclass").text
    
    classes = classes.filter(_.endsWith(classname))
    
    
    val c = urlLoader.loadClass(classes.head)
    
    c.newInstance().asInstanceOf[EffectPlugin]
  }
}