// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.spark.dataframe

import java.time.LocalDateTime
import org.apache.spark.TaskContext
import org.apache.spark.sql.test.SharedSparkSession
import org.apache.spark.sql.{QueryTest, Row}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-09-07.
 */
class ForEachPartitionSuite extends QueryTest with SharedSparkSession {

  import testImplicits._

  test("test forEachPartition") {
    val className = this.getClass.getName.stripSuffix("$")

    def consoleInfo(msg: => String): Unit = {
      // scalastyle:off
      println(s"${LocalDateTime.now()} $className : $msg")
      // scalastyle:on
    }

    val df = Range(1, 10).map(id => (id, s"stu-${id}")).toDF("id", "name").repartition(2)

    df.map {
      case Row(id: Int, name: String) =>
        consoleInfo(s"execute(${TaskContext.getPartitionId()}) run transform")
        (id, name)
    }.rdd.foreachPartition { iter =>
      consoleInfo(s"execute(${TaskContext.getPartitionId()}) start foreach init func")
      // jdbc connect
      Thread.sleep(1000)
      consoleInfo(s"execute(${TaskContext.getPartitionId()}) end foreach init func")

      iter.foreach(v => {
        consoleInfo(s"execute(${TaskContext.getPartitionId()}) loop read : ${v}")
      })
    }
  }
}
