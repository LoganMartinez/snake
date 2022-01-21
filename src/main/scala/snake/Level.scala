package snake

class Level {
  val boardSize = 10
  private var _entities = List[Entity]()

  def entities = _entities
  def += (entity:Entity) = _entities :+= entity
  def remove(entity:Entity) = {
    val index = _entities.indexOf(entity)
    _entities = _entities.patch(index, List[Entity](), 1)
  }
  def reset() = {
    _entities = _entities.filter( s => s == Main.snake )
    for(e <- _entities) e match {
      case h:SnakeHead => h.reset()
      case _ => 
    }
    +=(new Apple)
  }
  def occupiedSquares:Set[(Int,Int)] = {
    var ret = Set[(Int,Int)]()
    for(e <- entities) ret += ((e.x, e.y))
    return ret
  }
}
