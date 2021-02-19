# Spark Streaming程序开发和测试

## 使用步骤

sbt编译项目 : `sbt clean assembly`

spark 运行编译好的assembly jar: `spark-submit --master local  --class com.wankun.streaming.KafkaStreamTest2 target/scala-2.12/sbtstudy-assembly-1.0.jar`

注意事项:
1. 因为Streaming程序读写kafka的时候，需要依赖与kafka的包，所以需要assembly kafka的依赖包，这样方便程序开发和调试
2. Streaming程序必须要设置checkpoint，否则无法运行
