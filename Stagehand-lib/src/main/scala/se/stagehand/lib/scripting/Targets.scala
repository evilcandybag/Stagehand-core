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
    _targets.foreach(_.run(runArgs))
  }
  
  /**
   * Generate run arguments for the targets.
   * Run argument format is name:value. One argument per index.
   */
  def runArgs: Target.Protocol.Arguments
}

/**
 * The abstract superclass for effect targets.
 */
abstract class Target(val name:String, val cap:Array[String], val desc:String) {
  import Target._
  
  def run(args:Protocol.Arguments)
  def callback(args:Protocol.Arguments)
}

object Target {
  object Protocol {
    type Arguments = Map[String,String]
    /**
     * List separator.
     */
    val SEPARATOR = ","
    /**
     * Key-value separator.
     */
    val KEY_VAL = ":"
      
    def encode(args:Arguments):String = {
      args.map(t => t._1 + KEY_VAL + t._2).mkString(SEPARATOR)
    }
    def decode(args:String):Arguments = {
      args.split(SEPARATOR).map(s => {
        val arg = s.split(KEY_VAL)
        if (arg.length == 2) {
          (arg(1) -> arg(2))
        } else {
          throw new SyntaxError("Syntax error when decoding string:\n" + args + "\n" + 
              "number of key-value separators in argument " + s + " must be exactly 1.")
        }
      }).toMap
    }
    
    class SyntaxError(s:String) extends Exception(s)
  }
}