#!/usr/bin/env bash

BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
spark-submit --class com.wankun.streaming.KafkaStream \
--master local[4] \
${BASEDIR}/target/scala-2.12/sbtstudy-assembly-1.0.jar