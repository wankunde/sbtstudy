package scalatest

/**
 * case classes and pattern matching
 */


abstract class Tree

case class Sum(l: Tree, r: Tree) extends Tree

case class Var(n: String) extends Tree

case class Const(v: Int) extends Tree


object Tree {
  /**
   * type alias is a shorthand for a long function
   */
  type Environment = String => Int

  def eval(t: Tree, env: Environment): Int = t match {
    case Sum(l, r) => eval(l, env) + eval(r, env)
    case Var(n) => env(n)
    case Const(v) => v
  }

  /**
   * guard : follow the if keyword
   * wild-card : _ ,match any value
   * @param t : dest tree
   * @param v : replace string
   * @return
   */
  def derive(t: Tree, v: String): Tree = t match {
    case Sum(l, r) => Sum(derive(l, v), derive(r, v))
    case Var(n) if (n == v) => Const(1)
    case _ => Const(0)
  }

  def main(args: Array[String]) {
    // exp : (x + x) + (7+y)
    val exp: Tree = Sum(Sum(Var("x"), Var("x")), Sum(Const(7), Var("y")))
    val env: Environment = {
      case "x" => 5
      case "y" => 7
    }

    println("Expression : " + exp)
    println("evaluation with x=5,y=7:" + eval(exp, env))
    println("Derivative relative to x:\n " + derive(exp, "x"))
    println("Derivative relative to y:\n " + derive(exp, "y"))
  }
}
