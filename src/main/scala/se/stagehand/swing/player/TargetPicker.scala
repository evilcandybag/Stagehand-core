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

object NetworkedTargetPicker {
  private val log = Log.getLog(this.getClass())
  /**
   * Add targets to an effect.
   */
  def pickTargets(effect: Effect with Targets) {
    val dialog = new TargetDialog(effect)
    dialog.targets.foreach(effect.addTarget(_))
  }
  
  class TargetDialog(effect: Effect with Targets) extends BetterDialog {
    private val log = Log.getLog(this.getClass)
    private val allTargets = NetworkedEffect.targets
    private val valid = for (e <- effect.validTargets(allTargets) if e.isInstanceOf[NetworkedTarget]) 
      yield e.asInstanceOf[NetworkedTarget]
    
    log.debug("Valid targets: " + valid.size + " all targets : " + allTargets.size)
    
    private val targetView = new ListView[NetworkedTarget](valid.toSeq) {
      lazy val typedPeer = peer.asInstanceOf[JList[NetworkedTarget]]
      
      border = Swing.EtchedBorder(Swing.Raised)
      typedPeer.setCellRenderer(new Renderer)
    }
    
    private def items = targetView.selection.items
    private var _targets:Seq[NetworkedTarget] = Seq()
    def targets = _targets
    
    title = "Choose Targets"
    modal = true
    
    import BorderPanel.Position._
    contents = new BorderPanel {
      
      layout(new BoxPanel(Orientation.Vertical) {
        border = Swing.EmptyBorder(5,5,5,5)

        contents += targetView
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
              setText(targetDescription(tvs.items(tvs.anchorIndex)))
            }
            refresh
          }
        }
        
      })) = East
      refresh
    }
    
    centerOn(MouseInfo.getPointerInfo().getLocation())
    open()

    
    private def refresh {
      peer.setSize(preferredSize)
      contents.foreach(_.revalidate)
      repaint
    }
    
    /**
     * Generate a nicely formatted descriptive text.
     */
    private def targetDescription(tar: NetworkedTarget) = {
      val sb = new StringBuilder
      sb.append(tar.name).append('\n')
      sb.append(tar.addr).append(':').append(tar.port).append('\n')
      sb.append('\n')
      sb.append(tar.capabilities.mkString(", ")).append('\n')
      sb.append('\n')
      sb.append(tar.description)
      sb.toString
    }
    
    private class Renderer extends JLabel with ListCellRenderer[NetworkedTarget] with Publisher {
      
      def getListCellRendererComponent(list:JList[_ <: NetworkedTarget], item: NetworkedTarget, index:Int, selected:Boolean, focus:Boolean) = {
        setText(item.name + " @" + item.addr + ":" + item.port)
        
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
  }
}