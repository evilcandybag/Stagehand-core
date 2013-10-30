package se.stagehand.plugins

/**
 * Trait for GUI wrappers, independent of GUI  framework.
 */
trait ComponentGUI {

  val peer: Class[_]
  
}