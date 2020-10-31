// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import com.wankun.avro.PageViewRecord
import org.apache.avro.Schema
import org.apache.avro.Schema.Field
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.sql.streaming.Trigger
import za.co.absa.abris.avro.functions._
import za.co.absa.abris.avro.read.confluent.SchemaManager

import scala.concurrent.duration._

import scala.collection.JavaConverters._

/**
 * 0. queryName job名称
 * 1. Trigger 用于触发job启动， 分为Continuous和ProcessingTime两类
 * 2. checkpoint
 *
 */
object KafkaAvroStream {
  def main(args: Array[String]): Unit = {
    val topics = "to-do-list"
    val subscribeType = "subscribe"
    val bootstrapServers = "192.168.1.53:9092"

    val spark = SparkSession
      .builder
      .appName("Avro Kafka Streaming Consumer")
      .getOrCreate()

    import spark.implicits._

    val pv = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option(ConsumerConfig.GROUP_ID_CONFIG, "test_group")
      .option(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        classOf[StringDeserializer].getCanonicalName)
      .option(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        "io.confluent.kafka.serializers.KafkaAvroDeserializer")
      .option(subscribeType, topics)
      .load()
      .select($"value")

    val schemaRegistryConf = Map(
      SchemaManager.PARAM_SCHEMA_REGISTRY_URL -> "http://192.168.1.53:8081",
      SchemaManager.PARAM_SCHEMA_REGISTRY_TOPIC -> topics,
      SchemaManager.PARAM_VALUE_SCHEMA_NAMING_STRATEGY -> "topic.name",
      SchemaManager.PARAM_VALUE_SCHEMA_ID -> "latest"
    )
    val readerSchema =
      s"""{
         |    "type": "record",
         |    "name": "PageViewRecord",
         |    "namespace": "com.wankun.avro",
         |    "fields": [
         |        {
         |            "name": "viewtime",
         |            "type": "long",
         |            "default": 0
         |        },
         |        {
         |            "name": "pageid",
         |            "type": "string",
         |            "default": ""
         |        }
         |    ]
         |}
         |""".stripMargin
    val avroStream = PageViewRecord.getClassSchema
    val fields =
      List("viewtime", "pageid").map(fieldName => {
        val field = avroStream.getField(fieldName)
        new Field(field.name(), field.schema(), field.doc(), field.defaultVal())
      }).asJava
    val prunedSchema =
      Schema.createRecord(avroStream.getName, avroStream.getNamespace,
        avroStream.getDoc, avroStream.isError, fields)
    val pvstats =
      pv.withColumn("pv", from_confluent_avro('value, prunedSchema.toString, schemaRegistryConf))
        .withColumn("viewtime", ($"pv.viewtime".as("long") / 1000).cast("timestamp"))
        .withColumn("pageid", $"pv.pageid")
        .groupBy(
          functions.window($"viewtime", "30 seconds", "30 seconds"),
          $"pageid"
        )
        .count()

    val query = pvstats.writeStream
      .outputMode("update")
      .option("truncate", "false")
      .option("numRows", "100")
      .option("checkpointLocation", "hdfs:///tmp/kafkaAvroStream")
      .trigger(Trigger.ProcessingTime(30.seconds))
      .format("console")
      .queryName("counter batch")
      .start()

    query.awaitTermination()
  }
}
