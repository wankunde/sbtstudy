// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.collections

import com.wankun.FunSuiteBase

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-08-26.
 */
class CollectionFunction extends FunSuiteBase {

  test("test grouped of collection") {
    val s1 = Seq(1, 2, 3, 4, 5, 6, 7, 8)
    s1.grouped(2).toArray should equal(
      Array(
        Seq(1, 2),
        Seq(3, 4),
        Seq(5, 6),
        Seq(7, 8)
      )
    )
  }

  test("test parallel collections") {
    val a = Range(1, 10, 1).par
    a.foreach(i => {
      println(i)
      i
    })
  }

  case class Address(province: String, city: String)

  case class User(id: Int, name: String, address: Address)

  /**
   * 1. case class 自带copy方法
   * 2. copy方法不同于clone方法，copy方法在copy的过程中可以修改对象属性，copy过程中对于val类型的属性仍然是可以改变的
   * 3. copy方法不改变原来对象，需要使用一个新的对象接收copy的结果
   */
  test("test copy method") {
    val user1 = User(1, "wankun", Address("jiangsu", "xuzhou"))
    val user2 = user1.copy(address = Address("shanghai", "shanghai"))
    println(user1)
    println(user2)
  }
}
