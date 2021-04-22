import java.util.Locale

name := "sbtstudy"

organization := "com.wankun"

crossScalaVersions := Seq("2.12.8", "2.11.12")

scalaVersion := crossScalaVersions.value.head

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "log4j" % "log4j" % "1.2.17",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",
  "org.apache.commons" % "commons-lang3" % "3.9",

  // for scala parser
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5",

  "org.apache.spark" %% "spark-core" % "3.0.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.0.0" % "provided",
  "org.apache.spark" %% "spark-hive" % "3.0.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "3.0.0" % "provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.0",
  // 因为hive包中和spark包中avro冲突，做移除处理
//  "org.apache.spark" %% "spark-avro" % "3.0.0",
  "za.co.absa" %% "abris" % "3.2.1"
    exclude("org.apache.spark", "spark-avro")
    exclude("org.apache.avro", "avro"),

  // dependency for kafka schema registry
  "io.confluent" % "kafka-avro-serializer" % "5.3.1",

  // 引入Avro定义的接口数据对象
  "com.wankun" % "java" % "1.0"
    exclude("cglib", "*")
    exclude("com.google.guava", "*")
    exclude("io.dropwizard.metrics", "*")
    exclude("io.netty", "*")
    exclude("org.apache.avro", "*")
    exclude("org.apache.calcite", "*"),

  // Test deps
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "org.apache.spark" %% "spark-core" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-sql" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-hive" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-catalyst" % "3.0.0" % "test" classifier "tests"
)

// setting for assembly plugin
autoScalaLibrary := false

// skip tests
test in assembly := {}

// merge class and resource files strategy when find in different jars
assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase(Locale.ROOT).endsWith("manifest.mf")
  => MergeStrategy.discard
  case m if m.toLowerCase(Locale.ROOT).matches("meta-inf.*\\.sf$")
  => MergeStrategy.discard
  // assembly log4j.properties and remove $SPARK_HOME/conf/log4j.properties
  case "log4j.properties" => MergeStrategy.filterDistinctLines
  case m if m.toLowerCase(Locale.ROOT).startsWith("meta-inf/services/")
  => MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

resolvers += Resolver.url(
  "bintray-sbt-plugins", url("http://dl.bintray.com/sbt/sbt-plugin-releases")
)(Resolver.ivyStylePatterns)

resolvers += "Confluent Maven Repo" at "http://packages.confluent.io/maven/"

scalacOptions ++= Seq(
  "-target:jvm-1.8"
)

javaOptions += "-Xmx1024m"

fork in Test := true

// Configurations to speed up tests and reduce memory footprint
javaOptions in Test ++= Seq(
  "-Dspark.ui.enabled=false",
  "-Dspark.ui.showConsoleProgress=false",
  "-Dspark.databricks.delta.snapshotPartitions=2",
  "-Dspark.sql.shuffle.partitions=5",
  "-Ddelta.log.cacheSize=3",
  "-Dspark.sql.sources.parallelPartitionDiscovery.parallelism=5",
  "-Xmx1024m"
)