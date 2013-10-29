package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet
import scala.xml.Elem

/**
 * The implementing ScriptComponent can produce an output signal.
 */
trait Output extends ScriptComponent {
  
  private var _receivers: Set[ScriptComponent with Input] = ListSet()
  
  /**
   * connect a receiver to this output
   */
  def connectOut(rec: ScriptComponent with Input) {
    _receivers = _receivers + rec
  }
  
  /**
   * disconnect a receiver from this output.
   */
  def disconnectOut(rec: ScriptComponent with Input) {
    _receivers = _receivers - rec
  }
  
  /**
   * Get all connected receivers
   */
  protected def receivers:List[ScriptComponent with Input] = _receivers.toList
  
  /**
   * Signal all connected Inputs with the given ControlMessage.
   */
  def signal(msg: ScriptMessage): Unit 
  
  def outputsXML: Elem = 
    <outputs>_receivers.map(_.idXML)</outputs>
}
