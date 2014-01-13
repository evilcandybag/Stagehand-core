package se.stagehand.lib.scripting.network

import se.stagehand.lib.scripting._
import javax.jmdns.ServiceListener
import javax.jmdns.ServiceEvent
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import java.net.InetAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.InetSocketAddress
import se.stagehand.lib.Log
import akka.io.{IO,Tcp}
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.ByteString



abstract class NetworkedEffect(id:Int) extends Effect(id) with Targets {
  def this() = this(ID.unique)
  if (!NetworkedEffect.started) NetworkedEffect.start  
}

class NetworkedTarget(name:String, val addr:InetAddress, val port:Int, cap:Array[String], desc:String) extends Target(name,cap,desc) with Actor {
  private val log = Log.getLog(this.getClass())
  
  import Tcp._
  import context.system
  import Target._
  
  
  val io = IO(Tcp)
  private val socketAddress = new InetSocketAddress(addr,port)
  
  def receive = {
    case c @ Connected(remote,local) => {
      val connection = sender
      connection ! Register(self)
      context become {
        case data: ByteString ⇒ connection ! Write(data)
        case CommandFailed(w: Write) ⇒ // O/S buffer was full
        case Received(data) ⇒  callback(Protocol.decode(data.decodeString("UTF-8")))
        case "close" ⇒ connection ! Close
        case _: ConnectionClosed ⇒ context stop self
      }
      
    }
  }
  
  def connect {
    io ! Connect(socketAddress)
  }
  
  def run(args:Protocol.Arguments) {
    self ! ByteString.apply(Protocol.encode(args))
  }
  def callback(args:Protocol.Arguments) {
    log.debug("Received callback with args " + args)
  }
  
  override def toString = "NetworkedTarget{" + name + ", " + addr + ": " + port + ", cap: [" + cap.mkString(", ") + "]}"
}

object NetworkedTarget {
  def fromServiceInfo(info:ServiceInfo) = {
    val name = info.getName
    val address = info.getAddress
    val capabilities = info.getPropertyString(NetworkedEffect.CAPABILITIES).split(",")
    val description = info.getPropertyString(NetworkedEffect.DESCRIPTION)
    val port = info.getPort
    
    new NetworkedTarget(name,address, port ,capabilities,description)
  }
}

/**
 * Handles the detection of active EffectServers and creates Target objects 
 */
object NetworkedEffect {
  private val log = Log.getLog(this.getClass)
  
  val MDNS_SERVICE_TYPE = "_stagehand._tcp.local."
  val CAPABILITIES = "capabilities"
  val DESCRIPTION = "description"
    
      
  private var mdnsService:JmDNS = null
  private var mdnsServiceListener:ServiceListener = null
  private var _started = false
  
  
  private class StagehandServiceListener extends ServiceListener {
		override def serviceAdded(serviceEvent: ServiceEvent ) {
			// Test service is discovered. requestServiceInfo() will trigger serviceResolved() callback.
			mdnsService.requestServiceInfo(MDNS_SERVICE_TYPE, serviceEvent.getName());
			println("serviceAdded")
		}

		override def serviceRemoved(serviceEvent:ServiceEvent ) {
			// Test service is disappeared.
		    println("serviceRemoved")
		}

		override def serviceResolved(serviceEvent: ServiceEvent) {
			// Test service info is resolved.
			val serviceUrl = serviceEvent.getInfo().getURL();
			// serviceURL is usually something like http://192.168.11.2:6666/my-service-name
			println("serviceResolved")
		}
	}
  
  /**
   * Start searching the network for Stagehand services.
   */
  def start {
    mdnsService = JmDNS.create();
    mdnsServiceListener = new StagehandServiceListener
    mdnsService.addServiceListener(MDNS_SERVICE_TYPE, mdnsServiceListener)
    _started = true
    log.debug("Started listening for services.")
  }
  def started = _started
  
  /**
   * Get available Stagehand services as 
   */
  def targets = {
    val list = mdnsService.list(MDNS_SERVICE_TYPE)
    
    list.map(NetworkedTarget.fromServiceInfo(_))
  }  
  
}