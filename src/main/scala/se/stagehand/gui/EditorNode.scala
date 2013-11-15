package se.stagehand.gui

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.Point
import se.stagehand.lib.scripting.ScriptComponent
import scala.xml.Elem

class EditorNode(val script: ScriptComponent) extends BorderPanel with Movable {
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	visible = true
	
	opaque = false
	
	def locationXML: Elem = 
	  <node>
		{script.idXML}
		<locx>{this.location.x}</locx>
		<locy>{this.location.y}</locy>
	  </node>
}