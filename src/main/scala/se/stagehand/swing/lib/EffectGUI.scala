package se.stagehand.swing.lib

import se.stagehand.plugins.ComponentGUI
import se.stagehand.lib.scripting.Effect

trait EffectGUI extends ComponentGUI {

  protected def checkEffect[T <: Effect](effect: Effect):T = {
    if (effect.getClass != peer)
      throw new IllegalArgumentException("Invalid effect type: " + effect.getClass.getName + ", expected: " + peer.getName)
    effect.asInstanceOf[T]
  }
  
  def editorItem(effect:Effect): EditorEffectItem[_]
  def playerItem(effect:Effect): PlayerEffectItem[_]
}