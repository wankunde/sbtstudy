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

  // scalastyle:off
  /**
   * 20:15:44.902 ForkJoinPool-1-worker-5 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:44.902 ForkJoinPool-1-worker-3 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:44.902 ForkJoinPool-1-worker-1 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:44.902 ForkJoinPool-1-worker-15 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:44.902 ForkJoinPool-1-worker-11 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:44.902 ForkJoinPool-1-worker-7 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:44.902 ForkJoinPool-1-worker-9 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:44.903 ScalaTest-run-running-FutureSuite2 INFO com.wankun.concurrent
   * .FutureSuite2: Created
   * 20:15:44.946 ForkJoinPool-1-worker-13 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:45.948 ForkJoinPool-1-worker-13 INFO com.wankun.concurrent.FutureSuite2: 1000
   * 20:15:45.948 ForkJoinPool-1-worker-13 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:46.003 ForkJoinPool-1-worker-15 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:46.003 ForkJoinPool-1-worker-11 INFO com.wankun.concurrent.FutureSuite2: start fallback
   * 20:15:46.003 ForkJoinPool-1-worker-7 INFO com.wankun.concurrent.FutureSuite2: future list
   * thread start...
   * 20:15:46.102 ForkJoinPool-1-worker-5 INFO com.wankun.concurrent.FutureSuite2: 1200
   * 20:15:46.407 ForkJoinPool-1-worker-9 INFO com.wankun.concurrent.FutureSuite2: 1500
   * 20:15:46.750 ForkJoinPool-1-worker-13 INFO com.wankun.concurrent.FutureSuite2: 800
   * 20:15:46.906 ForkJoinPool-1-worker-1 INFO com.wankun.concurrent.FutureSuite2: 2000
   */
  // scalastyle:off
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
