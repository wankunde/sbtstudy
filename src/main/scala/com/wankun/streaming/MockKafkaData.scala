// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.streaming

import java.util
import java.util.Properties

import com.wankun.util.Logging
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.StringSerializer

import scala.util.Random

import collection.JavaConverters._

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-10-23.
 */
object MockKafkaData extends Logging {

  val topic = "test2"

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
    while (true) {
      val ct = System.currentTimeMillis() - 180000
      val ip = "192.168.2." + rand.nextInt(10)
      val msg = ct + ",www.example.com," + ip
      logInfo(s"send msg : $msg")
      val header: Header = new RecordHeader("userid", "123".getBytes("UTF-8"))
      val headers: util.List[Header] = List(header).asJava
      val data = new ProducerRecord[String, String](topic, null, ip, msg, headers)
      data.headers()
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
