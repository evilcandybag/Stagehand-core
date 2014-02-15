package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet
import scala.xml.Elem
import se.stagehand.lib.Log

/**
 * The implementing ScriptComponent can produce an output signal.
 */
trait Output extends ScriptComponent {
  private val log = Log.getLog(this.getClass())
  
  private var _receivers: Set[ScriptComponent with Input] = ListSet()
  
  /**
   * connect a receiver to this output
   */
  def connectOut(rec: ScriptComponent with Input) {
    log.debug("Connected " + rec)
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
  def outputs = _receivers
  
  /**
   * Signal all connected Inputs with the given ControlMessage.
   */
  def signal(msg: ScriptMessage) {
    log.debug(_receivers.toString)
    _receivers.foreach(x => {log.debug("messaging: " + x.displayName); x ! msg})
  }
  
  def outputsXML: Elem = 
    <outputs>{log.debug(_receivers.toString);_receivers.map(_.idXML)}</outputs>
}
