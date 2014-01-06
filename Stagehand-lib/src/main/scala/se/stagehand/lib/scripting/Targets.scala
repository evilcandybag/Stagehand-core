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
}

/**
 * The abstract superclass for effect targets.
 */
abstract class Target {
  
}