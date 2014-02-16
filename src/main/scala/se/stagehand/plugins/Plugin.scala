package se.stagehand.plugins

import se.stagehand.lib.scripting._
import java.net.URL
import se.stagehand.lib.Log

/**
 * Base trait for plugins.
 */
trait Plugin {
  /**
   * The name of the Plugin. This is used as an identifier when loading.
   */
  val name: String

  /**
   * A list of all the ComponentGUIs
   */
  val guis: List[ComponentGUI]
  
}
object Plugin {
  def newInstance(e:Effect):Effect = {
    e.getClass.newInstance().asInstanceOf[e.type]
  }
  
  def newInstance(s: ScriptComponent):ScriptComponent = {
    s.getClass.newInstance().asInstanceOf[s.type]
  }
  
  
}