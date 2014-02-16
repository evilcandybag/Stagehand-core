package se.stagehand.swing.assets

import se.stagehand.lib.Log

object FileManager {
  private def log = Log.getLog(this.getClass())

  
  /**
   * Get the path to a resource local to wherever owner resides.
   */
  def localResource(owner: Any, path: String):String = {
    val ret = ClassLoader.getSystemClassLoader().getResource(path)
    ret.toExternalForm()
  }
}