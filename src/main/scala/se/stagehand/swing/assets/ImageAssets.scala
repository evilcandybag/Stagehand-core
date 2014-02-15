package se.stagehand.swing.assets

import javax.swing.ImageIcon
import se.stagehand.lib.Log
import scala.swing.Dialog
import java.io.File
import java.net.URL

object ImageAssets {
  private val log = Log.getLog(this.getClass())
  
  
  /**
   * File references
   */
  val TARGET = new File("assets/target.png")
  val BACKGROUND_PLACEHOLDER = new File("assets/bg-placeholder.png")
  val FOREGROUND_PLACEHOLDER = new File("assets/orc_placeholder.png")
  val PLUS = new File("assets/plus-4-16.png")
  val TREE = new File("assets/deciduous-tree-16.png")
  val NOTE = new File("assets/note-16.png")
  val AUDIO = new File("assets/audio-16.png")
  
  /**
   * Online references.
   */
  val BG_WEB = new URL("http://stagehand.se/downloads/bg-placeholder.png")
  val FG_WEB = new URL("http://stagehand.se/downloads/orc_placeholder.png")
  
  /**
   * Image icon references
   */
  val TARGET_ICON = new ImageIcon(TARGET.getPath())
  val PLUS_ICON = new ImageIcon(PLUS.getPath())
  val TREE_ICON = new ImageIcon(TREE.getPath())
  val NOTE_ICON = new ImageIcon(NOTE.getPath())
  val AUDIO_ICON = new ImageIcon(AUDIO.getPath())
}