package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Component
import scala.xml.Node
import scala.swing.BorderPanel
import se.stagehand.lib.scripting.StagehandComponent
import se.stagehand.lib.scripting.Effect

trait ComponentNode[T <: StagehandComponent] {
  def component: T
}

trait ScriptNode[T <: ScriptComponent] extends BorderPanel with ComponentNode[T]{
  def component = script
  
  def script: T
  
  def locationXML: Node = 
	  <node>
		{script.idXML}
		<locx>{this.location.x}</locx>
		<locy>{this.location.y}</locy>
	  </node>
}

trait EffectNode[T <: Effect] extends Component with ComponentNode[T] {
  def component = effect
  def effect: T
} 