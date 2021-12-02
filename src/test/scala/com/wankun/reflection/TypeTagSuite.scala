/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wankun.reflection

import com.wankun.FunSuiteBase

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{TypeRef, TypeTag, typeOf, typeTag, weakTypeTag}

/**
 * Reference: https://www.baeldung.com/scala/type-information-at-runtime
 */
class TypeTagSuite extends FunSuiteBase {

  val intSeq: Seq[Int] = Seq(1, 2, 3)
  val intList: List[Int] = List(1, 2, 3)
  val strList: List[String] = List("foo", "bar")

  def genericTypeMatch[A](xs: List[A]): String = xs match {
    case _: List[String] => "List of Strings"
    case _: List[Int] => "List of Ints"
  }

  def genericTypeMatch2[T: ClassTag](o: Any): String = {
    o match {
      case x: T => "Type matched"
      case _ => "Type not matched"
    }
  }

  test("test type erasure") {
    // 1. type will erasure
    // every type parameter has been removed. Therefore, List[Int] and List[String]
    // are considered the same.
    genericTypeMatch(strList) shouldBe "List of Strings"
    // 这里检测不出来类型
    genericTypeMatch(intList) shouldBe "List of Strings"
    genericTypeMatch2[List[Int]](List(1, 2, 3)) shouldBe "Type matched"
    // 这里检测不出来类型
    genericTypeMatch2[List[String]](List(1, 2, 3)) shouldBe "Type matched"
  }

  /* 函数自动根据parameter 确定类型，然后根据类型做匹配处理
   */
  def checkTypeByTypeTag[T: TypeTag](v: T): String = typeOf[T] match {
    case t if t =:= typeOf[List[String]] => "List of Strings"
    case t if t =:= typeOf[List[Int]] => "List of Ints"
    case t if t =:= typeOf[Seq[Int]] => "Seq of Ints"
    case _ => ""
  }

  def typeString[T](o: T)(implicit tag: TypeTag[T]): String = {
    tag.tpe match {
      case TypeRef(utype, usymbol, args) => args.toString
      case _ => ""
    }
  }

  test("test check parameter type by typeTag") {
    // 2. typeTag has the detail type information, typeOf[T] is the parameter type
    checkTypeByTypeTag(strList) shouldBe "List of Strings"
    checkTypeByTypeTag(intList) shouldBe "List of Ints"
    checkTypeByTypeTag(intSeq) shouldBe "Seq of Ints"

    typeString(intList) shouldBe "List(Int)"
    typeString(strList) shouldBe "List(String)"
  }


  def getClassTag[T: ClassTag]: Class[_] = {
    val x = implicitly[ClassTag[T]]

    implicitly[ClassTag[T]].runtimeClass
  }

  def getTypeByTypeTag[T: TypeTag]: scala.reflect.runtime.universe.Type = typeOf[T]

  test("test generic type by ClassTag") {
    assert(getTypeByTypeTag[List[Int]] =:= typeOf[List[Int]])
    assert(!(getTypeByTypeTag[List[Int]] =:= typeOf[List[String]]))
    // 可以得到范型参数后的类型
    getClassTag[List[Int]] shouldBe classOf[List[String]]
  }

  trait Foo {

    type Bar

    // use weakTypeTag instead of typeTag for abstract type Bar
    def barType: scala.reflect.runtime.universe.Type = weakTypeTag[Bar].tpe
  }

  test("test weak Type tag") {
    new Foo {
      override type Bar = Int
    }.barType.toString shouldBe "Foo.this.Bar"
  }

}
