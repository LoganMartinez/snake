package snake

import scalafx.Includes._
import scalafx.application.JFXApp
import java.net.Socket
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scalafx.scene.Scene
import scalafx.scene.input.KeyEvent
import scalafx.scene.canvas.Canvas
import scalafx.application.Platform

object Client extends JFXApp {
  private var started = false
  private var lost = false

  val sock = new Socket("192.168.1.227", 8080)
  val oos = new ObjectOutputStream(sock.getOutputStream)
  val ois = new ObjectInputStream(sock.getInputStream)
  implicit val ec: ExecutionContext = ExecutionContext.global

  val canvas = new Canvas(Settings.canvasWidth, Settings.canvasHeight)
  val gc = canvas.graphicsContext2D
  val renderer = new Renderer(gc)

  Future {
    while(true) {
      ois.readObject() match {
        case level:PassableLevel => 
          Platform.runLater(renderer.render(level))
        case _ => println("unknown type")
      }
    }
  }

  stage = new JFXApp.PrimaryStage {
    title = "snake"
    scene = new Scene(Settings.canvasWidth, Settings.canvasHeight) {
      content += canvas
      canvas.onKeyPressed() = (e:KeyEvent) => {
        if(!started) started = true
        e.code.toString match {
          case "W" => 
            oos.writeInt(KeyCode.UP)
            oos.flush()
          case "D" =>
            oos.writeInt(KeyCode.RIGHT)
            oos.flush()
          case "S" =>
            oos.writeInt(KeyCode.DOWN)
            oos.flush()
          case "A" =>
            oos.writeInt(KeyCode.LEFT)
            oos.flush()
          case "R" => 
          case _ => 
        }
      }
    }
  }
  canvas.requestFocus()
}
