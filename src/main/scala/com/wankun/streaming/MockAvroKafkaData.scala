// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import java.util
import java.util.Properties

import com.wankun.avro.PageViewRecord
import com.wankun.util.Logging
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.StringSerializer

import scala.collection.JavaConverters._
import scala.util.Random

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-23.
 */
object MockAvroKafkaData extends Logging {

  val topic = "to-do-list"

  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("schema.registry.url", "http://192.168.1.53:8081")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.53:9092")
    props.put("acks", "all")
    props.put("key.serializer", classOf[StringSerializer].getCanonicalName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      classOf[KafkaAvroSerializer].getCanonicalName)
    props.put("message.timeout.ms", "3000")
    val producer = new KafkaProducer[String, PageViewRecord](props)

    val rand = new Random()

    logInfo("start send message!")
    while (true) {
      val viewtime = System.currentTimeMillis()
      val userid = "100" + rand.nextInt(10)
      val pageid = "p20" + rand.nextInt(10)
      val record = new PageViewRecord(viewtime, userid, pageid)
      logInfo(s"send record : $record")
      val header: Header = new RecordHeader("userid", userid.getBytes("UTF-8"))
      val headers: util.List[Header] = List(header).asJava
      val data = new ProducerRecord[String, PageViewRecord](topic, null, userid, record, headers)
      producer.send(data, new Callback {
        override def onCompletion(metadata: RecordMetadata, e: Exception): Unit = {
          if (e != null) e.printStackTrace()
          else System.out.println("The offset of the record we just sent is: " + metadata.offset)
        }
      })
      Thread.sleep(1000)
    }
    logInfo("end send message!")
  }
}
