// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package org.apache.spark.wankun.hellorpc

import com.wankun.util.Logging
import org.apache.spark.rpc.{RpcAddress, RpcEndpointRef, RpcEnv}
import org.apache.spark.util.ThreadUtils
import org.apache.spark.{SecurityManager, SparkConf}

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-09-07.
 */
object Client extends Logging {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    val rpcEnv =
      RpcEnv.create(
        "hello-server",
        "localhost",
        1234,
        conf,
        new SecurityManager(conf),
        true
      )

    val endpointRef = rpcEnv.setupEndpointRef(
      RpcAddress("localhost", 1234),
      "hello-service")

    asyncCall(endpointRef)
    syncCall(endpointRef)
  }

  def asyncCall(endpointRef: RpcEndpointRef): Unit = {
    logInfo("start async call, in main thread log")
    val future = endpointRef.ask[String](SayHi("new student"))
    future.onComplete {
      case Success(hiResp) => logInfo(s"rpc response : ${hiResp}")
      case Failure(e) => logError("got exception", e)
    }
    logInfo("end async call, in main thread log")
    ThreadUtils.awaitResult(future, Duration("3s"))
  }

  def syncCall(endpointRef: RpcEndpointRef): Unit = {
    logInfo("start sync call, in main thread log")
    val res = endpointRef.askSync[String](SayBye("new student"))
    logInfo(s"rpc response : ${res}")
    logInfo("end sync call, in main thread log")
    Thread.sleep(3000)
  }
}
