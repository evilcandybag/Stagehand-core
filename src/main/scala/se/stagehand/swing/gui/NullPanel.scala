package se.stagehand.swing.gui

import scala.swing._
import scala.swing.event._
import se.stagehand.swing.lib.Vector2._
import se.stagehand.swing.lib.Vector2
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Panel class that works with arbitrary placement of components.
 */
class NullPanel extends Panel {
  peer.setLayout(null)
  override lazy val peer = new NullJPanel with SuperMixin
   
  override def contents = _contents
  
  def add(comp: Component, p: Point) {
    add(comp,p.x,p.y)
  }
  
  def add(comp: Component, x: Int, y: Int) {
    this.contents += comp
    comp.peer.setLocation(x, y)
    
    comp.repaint
    comp.revalidate
  }
  
  class NullJPanel extends JPanel {
    setLayout(null)
    override def add(c:java.awt.Component):java.awt.Component = {
      super.add(c)
      c.setLocation(0, 0)
      c.setSize(c.getPreferredSize)
      
      c
    }
  }
  
}

trait Movable extends Component {
    var dragstart:Vector2 = null
    listenTo(mouse.clicks, mouse.moves)
    reactions += {
        case e:MouseDragged =>
            if( dragstart != null )
                peer.setLocation(location - dragstart + e.point)
        case e:MousePressed =>
            this match {
                case component:Resizable =>
                    if( component.resizestart == null )
                        dragstart = e.point
                case _ =>
                    dragstart = e.point

            }
        case e:MouseReleased =>
            dragstart = null    
    }
}

trait Resizable extends Component{
    var resizestart:Vector2 = null
    var oldsize = Vector2(0,0)
    def resized(delta:Vector2) {}
    listenTo(mouse.clicks, mouse.moves)
    reactions += {
        case e:MouseDragged =>
            if( resizestart != null ){
                val delta = e.point - resizestart
                peer.setSize(max(oldsize + delta, minimumSize))

                oldsize += delta
                resizestart += delta

                resized(delta)
                revalidate
            }
        case e:MousePressed =>
            if( size.width - e.point.x < 15
             && size.height - e.point.y < 15 ){
                resizestart = e.point
                oldsize = size

                this match {
                    case component:Movable =>
                        component.dragstart = null
                    case _ =>
                }
            }
        case e:MouseReleased =>
            resizestart = null
    }
}

trait QuickSubmit extends TextField {
  reactions += {
    case e:KeyTyped if e.char == '\n' || e.char == '\t' => publish(EditDone(this))
  }
}