package snake

import scala.util.Random

class Apple extends Entity {

  private var _x = Random.nextInt(Main.level.boardSize)
  private var _y = Random.nextInt(Main.level.boardSize)
  while(Main.level.occupiedSquares.contains((_x,_y))) {
    _x = Random.nextInt(Main.level.boardSize)
    _y = Random.nextInt(Main.level.boardSize)
  }
  private var _width = .4
  private var _height = .4
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

}
