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
import akka.io.Tcp.Write
import se.stagehand.lib.scripting.Target
import akka.actor.ActorSystem
import scala.collection.JavaConversions
import java.util.HashMap
import java.net.NetworkInterface
import java.net.Inet4Address

/**
 * Abstract class for an effect servers connection handling. Must be extended as an object to work properly.
 */
abstract class EffectServer {
  protected val log = Log.getLog(this.getClass())
  
 
  
  /**
   * Name of the EffectServer
   */
  def defaultName:String
  
  def name:String = _name
  
  
  
  /**
   * Property values of this EffectServer
   */
  def properties: Map[String,String]
  /**
   * Port used for connections. Must not be 0.
   */
  def port:Int = _port
  
  def stop {
    serverActor ! Halt 
    worker.stop
  }
  
  /**
   * The actor performing the actual 
   */
  val worker:AbstractWorker
  
  var _name = defaultName
  private var _port = 1337
  private val localIP = getLocalIP
  private val mdnsServer = JmDNS.create(localIP)
  private var mdnsInfo:ServiceInfo = null
  
  val system = ActorSystem("EffectServer")
  private val serverActor = system.actorOf(Props(new Actor {
    import Tcp._
    import context.system
    
    implicit val _system = context.system
    private var attempts = 0
    private var retries = 3
    
    IO(Tcp) ! Bind(self, new InetSocketAddress(localIP, port))
    
    def receive = {
	    case b @ Bound(address) => {
	      log.log("Bound server " + name + " at " + address.getAddress + ":" + address.getPort)
	      mdnsInfo = ServiceInfo.create(NetworkedEffect.MDNS_SERVICE_TYPE, name, port + attempts, 1,1, propertyMap(properties))
	      mdnsServer.registerService(mdnsInfo)
	      log.log("Started mDNS-server with config: " + mdnsInfo)
	    }
	    
	    case CommandFailed(b:Bind) => {
	      attempts += 1
	      log.log("Failed to bind service " + name + " at " + b.localAddress.getAddress + ":" + b.localAddress.getPort() )
	      if (attempts < retries) {
	        log.log("Retrying at " + b.localAddress.getAddress() + ":" + (b.localAddress.getPort() + 1) + " (attempt " + attempts + ")" )
	        IO(Tcp) ! Bind(self, new InetSocketAddress(b.localAddress.getAddress(), b.localAddress.getPort() + 1))
	      } else {
	        log.log("Giving up and exiting after " + attempts +  " retries.")
	        context stop self
	        System.exit(0)
	      }
	    }
	    
	    case c @ Connected(remote, local) => {
	      val connection = sender
	      log.log("Connected to client at: " + remote.getAddress() + ":" + remote.getPort())
	      connection ! Register(worker.workerRef)
	    }
	    
	    case Halt => {
	      context stop self
	    }
    }
  }),"SERVER-Connections")
  
  private def getLocalIP:InetAddress = {
    var ias = List[InetAddress]()
    var ni = NetworkInterface.getNetworkInterfaces() 
    while (ni.hasMoreElements()) {
      var ia = ni.nextElement().getInetAddresses() 
      while (ia.hasMoreElements()) {
        val i = ia.nextElement()
        ias = i :: ias
      }
    }
    var addr = InetAddress.getLocalHost()
    ias.foreach(_ match {
      case i:Inet4Address if !i.isLoopbackAddress() => addr = i
      case _ => {}
    })
    addr
  }
  
  /**
   * Convert a Scala Map to a java.util.Map
   */
  private def propertyMap(map:Map[String,String]): java.util.Map[String,String] = {
    var result = new HashMap[String,String]
    map.foreach(kv => result.put(kv._1,kv._2))
    result
  }
  
  def main(args:Array[String]) {
    if (args.length > 0) _name = args(0)
  }  
}

/**
 * Abstract worker 
 */
abstract class AbstractWorker(boss: EffectServer) {
  protected val log = Log.getLog(this.getClass)
  import Target._
  
  var replyTo: Option[ActorRef] = None
  
  /**
   * Reference to the internal actor for this worker
   */
  lazy val workerRef = boss.system.actorOf(Props(new Actor {
      import Tcp._
      import context.system
	  
      def receive = {
	    case r @ Received(data) => {
	      //assume for now that a server serves only one connection.
	      replyTo match {
	        case None => replyTo = Some(sender)
	        case Some(_) => {}
	      }
	      received(Protocol.decode(data.decodeString("UTF-8")))
	    }
	    case data:ByteString => {
	      replyTo match {
	        case Some(arf) => arf ! Write(data)
	        case None => log.log("Attempting to send data " + data + " to nonexistent receiver")
	      }
	    }
	    case Halt => context stop self
	    case m => {
	      log.error("Received something unknown to us: " + m)
	    }
	  }
  }), "SERVER-WORKER")
  
  /**
   * Defines what to do with received data. 
   */
  def received(args: Protocol.Arguments)
  def respond(args: Protocol.Arguments) {
    workerRef ! Protocol.encode(args)
  }
  
  def stop {
    workerRef ! Halt
  }
}

case class Halt
