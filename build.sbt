name := "scalatest"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-core" % "1.0.0",
  "ch.qos.logback" % "logback-classic" % "1.0.0"
)

// for scala parser
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5"
