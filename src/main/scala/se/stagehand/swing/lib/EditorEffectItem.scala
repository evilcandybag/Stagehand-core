package se.stagehand.swing.lib

import se.stagehand.lib.scripting.Effect
import scala.swing.Component

trait EditorEffectItem[T <: Effect] extends Component with EffectNode[T]{
  visible = true
}