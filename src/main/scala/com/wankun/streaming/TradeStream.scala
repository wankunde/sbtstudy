// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import org.apache.spark.sql.SparkSession

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-22.
 */
object TradeStream {

  def main(args: Array[String]): Unit = {
    val topics = "prd-sample-dstream-trades"
    val subscribeType = "subscribe"
    val bootstrapServers = "192.168.1.216:9092"
    val checkpointLocation = "/tmp/ck"

    val spark = SparkSession
      .builder
      .appName("TradeStreamingDemo")
      .getOrCreate()

    import spark.implicits._

    // Create DataSet representing the stream of input lines from kafka
    val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option(subscribeType, topics)
      .load()
      .selectExpr("CAST(value AS STRING)")
      .as[String]

    // Generate running word count
    val wordCounts = lines.flatMap(_.split(" ")).groupBy("value").count()

    // Start running the query that prints the running counts to the console
    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .option("checkpointLocation", checkpointLocation)
      .start()

    query.awaitTermination()
  }
}
