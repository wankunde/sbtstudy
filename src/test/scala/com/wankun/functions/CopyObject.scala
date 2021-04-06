// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.functions

import com.wankun.FunSuiteBase

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-04-06.
 */
case class Student(name: String, age: Int)

class CopyObject extends FunSuiteBase {

  test("test copy method of object") {
    val kun = Student("kun", 30)
    val king = kun.copy("king", 100)
    val kong = kun.copy(name = "kong")
    kun shouldBe Student("kun", 30)
    king shouldBe Student("king", 100)
    kong shouldBe Student("kong", 30)
  }

}
