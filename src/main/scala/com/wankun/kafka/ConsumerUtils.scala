// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.kafka

import java.util.Properties

import com.wankun.util.Logging
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.{Consumer, ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._


/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-02-18.
 */
trait ConsumerUtils[K, V] extends Logging {

  type PartitionOffsetMap = Map[TopicPartition, Long]

  var topic = "to-do-list"
  var brokers = "192.168.1.53:9092"
  var schemaRegistry = "http://192.168.1.53:8081"
  var groupId = "test_group"
  var messageType = "string"

  @volatile protected var _consumer: Consumer[K, V] = null

  def getOrCreateConsumer(): Consumer[K, V] = synchronized {
    if (_consumer == null) {
      val props = new Properties()
      props.put("schema.registry.url", schemaRegistry)
      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
      props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
      if ("string".equalsIgnoreCase(messageType)) {
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
          classOf[StringDeserializer].getCanonicalName)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
          classOf[StringDeserializer].getCanonicalName)
      } else if ("avro".equalsIgnoreCase(messageType)) {
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
          classOf[KafkaAvroDeserializer].getCanonicalName)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
          classOf[KafkaAvroDeserializer].getCanonicalName)
      }

      _consumer = new KafkaConsumer[K, V](props)
    }
    _consumer
  }


  def withConsumer[T](body: Consumer[K, V] => T): T = {
    val consumer = getOrCreateConsumer()
    body(consumer)
  }

  def closeConsumer(): Unit = withConsumer { consumer =>
    consumer.close()
  }

  def offsetMapToString(partitionsMap: PartitionOffsetMap): String = {
    partitionsMap.map(p => s"${p._1} : ${p._2}").mkString("\n")
  }

  def fetchEarlyOffsets(): PartitionOffsetMap = withConsumer { consumer =>
    val partitions = consumer.partitionsFor(topic).asScala
      .map(p => new TopicPartition(p.topic(), p.partition())).asJava
    consumer.assign(partitions)
    consumer.seekToBeginning(partitions)
    partitions.asScala.map(p => p -> consumer.position(p)).toMap
  }

  def fetchLatestOffsets(): PartitionOffsetMap = withConsumer { consumer =>
    val partitions = consumer.partitionsFor(topic).asScala
      .map(p => new TopicPartition(p.topic(), p.partition())).asJava
    consumer.assign(partitions)
    consumer.seekToEnd(partitions)
    partitions.asScala.map(p => p -> consumer.position(p)).toMap
  }
}
