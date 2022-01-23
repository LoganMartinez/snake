package snake

import java.util.concurrent.LinkedBlockingQueue
import scala.concurrent.ExecutionContext
import java.net.ServerSocket
import scala.concurrent.Future
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.SocketException
import scala.util.Random

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
      var startX = Random.nextInt(Settings.boardSize)
      var startY = Random.nextInt(Settings.boardSize)
      while(level.occupiedSquares.contains((startX,startY))) {
        startX = Random.nextInt(Settings.boardSize)
        startY = Random.nextInt(Settings.boardSize)
      }
      val player = new SnakeHead(startX, startY, level)
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
      for(client@ConnectedClient(sock,ois,oos,player) <- clients) {
        try {
          if(ois.available() > 0) {
            val key = ois.readInt()
            player.start()
            key match {
              case KeyCode.UP => player.setDir(0)
              case KeyCode.RIGHT => player.setDir(1)
              case KeyCode.DOWN => player.setDir(2)
              case KeyCode.LEFT => player.setDir(3)
              case KeyCode.R =>
                if(level.entities.contains(player)) level.remove(player)
                player.reset()
                level += player
              case _ => 
            }
          }
          if(sendUpdate) oos.writeObject(level.makePassable)
        } catch {
          case e:SocketException => 
            val index = clients.indexOf(client)
            clients = clients.patch(index, List[ConnectedClient](), 1)
            level.remove(player)
            level.removeApple()
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
