package se.stagehand.lib


object FileManager {
  private def log = Log.getLog(this.getClass())

  
  /**
   * Get the path to a resource local to wherever owner resides.
   */
  def localResource(path: String):String = {
    val ret = ClassLoader.getSystemClassLoader().getResource(path)
    ret.toExternalForm()
  }
  
  def localPath(owner:Any):String = {
    val url = owner.getClass.getProtectionDomain().getCodeSource().getLocation()
    log.debug("Getting a local URL: " + url.toString())
    url.getPath()
  }
}