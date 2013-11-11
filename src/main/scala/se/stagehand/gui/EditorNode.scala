package se.stagehand.gui

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.Point

class EditorNode extends BorderPanel with Movable {
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	visible = true
	border = Swing.EtchedBorder(Swing.Raised)

	background = Color.blue
	
	def location(p:Point) {
	  peer.setLocation(p)
	}
}