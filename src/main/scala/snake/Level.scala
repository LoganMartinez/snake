package snake

class Level {
  private var _entities = List[Entity]()

  def entities = _entities
  def += (entity:Entity) = _entities :+= entity
  def remove(entity:Entity):Unit = {
    entity match {
      case e:SnakePart => 
        e.child() match {
          case Some(ch) => remove(ch)
          case None => 
        }
      case _ => 
    }
    val index = _entities.indexOf(entity)
    _entities = _entities.patch(index, List[Entity](), 1)
    
  }
  def reset() = {
    _entities = _entities.filter( s => s == Main.snake ) //needs to be changed
    for(e <- _entities) e match {
      case h:SnakeHead => h.reset()
      case _ => 
    }
    +=(new Apple(this))
  }
  def occupiedSquares:Set[(Int,Int)] = {
    var ret = Set[(Int,Int)]()
    for(e <- entities) ret += ((e.x, e.y))
    return ret
  }
  def removeApple() = {
    val index = entities.indexWhere( e => 
      e match {
        case e:Apple => true
        case _ => false
      }
    )
    val lastIndex = entities.lastIndexWhere( e => 
      e match {
        case e:Apple => true
        case _ => false
      }
    )
    if(index >= 0 && index != lastIndex) _entities = _entities.patch(index, List[Entity](), 1) 
  }

  def makePassable() = new PassableLevel(_entities.map(e => e.makePassable))
  def update() = for(e <- _entities) e.update()
  
}
