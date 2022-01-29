package snake

import scala.util.Random

class SnakeHead(private var _x:Int, private var _y:Int, val level:Level) extends SnakePart {
  val width = Settings.snakeWidth
  val height = Settings.snakeHeight
  private var dir = 1
  private var nextDir = 1
  private var tail:SnakePart = this
  private var _child:Option[SnakePart] = None
  private var started = false
  val color = Random.nextInt(Settings.colors.length - 1) + 1

  def x = _x 
  def y = _y
  def child = _child

  def setDir(newDir:Int) = {
    require(newDir >= 0 && newDir <= 3)
    if((dir + newDir) % 2 == 1 || tail == this) nextDir = newDir
  }
  
  def start() = started = true

  def createChild() = {
    tail = new SnakeBody(tail, this, level)
    level += tail
    _child = Some(tail)
  }

  def setTail(newTail:SnakePart) = tail = newTail

  def reset() = { 
    var startX = Random.nextInt(Settings.boardSize)
    var startY = Random.nextInt(Settings.boardSize)
    while(level.occupiedSquares.contains((startX,startY))) {
      startX = Random.nextInt(Settings.boardSize)
      startY = Random.nextInt(Settings.boardSize)
    }
    _x = startX
    _y = startY
    dir = 1
    tail = this
    _child = None
    started = false
  }
  def update() = {
    if(started) {
      val (oldX, oldY) = (x,y)
      dir = nextDir
      dir match {
        case 0 => _y -= 1
        case 1 => _x += 1
        case 2 => _y += 1
        case 3 => _x -= 1
        case _ => 
      }

      if(Settings.wrapAround) {
        if(_x < 0) _x = Settings.boardSize - 1
        if(_y < 0) _y = Settings.boardSize - 1
        if(_x >= Settings.boardSize) _x = 0
        if(_y >= Settings.boardSize) _y = 0 
      } else {
        if(_x < 0) { _x + 1; level.remove(this) } 
        if(_y < 0) { _y + 1; level.remove(this) }
        if(_x >= Settings.boardSize) { _x - 1; level.remove(this) }
        if(_y >= Settings.boardSize) { _y - 1; level.remove(this) }
      }
      

      for(entity <- level.entities) {
        if(entity.x == x && entity.y == y) {
          entity match {
            case snake:SnakePart if(snake != this) => level.remove(this)
            case apple:Apple => 
              level.remove(apple)
              tail.createChild()
              level += new Apple(level)
            case _ =>
          }
        }
      }
    }
  }


}
