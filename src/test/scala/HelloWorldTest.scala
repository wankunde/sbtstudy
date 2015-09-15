import org.scalatest.FunSuite

import scalatest.Hello

/**
 * Author : wankun
 * Date : 2015/9/11 19:25
 */
class HelloWorldTest extends FunSuite {

  test("sayhello method works correctly") {
    val hello = new Hello
    assert(hello.sayHello("wankun") == "hello,wankun")
  }
}
