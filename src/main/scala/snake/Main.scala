package snake

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import scalafx.scene.Scene
import scalafx.animation.AnimationTimer
import scalafx.scene.input.KeyEvent
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

object Main extends JFXApp {
  val canvasWidth = 800.0
  val canvasHeight = 800.0
  val snakeWidth = .8
  val snakeHeight = .8
  val updateSpeed = .2
  val startX = 2
  val startY = 2
  private var started = false
  private var lost = false

  val canvas = new Canvas(canvasWidth, canvasHeight)
  val gc = canvas.graphicsContext2D
  val level = new Level
  val renderer = new Renderer
  val snake = new SnakeHead(startX, startY)

  level += snake
  level += new Apple

  stage = new JFXApp.PrimaryStage {
    title = "snake"
    scene = new Scene(canvasWidth, canvasHeight) {
      content += canvas
      renderer.render()
      var lastTime = -1L
      var updateTime = updateSpeed
      val timer = AnimationTimer { time => 
        if(started && !lost) {
          if (lastTime > 0) {
            val delay = (time - lastTime) / 1e9
            renderer.render()
            updateTime -= delay
            if(updateTime <= 0) {
              for(entity <- level.entities) entity.update()
              updateTime = updateSpeed
            }
          }
        }
        lastTime = time
      }
      timer.start()
    }
  }
  canvas.onKeyPressed() = (e:KeyEvent) => {
    if(!started) started = true
    e.code.toString match {
      case "W" if !lost => snake.setDir(0)
      case "D" if !lost => snake.setDir(1)
      case "S" if !lost => snake.setDir(2)
      case "A" if !lost => snake.setDir(3)
      case "R" => {
        level.reset()
        renderer.render()
        lost = false
        started = false
      }
      case _ => 
    }
  }
    

  canvas.requestFocus();

  def gameover() = {
    lost = true
    gc.setFill(Color.BLACK)
    gc.setFont(Font.font("Verdana", 100))
    gc.fillText("You Lose", 0,100)
  }
}