package se.stagehand.lib.scripting

import scala.actors.Actor

/**
 * The implementing ScriptComponent can receive input signals from a component
 * with Output.
 */
trait Input extends ScriptComponent with Actor {
}