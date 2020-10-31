// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.StructType

import scala.concurrent.duration._

/**
 * 0. queryName job名称
 * 1. Trigger 用于触发job启动， 分为Continuous和ProcessingTime两类
 * 2. checkpoint
 *
 */
object KafkaStream {
  def main(args: Array[String]): Unit = {
    val topics = "test2"
    val subscribeType = "subscribe"
    val bootstrapServers = "192.168.1.51:9092"

    val spark = SparkSession
      .builder
      .appName("Kafka Streaming Consumer")
      .getOrCreate()

    import spark.implicits._

    // Create DataSet representing the stream of input lines from kafka
    val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("kafka.group.id", "kafka_stream")
      .option("includeHeaders", "true")
      .option("startingOffsets", "earliest")
      .option(subscribeType, topics)
      .load()
      .selectExpr("CAST(key as STRING)", "CAST(value AS STRING)")
      .as[(String, String)]

    val words = lines.withColumn("tokens", split('value, ","))
      .withColumn("ts", ('tokens(0) cast "long") / 1000 cast "timestamp")
      .withColumn("url", 'tokens(1))
      .withColumn("ip", 'tokens(2))
      .select("key", "ts", "url", "ip")

    val query2 = words.writeStream
      .outputMode("append")
      .format("console")
      .option("truncate", "false")
      .option("numRows", "100")
      .queryName("words batch")
      // Trigger.ProcessingTime(5.seconds) 每5秒启动一个job
      // 间隔为0时，上批次调用完成后，立即进入下一批次调用一直调用
      .trigger(Trigger.ProcessingTime(5.seconds))
      // Trigger.ProcessingTime(5.seconds) 每5秒启动一个job，但是进程不再退出
      // 如果使用 Continuous 模式，会引发Spark内部告警
      // KafkaDataConsumer is not running in UninterruptibleThread.
//      .trigger(Trigger.Continuous(5.seconds))
      .start()
    query2.awaitTermination()

    //    // query by window
    //    val wordCounts =
    //      words
    //        // watermark 的时间是以当前数据集中的最新记录时间，向前推
    //        .withWatermark("ts", "1 minutes")
    //        .groupBy(
    //          window($"ts", "30 seconds", "10 seconds"),
    //          $"ip"
    //        ).count()
    //
    //    // Start running the query that prints the running counts to the console
    //    val query = wordCounts.writeStream
    //      .outputMode("update")
    //      .option("truncate", "false")
    //      .option("numRows", "100")
    //      .option("checkpointLocation", "hdfs:///tmp/kafkaStream")
    //      // 这里最好指定trigger，trigger不指定，有数据到达的时候，就会触发query查询
    //      .trigger(Trigger.ProcessingTime(20.seconds))
    //      .format("console")
    //      .queryName("counter batch")
    //      .start()
    //
    //    query.awaitTermination()
  }
}
