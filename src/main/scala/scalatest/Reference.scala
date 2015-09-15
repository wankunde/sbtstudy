package scalatest

/**
 * Author : wankun
 * Date : 2015/9/11 10:23
 */
class Reference[T] {
  // variables _ : represents a default value
  private var contents: T = _

  def set(value: T) {
    contents = value
  }

  def get(): T = contents
}


object IntegerReference {
  def main(args: Array[String]) {
    val cell = new Reference[Int]
    cell.set(13)
    println("Reference contains the half of " + (cell.get * 2))
  }
}