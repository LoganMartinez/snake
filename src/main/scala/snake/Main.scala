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
  private var started = false
  private var lost = false

  val canvas = new Canvas(Settings.canvasWidth, Settings.canvasHeight)
  val gc = canvas.graphicsContext2D
  val level = new Level
  val renderer = new Renderer(gc)
  val snake = new SnakeHead(Settings.startX, Settings.startY, level)

  level += snake
  level += new Apple(level)

  def gameover() = {
    // lost = true
    // gc.setFill(Color.BLACK)
    // gc.setFont(Font.font("Verdana", 100))
    // gc.fillText("You Lose", 0,100)
  }

  stage = new JFXApp.PrimaryStage {
    title = "snake"
    scene = new Scene(Settings.canvasWidth, Settings.canvasHeight) {
      content += canvas
      renderer.render(level.makePassable)
      var lastTime = -1L
      var updateTime = Settings.updateSpeed
      val timer = AnimationTimer { time => 
        if(started && !lost) {
          if (lastTime > 0) {
            val delay = (time - lastTime) / 1e9
            renderer.render(level.makePassable)
            updateTime -= delay
            if(updateTime <= 0) {
              for(entity <- level.entities) entity.update()
              updateTime = Settings.updateSpeed
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
        renderer.render(level.makePassable)
        lost = false
        started = false
      }
      case _ => 
    }
  }
    

  canvas.requestFocus();

}