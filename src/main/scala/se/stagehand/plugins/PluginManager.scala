package se.stagehand.plugins

import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
import java.io.FileFilter
import java.util.jar.JarFile
import java.net.URLClassLoader
import java.util.jar.JarEntry
import scala.xml.XML

object PluginManager {
  
  private var effectsMap: Map[String, EffectPlugin] = null
  private var scriptMap: Map[String, ScriptPlugin] = null

  /**
   * Load all plugins into the program.
   */
  def init() {
    effectsMap = Map[String, EffectPlugin]()
    scriptMap = Map[String, ScriptPlugin]()
    
    val pluginDir = new File("plugins")
    val files = pluginDir.listFiles(new FileFilter(){
      def accept(x:File) = x.getPath().endsWith("jar")
    })    
    if (files != null) {
      val classes = files.map(loadPluginClass(_))
    
      classes.foreach(_ match {
        case p if p.isInstanceOf[EffectPlugin] => effectsMap += (p.name -> p.asInstanceOf[EffectPlugin])
        case p if p.isInstanceOf[ScriptPlugin] => scriptMap += (p.name -> p.asInstanceOf[ScriptPlugin])
      })
    } 
  }
  
  /**
   * Fetch an effect plugin by name. 
   */
  def effectPlugin(name: String):Option[EffectPlugin] = {
    if (effectsMap == null || (effectsMap.isEmpty && scriptMap.isEmpty)) init
    effectsMap.get(name)
  }
  
  def effectPlugins():Array[EffectPlugin] = {
    if (effectsMap == null || (effectsMap.isEmpty && scriptMap.isEmpty)) init
    effectsMap.toArray.map(_._2)
  }
  
  /**
   * Find a script plugin by name. 
   */
  def scriptPlugin(name: String):Option[ScriptPlugin] = {
    if (scriptMap == null || (scriptMap.isEmpty && effectsMap.isEmpty)) init
    scriptMap.get(name)
  }
  
  def scriptPlugins():Array[ScriptPlugin] = {
    if (scriptMap == null || (scriptMap.isEmpty && scriptMap.isEmpty)) init
    scriptMap.toArray.map(_._2)
  }
  
  /**
   * Clumsy way to load classes
   */
  private def loadPluginClass(jar: File):Plugin = {
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
    val supertype = (xml \\ "supertype").text
    val classname = (xml \\ "pluginclass").text
    
    classes = classes.filter(_.endsWith(classname))
    
    
    val c = urlLoader.loadClass(classes.head)
    
    c.newInstance().asInstanceOf[Plugin]
  }
}