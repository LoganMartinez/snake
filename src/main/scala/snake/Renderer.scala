package snake

import javafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

class Renderer(val gc:GraphicsContext) {
  val cellWidth = Settings.canvasWidth / Settings.boardSize.toDouble
  val cellHeight = Settings.canvasHeight / Settings.boardSize.toDouble

  def drawEntity(x:Int, y:Int, w:Double, h:Double) = {
    val widthOffset = (1 - (w % 1)) / 2
    val heightOffset = (1 - (h % 1)) / 2
    gc.fillRect((widthOffset + x) * cellWidth, (heightOffset + y) * cellHeight, w * cellWidth, h * cellHeight)
  }
  
  def render(level:PassableLevel) {
    for(row <- 0 to Settings.boardSize; col <- 0 to Settings.boardSize) {
      if((row + col) % 2 == 0) gc.setFill(Color.AQUAMARINE) else gc.setFill(Color.DARKCYAN)
      gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight)
    }
    for(PassableEntity(x,y,w,h,t) <- level.entities) {
      t match {
        case EntityType.snakePart => 
          gc.setFill(Color.CORAL)
          drawEntity(x,y,w,h)
        case EntityType.apple =>
          gc.setFill(Color.CRIMSON)
          drawEntity(x,y,w,h)
        case _ => 
      }
    }
  }
}