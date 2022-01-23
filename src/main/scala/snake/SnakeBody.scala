package snake

class SnakeBody(parent:SnakePart, head:SnakeHead, val level:Level) extends SnakePart{
  val width = Settings.snakeWidth 
  val height = Settings.snakeHeight
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
      val child = new SnakeBody(this, head, level)
      head.setTail(child)
      level += child
      creatingChild = false
    }
  }
}
