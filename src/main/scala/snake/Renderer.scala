package snake

import javafx.scene.paint.Color

class Renderer {
  val gc = Main.gc
  val level = Main.level
  val cellWidth = Main.canvasWidth / level.boardSize.toDouble
  val cellHeight = Main.canvasHeight / level.boardSize.toDouble

  def drawEntity(entity:Entity) = {
    val widthOffset = (1 - (entity.width % 1)) / 2
    val heightOffset = (1 - (entity.height % 1)) / 2
    gc.fillRect((widthOffset + entity.x) * cellWidth, (heightOffset + entity.y) * cellHeight, entity.width * cellWidth, entity.height * cellHeight)
  }
  
  def render() {
    for(row <- 0 to level.boardSize; col <- 0 to level.boardSize) {
      if((row + col) % 2 == 0) gc.setFill(Color.AQUAMARINE) else gc.setFill(Color.DARKCYAN)
      gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight)
    }
    for(entity <- level.entities) {
      entity match {
        case snake:SnakePart => 
          gc.setFill(Color.CORAL)
          drawEntity(snake)
        case apple:Apple =>
          gc.setFill(Color.CRIMSON)
          drawEntity(apple)
        case _ => 
      }
    }
  }
}