package snake

import scalafx.scene.paint.Color

object Settings {
  val canvasWidth:Double = 800.0
  val canvasHeight:Double = 800.0
  val boardSize:Int = 20
  val snakeWidth:Double = .8
  val snakeHeight:Double = .8
  val appleWidth:Double = .4
  val appleHeight:Double = .4
  val updateSpeed:Double = .15
  val startX:Int = 2
  val startY:Int = 2
  val colors = Array(Color.CRIMSON, Color.rgb(116,82,80), Color.rgb(154,94,92), Color.rgb(231,202,191), Color.rgb(217,130,136), Color.rgb(233,160,147))
  val wrapAround = true
}
