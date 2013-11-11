package se.stagehand.lib.scripting

import scala.xml.Elem
import scala.actors.Actor

/**
 * The component that decides how effects are used.
 */
abstract class ScriptComponent(id: Int) extends StagehandComponent(id) {
  def this() = this(ID.unique)
  
  var _displayName: String = componentName + " #" + id
  def displayName: String = _displayName
  def displayName_=(s:String) {
    if (s != null && s.length < 1) {
      throw new IllegalArgumentException("Display Name must be at least one character")
    } else {
      _displayName = s
    }
  }
  
  override def generateInstructions: Elem = 
    <script class={this.getClass().getName()}>{idXML}</script>

  
}

/**
 * Base class for messages to send to other components. The case classes have
 * varargs for optional parameters and typ arguments should you want to specify
 * those arguments.
 */
abstract class ScriptMessage
case class Stop[T](param:T*) extends ScriptMessage
case class Start[T](param:T*) extends ScriptMessage