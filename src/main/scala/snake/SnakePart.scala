package snake

abstract class SnakePart extends Entity {
  def x:Int 
  def y:Int
  def width:Double
  def height:Double

  def createChild():Unit
  def makePassable() = new PassableEntity(x, y, width, height, EntityType.snakePart)
}
