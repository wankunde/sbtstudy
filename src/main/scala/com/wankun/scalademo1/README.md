# Learn Scala

## [GITHUB](https://github.com/wankunde/scalatest.git)

## Install Scala and sbt and IDE

### sbt config

* add `~/.sbt/repositories` file in configure directory.
```
[repositories]
  local
  nexus:  http://nexus.zamplus.net/public/
  osc: http://maven.oschina.net/content/groups/public/
  typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  sonatype-oss-releases
  maven-central
  sonatype-oss-snapshots
```

* ivy home :
 * way1 : modify `bin\sbt-launch.jar` &  `\sbt\sbt.boot.properties` 
 * way2 : add parameter in command line . `-Dsbt.ivy.home=project/.ivy` or update **sbt** script
 
### IDE : Intellij IDEA 

## [Scala Tutorial](http://www.scala-lang.org/docu/files/ScalaTutorial.pdf)

* HelloWorld : first scala,Strng Interpolation
* OncePerSecond : anonymous functions
* Complex : classes
* CaseClass : case classes and pattern matching
* TrantTest : traits
* Reference : Genericity
----
* HelloScala.sc : Scala WorkSheet
* HelloWorldTest : scala test with Funsuite




## ScalaTest

[TODO]

**Programming in Scala**

* http://www.artima.com/pins1ed/
* http://www.artima.com/scalazine/articles/steps.html

**ScalaTest**

* http://www.scalatest.org/release_notes/2.2.4
* http://www.scalatest.org/quick_start
* http://www.scalatest.org/