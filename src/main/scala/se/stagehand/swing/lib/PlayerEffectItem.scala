package se.stagehand.swing.lib

import se.stagehand.lib.scripting.Effect
import scala.swing.Component
import se.stagehand.lib.scripting.Targets
import scala.swing.GridBagPanel
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Button
import java.awt.Dimension
import se.stagehand.swing.assets.ImageAssets
import scala.swing.Action
import java.awt.Graphics2D
import java.awt.Color
import se.stagehand.swing.player.NetworkedTargetPicker
import se.stagehand.lib.Log
import scala.swing.Swing

trait PlayerEffectItem[T <: Effect] extends Component with EffectNode[T]{

}

abstract class TargetedPlayerEffectItem[T <: Effect with Targets](e:T) extends BoxPanel(Orientation.Horizontal) with PlayerEffectItem[T] {
  protected val log = Log.getLog(this.getClass())
  def effect = e
  
  border = Swing.EmptyBorder(2)
  
  /**
   * The actual PlayerEffectItem
   */
  def effectItem: Component
  val targetsButton = new TargetButton(effect)
  
  private var _eItem = effectItem
  _eItem.border = Swing.EmptyBorder(1)
  contents += _eItem
  contents += targetsButton
  
  class TargetButton(effect: Effect with Targets) extends Button {
    preferredSize = new Dimension(15,15)
    
    action = new Action("") {
      icon = ImageAssets.TARGET_ICON
      def apply {
        NetworkedTargetPicker.pickTargets(s => {
          effect.validTargets(s) 
        }, t => {
          effect.addTarget(t)
        }, effect.targets,
        t => {
          effect.removeTarget(t)
        })
      }
    }
    
    override def paint(g:Graphics2D) {
      val fulfills = effect.fulfills
      val requirements = effect.requirements.size
      
      //Set background colors depending on how many requirements are fulfilled
      if (fulfills >= requirements) {
        background = Color.GREEN
      } else if (fulfills > 0) {
        background = Color.YELLOW
      } else {
        background = Color.RED
      }
      this.peer.setSize(this.preferredSize)
      super.paint(g)
    }
    this.revalidate
    this.repaint
  }
  refresh
}