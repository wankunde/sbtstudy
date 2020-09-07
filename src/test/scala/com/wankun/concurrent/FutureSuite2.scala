// Copyright 2019 wankun. All Rights Reserved.
package com.wankun.concurrent

import com.wankun.FunSuiteBase

import scala.concurrent.duration._
import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-05-08.
 */
class FutureSuite2 extends FunSuiteBase {
//  implicit val ec: ExecutionContext =
//    ExecutionContext.fromExecutor(new ForkJoinPool(30))
  import scala.concurrent.ExecutionContext.Implicits.global
  /**
   * firstCompletedOf : 返回最早执行完的Future
   */
  test("test future list") {

    // System processes
    logInfo(s"System processes: ${Runtime.getRuntime.availableProcessors()}")

    val timeout = 1100 millisecond

    // List[Future] 在被创建的时候就开始执行了
    val futures = List(1000, 1500, 1200, 2000, 800, 3000) map { ms =>
      val f = Future {
        logInfo("future list thread start...")
        Thread sleep ms
        logInfo(ms)
        ms toString
      }

      Future firstCompletedOf Seq(f, Future {
        logInfo("future list thread2 start...")
        Thread sleep 1100
        "-1"
      })
    }

    // 将多个Future的结果合并为一个结果Future
    val waitingList = Future sequence futures
    logInfo("Created")

    // 等待这个合并后的Future执行完毕
    logInfo(futures.size)
    val results = Await result(waitingList, timeout * futures.size)
    logInfo(results)
  }
}
