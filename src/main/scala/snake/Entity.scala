package snake

trait Entity {
  def x:Int
  def y:Int
  def width:Double
  def height:Double

  def update()
}
