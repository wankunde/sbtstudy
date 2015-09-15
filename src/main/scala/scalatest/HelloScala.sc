import scalatest._

/**
 * A worksheet is a scala file with .sc extension which you can run
 * and  get evaluation results in a special view appeared in the editor.
 */
val hello = new Hello
println(hello.sayHello("wankun"))
val xl = List(1, 2, 3, 4)
xl foreach println
for (i <- 1 to 7)
  println(i)