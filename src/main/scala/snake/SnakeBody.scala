package snake

class SnakeBody(parent:SnakePart, head:SnakeHead, val level:Level) extends SnakePart{
  val width = Settings.snakeWidth 
  val height = Settings.snakeHeight
  private var _x = -1
  private var _y = -1
  val color = parent.color
  private var nextX = parent.x
  private var nextY = parent.y
  private var creatingChild = false
  private var _child:Option[SnakePart] = None
  def x = _x 
  def y = _y
  def child = _child

  def createChild() = creatingChild = true

  def update() = {
    _x = nextX
    _y = nextY
    nextX = parent.x
    nextY = parent.y
    if(creatingChild) {
      val newChild = new SnakeBody(this, head, level)
      head.setTail(newChild)
      level += newChild
      _child = Some(newChild)
      creatingChild = false
    }
  }
}
