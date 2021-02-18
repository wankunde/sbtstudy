// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import java.util
import java.util.Properties

import com.wankun.util.Logging
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.StringSerializer

import scala.collection.JavaConverters._
import scala.util.Random

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-23.
 */
object MockKafkaDataTest2 extends Logging {

  val topic = "test3"

  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "192.168.1.51:9092")
    props.put("acks", "all")
    props.put("key.serializer", classOf[StringSerializer].getCanonicalName)
    props.put("value.serializer", classOf[StringSerializer].getCanonicalName)
    props.put("message.timeout.ms", "3000")
    val producer = new KafkaProducer[String, String](props)

    val rand = new Random()

    logInfo("start send message!")
    var i = 0
    while (true) {
      i = (i + 1) % 10
      val storeId = 100 + i
      val buyerNick = s"wankun$i"
      val sex = if (i % 2 == 0) "man" else "woman"
      val msg = s"$storeId,$buyerNick,$sex"
      logInfo(s"send msg : $msg")
      val data = new ProducerRecord[String, String](topic, msg)
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
