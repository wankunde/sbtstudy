// Copyright 2019 wankun. All Rights Reserved.
package com.wankun.concurrent

import com.wankun.util.FunSuiteBase

import scala.concurrent.duration._
import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-05-08.
 */
class FutureSuite extends FunSuiteBase {

  test("test future") {
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool())

    logInfo("begin main thread")
    val future = Future {
      logInfo("begin future thread")
      Thread sleep 1000
      logInfo("future finished")
      "hello future"
    }

    /* 定义完Future后并不会立即执行 */
    Thread sleep 2000
    logInfo("main thread finished")
    val ret = Await result(future, 3 seconds)
    logInfo("all finished")

    ret should be("hello future")

  }
}
