package se.stagehand.swing.player

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.network.NetworkedTarget
import com.jidesoft.swing.MultilineLabel
import scala.collection.mutable.StringBuilder
import se.stagehand.lib.Log
import se.stagehand.lib.scripting._
import scala.swing._
import se.stagehand.swing.gui.BetterDialog
import java.awt.MouseInfo
import java.awt.Dimension
import javax.swing.JList
import javax.swing.JLabel
import javax.swing.ListCellRenderer
import javax.swing.event.ListSelectionListener
import scala.swing.event.SelectionChanged
import scala.swing.event.MouseClicked
import se.stagehand.swing.gui.PopupMenu
import javax.swing.SwingUtilities

object NetworkedTargetPicker {
  private val log = Log.getLog(this.getClass())
  /**
   * Add targets to an effect.
   */
  def pickTargets(filter: Set[_ <: Target] => Set[_ <: Target], applier: Target => Unit, existing: Set[_<: Target], remover:Target => Unit) {
    val dialog = new TargetDialog(filter, existing, remover)
    dialog.targets.foreach(applier)
  }
  
  class TargetDialog(filter: Set[_ <: Target] => Set[_ <: Target], existing: Set[_<: Target], remover:Target => Unit) extends BetterDialog {
    private val allTargets:Set[_ <: Target] = Target.allTargets.toSet
    private val valid = filter(allTargets)
    
    log.debug("Valid targets: " + valid.size + " all targets : " + allTargets.size)
    
    private val targetView = new ListView[Target](valid.toSeq) {
      lazy val typedPeer = peer.asInstanceOf[JList[Target]]
      
      border = Swing.EtchedBorder(Swing.Raised)
      typedPeer.setCellRenderer(new Renderer)
    }
    private val existList = new BoxPanel(Orientation.Vertical) {
      border = Swing.EtchedBorder(Swing.Raised)
      
      existing.foreach(x => {
        contents += new ListWrapper(x)
      })
    }
    
    private def items = targetView.selection.items
    private var _targets:Seq[Target] = Seq()
    def targets = _targets
    
    title = "Choose Targets"
    modal = true
    
    import BorderPanel.Position._
    contents = new BorderPanel {
      
      layout(new FlowPanel {
        border = Swing.EmptyBorder(5,5,5,5)
   
        contents += new BoxPanel(Orientation.Vertical) {
          contents += new Label("All Valid")
          contents += targetView
        }
        contents += new BoxPanel(Orientation.Vertical) {
          contents += new Label("Added")
          contents += existList
        }
      }) = Center

      layout(new FlowPanel(FlowPanel.Alignment.Right)(
        Button("Select") {
          _targets = items
          close()
        }
      )) = South
      
      layout( Component.wrap(new MultilineLabel {
        private val tvs = targetView.selection
        listenTo(tvs)
        
        reactions += {
          case e:SelectionChanged => {
            if (tvs.items.size > 0) {
//              setText((tvs.items(tvs.anchorIndex).prettyDescription))
            }
            refresh
          }
        }
        
      })) = East
      refresh
    }
    
    centerOn(MouseInfo.getPointerInfo().getLocation())
    open()

    
    
    
    private class Renderer extends JLabel with ListCellRenderer[Target] with Publisher {
      
      def getListCellRendererComponent(list:JList[_ <: Target], item: Target, index:Int, selected:Boolean, focus:Boolean) = {
        setText(item.prettyName)
        
        if (selected) {
          setBackground(list.getSelectionBackground)
          setForeground(list.getSelectionForeground)
        } else {
          setBackground(list.getBackground)
          setForeground(list.getForeground)
        }
        setEnabled(list.isEnabled())
        setFont(list.getFont)
        setOpaque(true)        
        
        this
      }
    }
    
    private class ListWrapper(target:Target) extends Label {
      var me = this
      text = this.name
      
      
      reactions += {
      	case e: MouseClicked if SwingUtilities.isRightMouseButton(e.peer)  => {
      	  var cursor = MouseInfo.getPointerInfo.getLocation
          SwingUtilities.convertPointFromScreen(cursor, this.peer)
          contextMenu.show(this, cursor.x, cursor.y)
        }
      }
  
      private val contextMenu = new PopupMenu {
    	 contents += new MenuItem(new Action("Delete") {
    		 def apply {
    			 existList.contents -= me
    			 remover(target)
    		 }
    	 })
      }
    }
  }
}