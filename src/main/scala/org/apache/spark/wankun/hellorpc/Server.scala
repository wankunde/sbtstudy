// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package org.apache.spark.wankun.hellorpc

import java.time.LocalDateTime

import org.apache.spark.{SecurityManager, SparkConf}
import org.apache.spark.rpc.{RpcCallContext, RpcEndpoint, RpcEnv}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-09-07.
 */
object Server {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    val rpcEnv =
      RpcEnv.create(
        "hello-server",
        "localhost",
        1234,
        conf,
        new SecurityManager(conf)
      )

    rpcEnv.setupEndpoint("hello-service", new RpcEndpoint {
      override val rpcEnv: RpcEnv = rpcEnv

      val className = this.getClass.getName.stripSuffix("$")

      def consoleInfo(msg: => String): Unit = {
        // scalastyle:off
        println(s"${LocalDateTime.now()} $className : $msg")
        // scalastyle:on
      }

      override def onStart(): Unit = {
        consoleInfo("start hello endpoint")
      }

      override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
        case SayHi(msg) =>
          consoleInfo(s"receive $msg")
          context.reply(s"hi, $msg")
        case SayBye(msg) =>
          consoleInfo(s"receive $msg")
          context.reply(s"byte, $msg")
      }

      override def onStop(): Unit = {
        consoleInfo("stop hello endpoint")
      }


    })
    rpcEnv.awaitTermination()
  }
}
