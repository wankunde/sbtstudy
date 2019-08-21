package com.wankun.parser

/**
  * @author kun.wan, <kun.wan@leyantech.com>
  * @date 2019-08-07.
  */
import scala.util.Try
import scala.util.parsing.combinator.RegexParsers

class ParserDemo1 extends RegexParsers {
  def expression = {
    number ~ symbol ~ number ^^ { case firstOperand ~ operator ~ secondOperand =>
      validateAndExtractFirstValue(firstOperand) + validateAndExtractSecondValue(secondOperand)
    }
  }

  def symbol: Parser[Any] = "+" | "-" | "*"

  def number: Parser[Int] = """(0|[1-9]\d*)""".r ^^ { _.toInt }

  def validateAndExtractFirstValue(firstOperand: Any): Int = {
    val firstValue: Try[Int] = Try(firstOperand.toString.toInt)
    firstValue match {
      case util.Success(value) => value
      case util.Failure(exception) => throw new Exception("can not convert values to integer")
    }
  }

  def validateAndExtractSecondValue(secondOperand: Any): Int = {
    val secondValue = Try(secondOperand.toString.toInt)
    secondValue match {
      case util.Success(value) => value
      case util.Failure(exception) => throw new Exception("can not convert values to integer")
    }
  }
}
object TestParserDemo1 extends ParserDemo1 {

  def main(args: Array[String]) = {
    parse(expression, "5 + 4") match {
      case Success(result, _) => println(result)
      case Failure(msg, _) => println("FAILURE: " + msg)
      case Error(msg, _) => println("ERROR: " + msg)
    }
  }

}