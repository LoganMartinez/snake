package snake

import scala.util.Random

class Apple(val level:Level) extends Entity {

  private var _x = Random.nextInt(Settings.boardSize)
  private var _y = Random.nextInt(Settings.boardSize)
  while(level.occupiedSquares.contains((_x,_y))) {
    _x = Random.nextInt(Settings.boardSize)
    _y = Random.nextInt(Settings.boardSize)
  }
  private var _width = Settings.appleWidth
  private var _height = Settings.appleHeight
  val animationSpeed = 4
  private var animationTimer = animationSpeed

  def x = _x
  def y = _y
  def width = _width 
  def height = _height



  def update() = {
    if(animationTimer <= 0) {
      if(width > .4) {
        _width -= .1 
        _height -= .1
      } else {
        _width += .1
        _height += .1
      }
      animationTimer = animationSpeed
    } else animationTimer -= 1
  }

  def makePassable():PassableEntity = new PassableEntity(x, y, width, height, EntityType.apple)

}
