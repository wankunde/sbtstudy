// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import org.apache.spark.sql.SparkSession

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-23.
 */
object Helloworld {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Streaming hello world")
      .getOrCreate()

    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

    // Split the lines into words
    val words = lines.as[String].flatMap(_.split(" "))

    // Generate running word count
    val wordCounts = words.groupBy("value").count()

    // Start running the query that prints the running counts to the console
    val query = wordCounts.writeStream
      .outputMode("complete") // or update but not append
      .format("console")
      .start()

    query.awaitTermination()
  }
}
