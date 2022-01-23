package snake

class SnakeHead(private var _x:Int, private var _y:Int, val level:Level) extends SnakePart {
  val width = Settings.snakeWidth
  val height = Settings.snakeHeight
  private var dir = 1
  private var nextDir = 1
  private var tail:SnakePart = this

  def x = _x 
  def y = _y

  def setDir(newDir:Int) = {
    require(newDir >= 0 && newDir <= 3)
    if((dir + newDir) % 2 == 1) nextDir = newDir
  }

  def createChild() = {
    tail = new SnakeBody(tail, this, level)
    level += tail
  }

  def setTail(newTail:SnakePart) = tail = newTail

  def reset() = { 
    _x = Settings.startX
    _y = Settings.startY 
    dir = 1
    tail = this
  }
  def update() = {
    val (oldX, oldY) = (x,y)
    dir = nextDir
    dir match {
      case 0 => _y -= 1
      case 1 => _x += 1
      case 2 => _y += 1
      case 3 => _x -= 1
      case _ => 
    }

    if(_x < 0) { _x + 1; Main.gameover() } 
    if(_y < 0) { _y + 1; Main.gameover() }
    if(_x >= Settings.boardSize) { _x - 1; Main.gameover() }
    if(_y >= Settings.boardSize) { _y - 1; Main.gameover() }

    for(entity <- level.entities) {
      if(entity.x == x && entity.y == y) {
        entity match {
          case snake:SnakePart if(snake != this) => Main.gameover()
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
