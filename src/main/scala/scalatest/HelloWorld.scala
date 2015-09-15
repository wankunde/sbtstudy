package scalatest


class Hello {
  def sayHello(name: String) = s"hello,$name"
}

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello World")
  }
}

object HelloApp extends App {
  println("Hello scala app")
}

/**
 * Strng Interpolation : 字符串插值 ，将变量的引用直接嵌入到处理字符串的字面量(processed String literals)
 */
object StringInterpolation extends App {
  // test s
  val name = "James"
  println(s"Hello, $name") // Hello, James

  println(s"1 + 1 = ${1 + 1}")

  val age = 22
  println(s"$name is ${age + 2} years old.")

  // test f
  val height = 1.9d
  println(f"$name%s is $height%2.2f meters tail")

  // raw
  print(s"a\nb")
  print(raw"a\nb")

}