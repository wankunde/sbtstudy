# Sbt Tutorial

## sbt 安装

手动安装 `sbt-1.2.8` 版本，这样方便后续和Idea集成

## Sbt 国内加速

[repositories]
  local
  activator-launcher-local: file://${activator.local.repository-${activator.home-${user.home}/.activator}/repository}, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
  activator-local: file://${activator.local.repository-/Users/wankun603/Applications/activator-dist-1.3.12/repository}, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
  maven-central
  typesafe-releases: http://repo.typesafe.com/typesafe/releases
  typesafe-ivy-releasez: http://repo.typesafe.com/typesafe/ivy-releases, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
  
  
  
//resolvers += "central" at "http://maven.aliyun.com/nexus/content/groups/public/"
//externalResolvers :=
//  Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)



## create a sbt project using command

* `sbt` 命令进入sbt管理命令行
  * show scalaVersion
  * show version
  * show organization
  * set organization := "com.wankun"
  * set scalaVersion := "2.11.7"
  * set version := "1.0"
  * session save
  * exit

## manager project command

* clean
* compile : 单次项目编译
* reload : 每次修改项目配置后，需要执行
* ~compile : 修改完scala文件后，立即编译
* console : 读取项目中存在的dependency和src files，进入scala 命令行模式
* run : 执行 scala object 
* test : 执行测试
* package ： 项目打包


## build.sbt

```
name := "sbt01"

version := "1.0"

scalaVersion := "2.11.7"

organization := "com.wankun"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.9",
  "org.joda" % "joda-convert" % "2.1.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
```

# Idea 使用

* 新建项目，直接创建scala - sbt 项目即可
* 在主界面下方，可以使用 `sbt shell` 面板直接进步sbt prompt ，执行sbt命令



# Scala Study

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

* HelloWorld : first scala
* OncePerSecond : anonymous functions
* Complex : classes
* CaseClass : case classes and pattern matching
* TrantTest : traits
* Reference : Genericity


## ScalaTest

[TODO]

**Programming in Scala**

* http://www.artima.com/pins1ed/
* http://www.artima.com/scalazine/articles/steps.html

**ScalaTest**

* http://www.scalatest.org/release_notes/2.2.4
* http://www.scalatest.org/quick_start
* http://www.scalatest.org/