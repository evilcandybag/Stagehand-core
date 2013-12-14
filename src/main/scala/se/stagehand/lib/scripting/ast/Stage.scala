package se.stagehand.lib.scripting.ast

import java.awt.Point
import scala.xml._

final case class Stage

sealed abstract class ASTNode {
  def toXML: Node
  protected def typ: String
  
}

sealed abstract class Component(c: Class[_], i:Int) {
  val cls:Class[_] = c; val id:Int = i
  
//  def toXML = 
//    <component class={cls.getName}
//             id={i.toString}></component>.copy(label = typ)
    
}
abstract class Script(c: Class[_], id:Int) extends Component(c,id)
abstract class Effect(c: Class[_], id:Int) extends Component(c,id)

trait Outputs extends Script

//Represents a node in an UI
final case class UINode(id: Int, pos: Point)