package se.stagehand.swing.assets

import javax.swing.ImageIcon
import se.stagehand.lib.Log
import scala.swing.Dialog
import java.io.File
import java.net.URL
import se.stagehand.lib.FileManager

object ImageAssets {
  private val log = Log.getLog(this.getClass())
  
  
  
  
  /**
   * File references
   */
  val TARGET = new URL(FileManager.localResource("assets/target.png"))
  val BACKGROUND_PLACEHOLDER = new URL(FileManager.localResource("assets/bg-placeholder.png"))
  val FOREGROUND_PLACEHOLDER = new URL(FileManager.localResource("assets/orc_placeholder.png"))
  val PLUS = new URL(FileManager.localResource("assets/plus-4-16.png"))
  val TREE = new URL(FileManager.localResource("assets/deciduous-tree-16.png"))
  val NOTE = new URL(FileManager.localResource("assets/note-16.png"))
  val AUDIO = new URL(FileManager.localResource("assets/audio-16.png"))
  
  log.debug("PLUS: " + PLUS)
  
  /**
   * Online references.
   */
  val BG_WEB = new URL("http://stagehand.se/downloads/bg-placeholder.png")
  val FG_WEB = new URL("http://stagehand.se/downloads/orc_placeholder.png")
  
  /**
   * Image icon references
   */
  val TARGET_ICON = new ImageIcon(TARGET)
  val PLUS_ICON = new ImageIcon(PLUS)
  val TREE_ICON = new ImageIcon(TREE)
  val NOTE_ICON = new ImageIcon(NOTE)
  val AUDIO_ICON = new ImageIcon(AUDIO)
}