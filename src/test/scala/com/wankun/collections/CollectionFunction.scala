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
}
