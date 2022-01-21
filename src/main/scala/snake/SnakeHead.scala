package snake

class SnakeHead(private var _x:Int, private var _y:Int) extends SnakePart {
  val width = Main.snakeWidth
  val height = Main.snakeHeight
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
    tail = new SnakeBody(tail)
    Main.level += tail
  }

  def setTail(newTail:SnakePart) = tail = newTail

  def reset() = { 
    _x = Main.startX
    _y = Main.startY 
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
    if(_x >= Main.level.boardSize) { _x - 1; Main.gameover() }
    if(_y >= Main.level.boardSize) { _y - 1; Main.gameover() }

    for(entity <- Main.level.entities) {
      if(entity.x == x && entity.y == y) {
        entity match {
          case snake:SnakePart if(snake != this) => Main.gameover()
          case apple:Apple => 
            Main.level.remove(apple)
            tail.createChild()
            Main.level += new Apple
          case _ =>
        }
      }
    }
  }


}
