package se.stagehand.lib.scripting

import scala.xml._
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
  
  val description: String
  
  protected val kind = "script"
  
  override def generateInstructions: Node = 
    <script class={this.getClass().getName()}>{idXML}<name>{displayName}</name></script>
  
  /**
   * Execute the given instructions over the assigned targets. 
   */
  def executeInstructions(params:Any*): Unit

  override def readInstructions(in:Node) {
    super.readInstructions(in)
    val name = (in \\ "name")(0).text
    displayName = name
  }
}

object ScriptComponent {
  def fromXML[T <: ScriptComponent](e:Node):T = {
    val className = (e \ "@class").text
    val id = (e \ "id")(0).text.toInt
    val name = (e \ "name")(0).text
    val sc = ID.newInstance[T](className,id)
//    sc.readInstructions(e)
    sc.displayName = name
    
    sc
  }
}

/**
 * Base class for messages to send to other components. The case classes have
 * varargs for optional parameters and typ arguments should you want to specify
 * those arguments.
 */
abstract class ScriptMessage
case class Stop[T](param:T*) extends ScriptMessage
case class Start[T](param:T*) extends ScriptMessage