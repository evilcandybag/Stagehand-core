package se.stagehand.swing.assets

import java.io.File
import java.net.URL

object AudioAssets {
  val SOUND_EFFECT_PLACEHOLDER = new File("assets/arr.wav")
  val BACKGROUND_MUSIC_PLACEHOLDER = new File("assets/anticipation.ogg")
  
  
  val SFX_WEB = new URL("http://stagehand.se/downloads/arr.wav")
  val BGM_WEB = new URL("http://stagehand.se/downloads/EntertheWoods.mp3")
}