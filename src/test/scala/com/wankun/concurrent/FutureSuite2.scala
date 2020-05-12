// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.concurrent

import com.wankun.util.FunSuiteBase

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-05-12.
 */
class FutureSuite2 extends FunSuiteBase {

  test("test future list") {
    val futures = List(1000, 1500, 1200, 2000, 800, 3000) map { ms =>
      Future firstCompletedOf Seq(
        Future {
          logInfo("future list thread start...")
          Thread sleep ms
          logInfo(ms)
          ms toString
        },
        Future {
          logInfo("start fallback")
          Thread sleep 1100
          "-1"
        })
    }

    // 将多个Future的结果合并为一个结果Future
    val waitingList = Future sequence futures
    logInfo("Created")

    val results = Await result(waitingList, 3 seconds)
    logInfo(results)
  }

}
