package scalatest


object OncePerSecond {

  def printinfo(): Unit = {
    println("hello")
  }

  def main(args: Array[String]) {
    //    oncePerSceond(printinfo)
    oncePerSceond(() => {
      println("hello anonymous")
    })
  }

  def oncePerSceond(callback: () => Unit): Unit = {
    while (true) {
      callback()
      Thread sleep 1000
    }
  }
}
