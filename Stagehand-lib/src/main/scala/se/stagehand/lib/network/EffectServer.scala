package se.stagehand.lib.network

import javax.jmdns.JmDNS
import java.net.InetAddress
import javax.jmdns.ServiceInfo
import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.Log
import java.net.ServerSocket
import akka.io.{IO,Tcp}
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.ByteString
import java.net.InetSocketAddress


/**
 * Abstract class for an effect servers connection handling. Must be extended as an object to work properly.
 */
abstract class EffectServer extends Actor {
  protected val log = Log.getLog(this.getClass())
  import Tcp._
  import context.system
  /**
   * Name of the EffectServer
   */
  def name:String
  /**
   * Property values of this EffectServer
   */
  def properties: java.util.Map[String,String]
  /**
   * Port used for connections. Must not be 0.
   */
  def port:Int
  
  /**
   * The actor performing the actual 
   */
  val worker:AbstractWorker
  
  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", port))
  
  private val mdnsServer = JmDNS.create(InetAddress.getLocalHost)
  private val mdnsInfo = ServiceInfo.create(NetworkedEffect.MDNS_SERVICE_TYPE, name, port, 1,1,properties)
  
  def receive = {
    case b @ Bound(address) => {
      
    }
    
    case CommandFailed(b:Bind) => {
      log.log("Failed to bind service " + name + " at " + b.localAddress.getAddress + ":" + port )
      context stop self
    }
    
    case c @ Connected(remote, local) => {
      val connection = sender
      connection ! Register(worker.self)
    }
  }
  
  def main(args:Array[String]) {
    mdnsServer.registerService(mdnsInfo)
    log.log("Started mDNS-server with config: " + mdnsInfo)
  }
}

/**
 * Abstract worker 
 */
abstract class AbstractWorker extends Actor