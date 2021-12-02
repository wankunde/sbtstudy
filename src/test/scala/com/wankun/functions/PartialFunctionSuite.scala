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

package com.wankun.functions

import com.wankun.FunSuiteBase

class PartialFunctionSuite extends FunSuiteBase {

  test("test partial function") {
    val func: PartialFunction[Int, String] = {
      case i if i % 2 == 0 => s"$i is even"
    }

    val data = 1 to 5
    data.collect(func) should equal(
      Seq("2 is even", "4 is even")
    )
  }

  test("test lift method") {
    val func: PartialFunction[Int, String] = {
      case i if i % 2 == 0 => s"$i is even"
    }

    val data = 1 to 5
    data.map(func.lift) should equal(
      Seq(None, Some("2 is even"), None, Some("4 is even"), None)
    )

    val lifted = func.lift
    data.foreach(i => lifted(i).foreach(println))
    // lifted(i) : return Option[String]
    // lifted(i).foreach(println) : Do println only if lifted(i) has value
  }
}
