package com.wankun.parser

import com.wankun.util.Logging

import scala.util.parsing.combinator.RegexParsers

class ExprParser extends RegexParsers {

  val number: Parser[Int] = "[1-9][0-9]*".r ^^ (_.toInt)

  def operator: Parser[Any] = "+" | "-" | "*" | "/"

  def expr: Parser[Int] = number ~ opt(operator ~ expr) ^^ {
    case a ~ None => a
    case a ~ Some("*" ~ b) => a * b
    case a ~ Some("/" ~ b) => a / b
    case a ~ Some("+" ~ b) => a + b
    case a ~ Some("-" ~ b) => a - b
  }
}

object TestExprParser extends Logging {

  def main(args: Array[String]): Unit = {
    val parser = new ExprParser
    val result = parser.parseAll(parser.expr, "9*8+21/7")
    logInfo(s"${result.get}")
  }
}
