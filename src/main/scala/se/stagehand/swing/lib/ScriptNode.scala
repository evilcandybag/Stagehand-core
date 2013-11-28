package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Component
import scala.xml.Node
import scala.swing.BorderPanel

trait ScriptNode[T <: ScriptComponent] extends BorderPanel {
  def script: T
  
  def locationXML: Node = 
	  <node>
		{script.idXML}
		<locx>{this.location.x}</locx>
		<locy>{this.location.y}</locy>
	  </node>
}