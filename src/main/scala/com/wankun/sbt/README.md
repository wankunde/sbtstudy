# Sbt Tutorial

## sbt 安装

手动安装 `sbt-1.2.8` 版本，这样方便后续和Idea集成

# SBT 配置

修改 `~/.sbt/repositories` 

```$xslt
[repositories]
  local
  local-preloaded-ivy: file:///${sbt.preloaded-${sbt.global.base-${user.home}/.sbt}/preloaded/}, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]
  local-preloaded: file:///${sbt.preloaded-${sbt.global.base-${user.home}/.sbt}/preloaded/}
  gcs-maven-central-mirror: https://maven-central.storage-download.googleapis.com/repos/central/data/
  maven-central
  typesafe-ivy-releases: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  sbt-ivy-snapshots: https://repo.scala-sbt.org/scalasbt/ivy-snapshots/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  sbt-plugin-releases: https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
  bintray-spark-packages: https://dl.bintray.com/spark-packages/maven/
  typesafe-releases: http://repo.typesafe.com/typesafe/releases/
  spring-releases: https://repo.spring.io/plugins-release/
```

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
  * set scalaVersion := "2.11.12"
  * set version := "1.0"
  * session save
  * exit

## [manager project command](https://www.scala-sbt.org/0.13/docs/Running.html)

* clean
* compile : 单次项目编译
* reload : 每次修改项目配置后，需要执行
* ~compile : 修改完scala文件后，立即编译
* console : 读取项目中存在的dependency和src files，进入scala 命令行模式
* run : 执行 scala object 
* test : 执行所有UT
* testOnly : 执行指定UT，例如 `testOnly *.WithColumn*`
* package : 项目打包
* project : 查看当前项目


## build.sbt

```
name := "sbt01"

version := "1.0"

scalaVersion := "2.11.12"

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

# sbt plugins

[SBT 插件列表](https://github.com/sbt/sbt/wiki/sbt-1.x-plugin-migration)

介绍常见的几个plugins

* `addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")` 加速并发下载依赖包的插件，好用，但是目前不知道依赖包被下载到地方了
* `addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")` scala 代码风格插件，启用命令`scalastyle`可以检查scala代码的书写风格，但是太严格了。。。
* `addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.3.0")` 这个是用于解决不同Jar包版本冲突的工具，启用命令`mimaReportBinaryIssues`,会报告依赖包之间的冲突
* `addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")` 用于发布版本的时候，通过覆盖写 `version.sbt`文件来实现版本号更新，自动commit操作等。

##  sbt-spark-package

* 在 /project/plugins.sbt 文件中添加

```
resolvers += "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"

addSbtPlugin("org.spark-packages" % "sbt-spark-package" % "0.2.6")
```

* 在 `build.sbt` 中添加 `sparkVersion := "2.4.0.cloudera2"`, 此时通过 libraryDependencies 命令可以查看到当前项目已经引入了spark-core的provided 包。

* 修改 `sparkComponents ++= Seq("core", "sql", "graphx")` 参数来改变我们需要的不同的spark component包。
  但是我没有找到自动加载spark各模块的test包，所以还是老老实实的一个一个的引入spark及test包吧，看起来也更清晰。。。
  
* 此时 `console` 命令会自动进行spark-shell 命令行（包含 spark context 和 SQL context）

# scala Jvm configurations with "run" and "test" action in SBT

```$xslt
scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-P:genjavadoc:strictVisibility=true" // hide package private types and methods in javadoc
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
```