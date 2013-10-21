package se.stagehand.plugins

/**
 * Base trait for plugins.
 */
trait Plugin {
  /**
   * The name of the Plugin. This is used as an identifier when loading.
   */
  val name: String

}