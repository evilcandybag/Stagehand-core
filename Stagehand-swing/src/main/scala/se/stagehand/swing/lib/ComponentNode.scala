package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Component
import scala.xml.Node
import scala.swing.BorderPanel
import se.stagehand.lib.scripting.StagehandComponent
import se.stagehand.lib.scripting.Effect
import java.awt.Dimension
import scala.swing.BoxPanel
import scala.swing.Orientation

trait ComponentNode[+T <: StagehandComponent] extends Component {
  def component: T
  
  def refresh {
    peer.setSize(preferredSize)
    revalidate
    repaint
  }
  
  class ContentPanel(name:String) extends BoxPanel(Orientation.Vertical) {
    override def toString = name + "\n\t" + contents.map(_.toString).mkString("\n\t")
  }
}

trait ScriptNode[+T <: ScriptComponent] extends ComponentNode[T]{
  def component = script
  
  def script: T
  
  def locationXML: Node = 
	  <node>
		{script.idXML}
		<locx>{this.location.x}</locx>
		<locy>{this.location.y}</locy>
	  </node>
}

trait EffectNode[T <: Effect] extends ComponentNode[T] {
  def component = effect
  def effect: T
} 