package snake

trait Entity {
  def x:Int
  def y:Int
  def width:Double
  def height:Double
  val level:Level

  def update():Unit
  def makePassable():PassableEntity
}
