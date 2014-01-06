package se.stagehand.swing.editor

import scala.swing._
import se.stagehand.lib.scripting._
import scala.actors.Actor
import javax.swing.JOptionPane
import scala.swing.BorderPanel.Position._
import se.stagehand.lib.Log


object EffectSelector {
  
  def pickEffect:Seq[Class[_]] = {
    val d = new EffectDialog(classOf[Effect])
    d.classes
  }
  def pickEffectsAsInstances = pickEffect.map(ID.newInstance[Effect](_))
  
 
  
  class EffectDialog(c: Class[_]) extends Dialog {
    private val log = Log.getLog(this.getClass())
    if (!classOf[StagehandComponent].isAssignableFrom(c))
      throw new IllegalArgumentException("Class needs to be a subclass of StagehandComponent!")
    
    private val effects = ID.getAllWithNames(c)
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

    centerOnScreen()
    open()
    
    
    
    private class DisplayWrapper(t: (Class[_],String)) {
      override def toString = t._2
      val cls = t._1
    }
    
  }
}
