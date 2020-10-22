#!/usr/bin/env bash

BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
spark-submit --class com.wankun.streaming.TradeStream \
--master yarn \
--deploy-mode cluster \
--driver-memory 1g \
--executor-memory 2g \
--executor-cores 3 \
${BASEDIR}/target/scala-2.12/sbtstudy_2.12-1.0.jar

# --queue thequeue \