package se.stagehand.swing.assets

import javax.swing.ImageIcon
import se.stagehand.lib.Log
import scala.swing.Dialog
import java.io.File

object ImageAssets {
  private val log = Log.getLog(this.getClass())
  
  
  /**
   * File references
   */
  val TARGET = new File("assets/target.png")
  val BACKGROUND_PLACEHOLDER = new File("assets/bg-placeholder.png")
  
  /**
   * Image icon references
   */
  val TARGET_ICON = new ImageIcon(TARGET.getPath())
}