package se.stagehand.swing.lib

import se.stagehand.lib.scripting.ScriptComponent
import scala.swing.Component
import scala.xml.Node
import scala.swing.BorderPanel
import se.stagehand.lib.scripting.StagehandComponent
import se.stagehand.lib.scripting.Effect
import java.awt.Dimension
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Button
import scala.swing.Action
import se.stagehand.swing.assets.ImageAssets
import scala.swing.Panel
import se.stagehand.swing.editor.EffectSelector
import se.stagehand.lib.scripting.ID

trait ComponentNode[+T <: StagehandComponent] extends Component {
  def component: T
  
  def refresh {
    peer.setSize(preferredSize)
    revalidate
    repaint
  }
  
  /**
   * Removes this ComponentNode and its StagehandComponent from all managers.
   * For StagehandComponents that contain other StagehandComponents, this needs to be overriden to properly deal with the children.
   */
  def remove {
    GUIManager.unregister(this)
    ID.remove(component)
  }
  
  class ContentPanel(name:String) extends BoxPanel(Orientation.Vertical) {
    override def toString = name + "\n\t" + contents.map(_.toString).mkString("\n\t")
  }
}

/**
 * Button class to be used to add effects to various things.
 */
class AddEffectsButton(gui: ComponentNode[_], pan: Panel, addMethod: (EditorEffectItem[_], Effect) => Unit, filter: (Class[_]) => Boolean) extends Button {
    def this(g:ComponentNode[_], p: Panel, aM: (EditorEffectItem[_], Effect) => Unit) = this(g,p,aM,(c) => true)
    preferredSize = new Dimension(20,20)
    
    action = new Action("") {
      icon = ImageAssets.PLUS_ICON
      def apply {
        val efs = EffectSelector.pickEffectsAsInstances(filter)
        
        efs.foreach(x => {
          val gui = GUIManager.getGUI[EffectGUI](x.getClass).editorItem(x)
          addMethod(gui,x)
          pan.repaint
          pan.revalidate
        })
        
		  gui.refresh
      }
    }
}
    
trait ScriptNode[+T <: ScriptComponent] extends ComponentNode[T]{
  def component = script
  
  def script: T
  
  def locationXML: Node = 
	  <node>
		{script.idXML}
		<locx>{this.location.x}</locx>
		<locy>{this.location.y}</locy>
	  </node>
}

trait EffectNode[T <: Effect] extends ComponentNode[T] {
  def component = effect
  def effect: T
} 