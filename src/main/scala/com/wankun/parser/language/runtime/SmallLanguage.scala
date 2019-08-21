package com.wankun.parser.language.runtime


import com.wankun.parser.language.SmallLanguageParser

import scala.io.Source

object SmallLanguage {
  def main(args: Array[String]) {

    val inputFile = Source.fromFile(getClass.getResource("/parser/program.small").getFile)
    val inputSource = inputFile.mkString

    val parser = new SmallLanguageParser
    parser.parseAll(parser.program, inputSource) match {
      case parser.Success(r, n) => {
        val interpreter = new Interpreter(r)

        try {
          interpreter.run
        } catch {
          case e: RuntimeException => println(e.getMessage)
        }
      }
      case parser.Error(msg, n) => println("Error: " + msg)
      case parser.Failure(msg, n) => println("Error: " + msg)
      case _ =>
    }
  }
}