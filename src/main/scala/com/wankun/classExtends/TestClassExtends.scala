// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.classExtends

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-04-12.
 */
object TestClassExtends {
  def main(args: Array[String]): Unit = {
    val p: Person = new Student()
    println(p.getI)
    println(p.getSum()) // 这里和Java不一样，对varible 和function的访问都是就近访问
    println(p.getSum2())
  }
}

class Person {
  val i = 10

  def getI(): Int = i

  def getSum(): Int = i + 10

  def getSum2(): Int = getI() + 10
}

class Student extends Person {
  override val i = 20

  override def getI(): Int = i
}