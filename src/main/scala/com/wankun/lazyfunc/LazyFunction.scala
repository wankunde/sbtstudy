// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.lazyfunc

import com.wankun.util.Logging

/**
 * 前两种方法都很简单，关键是iterator2的参数明显应该是一个函数，但是确可以传入一个函数，并lazy执行
 */
object LazyFunction extends Logging {

  def callback(): Unit = {
    logInfo("callback is running...")
  }

  def iterator(callback: () => Unit): Unit = {
    logInfo("begin work")
    Thread.sleep(2000)
    logInfo("end work")
    callback()
  }

  def iterator2(callback: => Unit): Unit = {
    logInfo("begin work")
    Thread.sleep(2000)
    logInfo("end work")
    callback
  }

  def main(args: Array[String]): Unit = {
//    iterator(callback()) 不能这样调用，因为callback()返回的是Unit类型，不是一个函数
    iterator(callback)
    logInfo("===================")
    iterator2(callback)
    logInfo("===================")
    iterator2(callback())
  }

}
