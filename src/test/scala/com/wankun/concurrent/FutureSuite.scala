// Copyright 2019 wankun. All Rights Reserved.
package com.wankun.concurrent

import java.util.concurrent.Executors

import com.wankun.util.FunSuiteBase

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, TimeoutException}
import scala.util.{Failure, Success}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-05-08.
 */
class FutureSuite extends FunSuiteBase {

  // 可以使用系统默认线程池，也可以使用自定义线程池
  // !!! 多线程的线程池要注意了，如果需要运行的线程超过线程池，会造成排队，而排队的后果就是Future实际启动和执行的时间不可控了
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30))

  test("test future") {
    logInfo("begin main thread")
    val future = Future {
      logInfo("begin future thread")
      Thread sleep 1000
      logInfo("future finished")
      "hello future"
    }
    future.onComplete {
      case Success(value) =>
        Thread sleep 500
        value should be("hello future")
        logInfo("do something else with result value.")
      case Failure(ex) =>
        logError("future failed!", ex)
    }

    /* 定义完Future后并不会立即执行 */

    Thread sleep 2000
    logInfo("main thread finished")
    val ret = Await result(future, 3 seconds)
    logInfo("all finished")

    ret should be("hello future")

  }

  /**
   * 在指定时间内未完成任务，抛出TimeoutException
   */
  test("test future timeout exception") {
    val future = Future {
      Thread sleep 3000
      "hello future"
    }

    intercept[TimeoutException] {
      Await result(future, 1 seconds)
    }
  }

  /**
   * firstCompletedOf : 返回最早执行完的Future
   */
  test("test future list") {

    val timeout = 1100 millisecond

    def fallback(timeout: Duration): Future[String] = Future {
      Thread sleep (timeout toMillis)
      "-1"
    }

    // List[Future] 在被创建的时候就开始执行了
    val futures = List(1000, 1500, 1200, 2000, 800, 3000) map { ms =>
      val f = Future {
        logInfo("future list thread start...")
        Thread sleep ms
        logInfo(ms)
        ms toString
      }

      Future firstCompletedOf Seq(f, fallback(timeout))
    }

    // 将多个Future的结果合并为一个结果Future
    val waitingList = Future sequence futures
    logInfo("Created")

    // 等待这个合并后的Future执行完毕
    logInfo(futures.size)
    val results = Await result(waitingList, timeout * futures.size)
    logInfo(results)
  }

  /**
   * firstCompletedOf : 返回最早执行完的Future
   */
  test("test sequence future list") {

    val timeout = 1100 millisecond

    // List[Future] 在被创建的时候就开始执行了
    val futures = List(1000, 1500, 1200, 2000, 800, 3000) map { ms =>
      Future {
        logInfo("sequence future thread start...")
        Thread sleep ms
        logInfo(ms)
        ms toString
      }
    }

    // 将多个Future的结果合并为一个结果Future
    val waitingList = Future sequence futures
    logInfo("Created")

    // 等待这个合并后的Future执行完毕
    val results = Await result(waitingList, timeout * futures.size)
    logInfo(results)
  }
}
