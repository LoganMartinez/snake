package snake

import java.util.concurrent.LinkedBlockingQueue
import scala.concurrent.ExecutionContext
import java.net.ServerSocket
import scala.concurrent.Future
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.SocketException

object Server extends App {
  private val clientQueue = new LinkedBlockingQueue[ConnectedClient]
  private var clients = List[ConnectedClient]()
  implicit val ec:ExecutionContext = ExecutionContext.global

  val level = new Level

  val connection = 8080
  val ss = new ServerSocket(connection)
  println("connected to socket " + connection)
  Future {
    while(true) {
      val sock = ss.accept()
      val ois = new ObjectInputStream(sock.getInputStream())
      val oos = new ObjectOutputStream(sock.getOutputStream())
      oos.flush()
      val player = new SnakeHead(Settings.startX, Settings.startY, level)
      clientQueue.put(ConnectedClient(sock, ois, oos, player))
    }
  }

  private var sendDelay = 0.0
  private var lastTime = -1L
  while(true) {
    while(!clientQueue.isEmpty()) {
      val cc = clientQueue.take()
      level += cc.player
      level += new Apple(level)
      clients ::= cc
    }
    val time = System.nanoTime()
    if(lastTime >= 0) {
      val delay = (time - lastTime) / 1e9
      sendDelay += delay
      val sendUpdate = sendDelay > Settings.updateSpeed
      for(ConnectedClient(sock,ois,oos,player) <- clients) {
        try {
          if(ois.available() > 0) {
            val key = ois.readInt()
            key match {
              case KeyCode.UP => player.setDir(0)
              case KeyCode.RIGHT => player.setDir(1)
              case KeyCode.DOWN => player.setDir(2)
              case KeyCode.LEFT => player.setDir(3)
              case _ => 
            }

          }
          if(sendUpdate) oos.writeObject(level.makePassable)
        } catch {
          case e:SocketException => 
            clients.filter(_.player != player)
            level.remove(player)
        }
      }
      if(sendUpdate) {
        level.update()
        sendDelay = 0
      }
    }
    lastTime = time 
  }

}
