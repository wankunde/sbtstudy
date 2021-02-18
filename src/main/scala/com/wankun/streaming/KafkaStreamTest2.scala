// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import java.nio.file.Paths

import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger

import scala.concurrent.duration._

/**
 * 0. queryName job名称
 * 1. Trigger 用于触发job启动， 分为Continuous和ProcessingTime两类
 * 2. checkpoint
 *
 */
object KafkaStreamTest2 {
  def main(args: Array[String]): Unit = {
    val topics = "test3"
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
      .option("kafka.group.id", "kafka_stream_test2")
      .option("includeHeaders", "true")
      .option("startingOffsets", "earliest")
      .option("failOnDataLoss", "false")
      .option(subscribeType, topics)
      .load()
      .selectExpr("CAST(key as STRING)", "CAST(value AS STRING)")
      .as[(String, String)]

    val df = lines.withColumn("tokens", split('value, ","))
      .withColumn("store_id", 'tokens(0) cast "long")
      .withColumn("buyer_nick", 'tokens(1))
      .withColumn("sex", 'tokens(2))
      .select("store_id", "buyer_nick", "sex")

    val query2 = df.filter(" NOT(substr(buyer_nick, 1, 1) = '~' AND substr(buyer_nick, -1) = '~') ")
      .withColumn("id", concat(col("store_id"), lit("_"), col("buyer_nick"))).toJSON.toDF("value")
      .writeStream.queryName("write  to kafka")
      .format("kafka")
      .outputMode("append")
      .trigger(Trigger.ProcessingTime(60.seconds))
      .option("topic", "test4")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("checkpointLocation", "/.checkpoint/test2")
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
