package se.stagehand.lib

import java.io.OutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Calendar

object Log {
  object Mode extends Enumeration {
    val NoLog,NoDebug,Default = Value
  }
  
  private var _logs: Map[Class[_],Log] = Map()
  private var _out: PrintStream = System.out
  var mode = Mode.Default
  var showTimestamp = false
  private var _dateFormat = new SimpleDateFormat("HH:mm:ss")
  
  
  private def activeLog(cls:Class[_]) = {
    mode match {
      case Mode.Default => new Log(cls)
      case Mode.NoLog => NoLog
      case Mode.NoDebug => new NoDebug(cls)
    }
  }
  def timestamp = {
    _dateFormat.format(Calendar.getInstance.getTime)
  }
    
  def out_=(stream:PrintStream) = _out = stream
  def dateFormat_=(str:String) = {
    _dateFormat.applyPattern(str)
  }
  
  def getLog(cls:Class[_]): Log = {
    _logs.get(cls) match {
      case Some(l) => l
      case None    => {
        val log = activeLog(cls)
        _logs += (cls -> log)
        log
      }
    }
  }
  
  def out = _out
  
  private object NoLog extends Log(classOf[Any]) {
    override def debug(msg:String) {}
    override def log(msg:String) {}
    override def error(msg:String) {}
  }
  private class NoDebug(cls:Class[_]) extends Log(cls) {
    override def debug(msg:String) {}
  }
  
}

class Log(private val cls:Class[_]) {
  private val cname = {
    val nm = cls.getName
    val i = nm.lastIndexOf('.') + 1
    
    nm.substring(i, nm.length)
  }
  
  private def stamp = if (Log.showTimestamp) Log.timestamp + "-" else ""
  
  def debug(msg:String) = message("DEBUG", msg)
  def log(msg:String) = message("LOG", msg)
  def error(msg:String) = message("ERROR", msg)
  
  private def message(typ:String, msg:String) = Log.out.println("[" + stamp + typ + "-" + cname + "] " + msg)
}