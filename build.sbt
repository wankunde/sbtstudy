
name := "sbtstudy"

organization := "com.wankun"

crossScalaVersions := Seq("2.12.8", "2.11.12")

version := "1.0"

scalaVersion := crossScalaVersions.value.head

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.apache.spark" %% "spark-hive" % "3.0.0",

  // Test deps
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "org.apache.spark" %% "spark-core" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-sql" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-hive" % "3.0.0" % "test" classifier "tests",
  "org.apache.spark" %% "spark-catalyst" % "3.0.0" % "test" classifier "tests"
)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "log4j" % "log4j" % "1.2.17",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",

  // for scala parser
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5"
)

resolvers ++= Seq(
  "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)

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