package se.stagehand.lib.scripting

import scala.xml.Elem
import scala.actors.Actor

/**
 * The component that decides how effects are used.
 */
abstract class ScriptComponent(id: Int) extends StagehandComponent(id) {
  def this() = this(ID.unique)
  
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