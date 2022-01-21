package snake

class SnakeBody(parent:Entity) extends SnakePart{
  val width = Main.snakeWidth 
  val height = Main.snakeHeight
  private var _x = -1
  private var _y = -1
  private var nextX = parent.x
  private var nextY = parent.y
  private var creatingChild = false
  def x = _x 
  def y = _y

  def createChild() = creatingChild = true

  def update() = {
    _x = nextX
    _y = nextY
    nextX = parent.x
    nextY = parent.y
    if(creatingChild) {
      val child = new SnakeBody(this)
      Main.snake.setTail(child)
      Main.level += child
      creatingChild = false
    }
  }
}
