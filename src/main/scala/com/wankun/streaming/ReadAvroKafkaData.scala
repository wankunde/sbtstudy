// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import java.util.Properties

import com.wankun.util.Logging
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-29.
 */
object ReadAvroKafkaData extends Logging {

  val topic = "to-do-list"

  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("schema.registry.url", "http://192.168.1.53:8081")
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.53:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      classOf[KafkaAvroDeserializer].getCanonicalName)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val consumer = new KafkaConsumer[String, GenericRecord](props)
    consumer.subscribe(List(topic).asJava)
    logInfo("start consume message!")

    try {
      while (true) {
        val records = consumer.poll(100)
        val it = records.iterator()
        while (it.hasNext) {
          val record = it.next()
          println(s"offset = ${record.offset}, key = ${record.key}, value = ${record.value} ")
        }
      }
    } finally {
      consumer.close()
    }

  }
}
