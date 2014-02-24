package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.lib.scripting._
import scala.actors.Actor
import javax.swing.JOptionPane
import scala.swing.BorderPanel.Position._
import se.stagehand.lib.Log
import se.stagehand.swing.gui.BetterDialog
import java.awt.MouseInfo


object EffectSelector {
  
  def pickEffect(filter: (Class[_]) => Boolean):Seq[Class[_]] = {
    val d = new EffectDialog(classOf[Effect],filter)
    d.classes
  }
  
  /**
   * Filter is an optional parameter to sort out stuff you don't want.
   */
  def pickEffectsAsInstances(filter: (Class[_]) => Boolean = (c) => true) = pickEffect(filter).map(ID.newInstance[Effect](_))
  
  
  class EffectDialog(c: Class[_],filter: (Class[_]) => Boolean) extends BetterDialog {
    if (!classOf[StagehandComponent].isAssignableFrom(c))
      throw new IllegalArgumentException("Class needs to be a subclass of StagehandComponent!")
    
    private val effects = ID.getAllWithNames(c).filter(x => filter(x._1))
    private val effectsview = new ListView(effects.map(new DisplayWrapper(_)))
    private var _classes = Seq[Class[_]]()
  
    def classes = _classes
    
    title = "Pick effects"
    modal = true
    
    
    
    private def items = effectsview.selection.items.map(_.cls).toSeq
     
    contents = new BorderPanel {
      layout(new BoxPanel(Orientation.Vertical) {
        border = Swing.EmptyBorder(5,5,5,5)

        contents += effectsview
        log.debug("effects: " + effects.length)
      }) = Center

      layout(new FlowPanel(FlowPanel.Alignment.Right)(
        Button("Select") {
          _classes = items
          close()
        }
      )) = South
    }

    centerOn(MouseInfo.getPointerInfo().getLocation())
    open()
    
    
    
    private class DisplayWrapper(t: (Class[_],String)) {
      override def toString = t._2
      val cls = t._1
    }
    
  }
}
