package se.stagehand.lib.scripting

import scala.collection.immutable.ListSet
import se.stagehand.lib.Log

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
    log.debug("sArgs_ " + sourceArgs)
    targets.foreach(_.run(runArgs))
  }
  
  /**
   * The required capabilities for a service to serve this effect.
   */
  def requirements:Set[String]
  
  /**
   * The number of requirements for this Effect currently fulfilled by its Targets.
   * A requirement is considered fulfilled if at least one of the Effect's Targets
   * has that capability.
   */
  def fulfills:Int =
    (for (r <- requirements if targets.find(t => t.capabilities.contains(r) ).isDefined ) yield r).size
    
  /**
   * Sort out the Targets that fulfill one or more of this Effect's requirements.
   */
//  def validTargets(targets: Set[_ <: Target]):Set[_ <: Target] = targets.filter(t => {
//    t.capabilities.find(c => requirements.contains(c)).isDefined
//  })
  def validTargets(targets: Set[_ <: Target]):Set[_ <: Target] = {
    val ts = for (t <- targets) yield {
      val v = (t,t.capabilities.find(c => requirements.contains(c)).isDefined)
      log.debug("target: " + v )
      v._1.capabilities.foreach(x => log.debug("" + x))
      v
    }
    
    ts.filter(_._2).map(_._1)
  }
  
}

/**
 * The abstract superclass for effect targets.
 */
abstract class Target(val name:String, caps:Array[String], val description:String) {
  import Target._
  
  def run(args:Protocol.Arguments)
  def callback(args:Protocol.Arguments)
  def prettyName:String = name
  def prettyDescription:String = description
  
  def capabilities = caps
  
  /**
   * Two targets are equal IFF they have the same name.
   */
  override def equals(other:Any) = other match {
    case that: Target => this.getClass == that.getClass &&
      this.name == that.name
    case _ => false
  }
  override def hashCode() = {
    this.name.hashCode() * 7
  }
}

object Target {
  private var _targetSources: List[() => Set[Target]] = List()
  def addSource(fun: () => Set[Target]) = {
    _targetSources = fun :: _targetSources
  } 
  
  def allTargets = _targetSources.map(_()).flatten
  
  object Protocol {
    type Arguments = Map[String,String]
    /**
     * List separator.
     */
    val SEPARATOR = "\uE000"
    /**
     * Key-value separator.
     */
    val KEY_VAL = "\uE001"
    val KEY_KEY = "\uE002"
      
    def encode(args:Arguments):String = {
      args.map(t => t._1 + KEY_VAL + t._2).mkString(SEPARATOR)
    }
    def decode(args:String):Arguments = {
      args.split(SEPARATOR).map(s => {
        val arg = s.split(KEY_VAL)
        if (arg.length == 2) {
          (arg(0) -> arg(1))
        } else {
          throw new SyntaxError("Syntax error when decoding string:\n" + args + "\n" + 
              "number of key-value separators in argument " + s + " must be exactly 1.")
        }
      }).toMap
    }
    
    class SyntaxError(s:String) extends Exception(s)
  }
}