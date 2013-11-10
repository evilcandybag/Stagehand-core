package se.stagehand.gui

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.Point

class EditorNode extends FlowPanel with Movable {
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	visible = true
	border = Swing.EtchedBorder(Swing.Raised)

	background = Color.blue
	
	contents += {
	  new Label("BAJS")
	}
	
	reactions += {
	  case e:MouseClicked => println(e.point)
	}
	
	def location(p:Point) {
	  peer.setLocation(p)
	}
}