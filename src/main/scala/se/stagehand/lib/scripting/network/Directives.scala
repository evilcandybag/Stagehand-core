package se.stagehand.lib.scripting.network

/**
 * Collection of constants for various directives to servers
 */
object Directives {
  /**
   * Directives dealing with the persistence of effects on servers
   */
  object Persistence {
    /**
     * Clear all effects currently on the server
     */
    val CLEAR = "clear"
    /**
     * Keep all effects that are not overridden by the sent effect
     */
    val KEEP = "keep"
    
    val ARG = "persistence"
  }
}