package se.stagehand.lib.scripting.ast

import java.awt.Point
import scala.xml._

sealed abstract class ASTNode {
  def typ: String
}

sealed case class Stage(components: List[Component], ui: List[UINode])

sealed abstract class Component(cls: String, i:Int, p: List[Prop]) extends ASTNode {

}
case class Script(c: String, id:Int, p:List[Prop]) extends Component(c,id,p) {
  def typ = "script"
}
case class Effect(c: String, id:Int, p:List[Prop]) extends Component(c,id,p) {
  def typ = "effect"
}

sealed trait Prop
case class Val(name: String, value: String) extends Prop
case class PList(name: String, vals: List[Val]) extends Prop

//Represents a node in an UI
final case class UINode(id: Int, pos: Point)

object examples {
  val tree = {
    val e = Effect("Popup",1,List(Val("message","bajs")))
    
  }
}