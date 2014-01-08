package se.stagehand.lib.scripting

import scala.actors._
import scala.collection.immutable.ListSet

/**
 * The implementing ScriptComponent can receive input signals from a component
 * with Output.
 */
trait Input extends ScriptComponent with Actor {
  private var _senders: Set[ScriptComponent with Output] = ListSet()
  
  start
  
  def connectIn(out: ScriptComponent with Output) {
    _senders += out
  }
  
  def disconnectIn(out: ScriptComponent with Output) {
    _senders -= out
  }
  
  protected def inputs = _senders
  
  def act = loop { 
    react {
      case Start(args) => executeInstructions(args)
    }
  }
    
  
}