package se.stagehand.lib.scripting

import scala.actors._
import scala.collection.immutable.ListSet

/**
 * The implementing ScriptComponent can receive input signals from a component
 * with Output.
 */
trait Input extends ScriptComponent with Reactor[ScriptMessage] {
  private var _senders: Set[ScriptComponent with Output] = ListSet()
  
  def connectIn(out: ScriptComponent with Output) {
    _senders += out
  }
  
  def disconnectIn(out: ScriptComponent with Output) {
    _senders -= out
  }
  
  protected def senders = _senders.toList
  
}