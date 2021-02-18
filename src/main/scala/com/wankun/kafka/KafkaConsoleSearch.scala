// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.kafka

import java.time.Duration


/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-02-18.
 * 工具类，负责消费指定kafka topic
 */
object KafkaConsoleSearch extends ConsumerUtils[String, String] {

  def main(args: Array[String]): Unit = {
    parseArgument(args)
    val earliestPartitionsMap = fetchEarlyOffsets()
    val latestPartitionsMap = fetchLatestOffsets()
    logInfo(
      s"""Earliest Offset Info:
         |${offsetMapToString(earliestPartitionsMap)}
         |Latest Offset Info:
         |${offsetMapToString(latestPartitionsMap)}
         |""".stripMargin)

    pollMessage(latestPartitionsMap, 2)
    closeConsumer()
  }

  def parseArgument(args: Array[String]): Unit = {
    var i = 0
    while (i < args.length) {
      if (args(i) == "--topic") {
        i = i + 1
        topic = args(i)
      } else if (args(i) == "--brokers") {
        i = i + 1
        brokers = args(i)
      } else if (args(i) == "--groupId") {
        i = i + 1
        groupId = args(i)
      } else if (args(i) == "--messageType") {
        i = i + 1
        messageType = args(i)
      } else if (args(i) == "--schemaRegistry") {
        i = i + 1
        schemaRegistry = args(i)
      }
      i = i + 1
    }
  }


  def pollMessage(partitionsMap: PartitionOffsetMap,
                  rowLimit: Int = 100): Unit = withConsumer { consumer =>
    partitionsMap.foreach { case (partition, offset) =>
      consumer.seek(partition, offset)
    }
    var i = 0;
    while (i < rowLimit) {
      val records = consumer.poll(Duration.ofSeconds(10))
      val it = records.iterator()
      // empty result in 10 seconds, just return
      if (!it.hasNext) {
        return
      }
      while (i < rowLimit && it.hasNext) {
        val msg = it.next()
        i = i + 1
        log.info(s"offset: ${msg.offset} ${msg.timestamp()} ${msg.key} -> ${msg.value} ")
      }
    }
  }

}
