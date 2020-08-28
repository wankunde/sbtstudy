// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.collections

import com.wankun.util.Logging

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-08-26.
 */
object CollectionFunction extends Logging {

  def main(args: Array[String]): Unit = {
    val s1 = Seq(1, 2, 3, 4, 5, 6, 7, 8)
    s1.grouped(2).foreach(i => logInfo(s"seq of ${i}"))
  }
}
