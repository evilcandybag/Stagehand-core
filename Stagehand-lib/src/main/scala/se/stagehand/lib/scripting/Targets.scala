package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet

trait Targets extends Effect {
  private var _targets:ListSet[Target] = ListSet()
  
  def addTarget(tar: Target) {
    _targets = _targets + tar
  }
  
  def removeTarget(tar: Target) {
    _targets = _targets - tar
  }
  
  def targets = _targets
  
  def trigger {
    _targets.foreach(_.run(runArgs:_*))
  }
  
  /**
   * Generate run arguments for the targets.
   * Run argument format is name:value. One argument per index.
   */
  def runArgs: Array[String]
}

/**
 * The abstract superclass for effect targets.
 */
abstract class Target(val name:String, val cap:Array[String], val desc:String) {
  
  def run(args:String*)
  def callback(args:String*)
}