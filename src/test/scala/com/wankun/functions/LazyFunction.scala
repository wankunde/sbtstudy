// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.functions

import com.wankun.FunSuiteBase

/**
 * 前两种方法都很简单，关键是iterator2的参数明显应该是一个函数，但是确可以传入一个函数，并lazy执行
 * call-by-value:
 * call-by-name:
 */
class LazyFunction extends FunSuiteBase {

  def subFunc(): Long = {
    logInfo("callback is running...")
    System.currentTimeMillis()
  }

  test("test call-by-value") {
    testCallByValue(subFunc)
  }

  test("test call-by-name") {
    testCallByName(subFunc)
    testCallByName(subFunc())
  }

  def testCallByValue(subFunc: Long): Unit = {
    val callTime = System.currentTimeMillis()
    logInfo("begin work")
    Thread.sleep(2000)
    logInfo("end work")
    val funcValue = subFunc
    funcValue should be <= callTime
  }

  def testCallByName(subFunc: => Long): Unit = {
    val callTime = System.currentTimeMillis()
    logInfo("begin work")
    Thread.sleep(2000)
    logInfo("end work")
    val funcValue = subFunc
    funcValue should be >= (callTime + 2000)
  }
}
