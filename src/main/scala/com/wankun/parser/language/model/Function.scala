package com.wankun.parser.language.model

import scala.collection.mutable.HashMap

case class Function(name: String, arguments: Map[String, Int], statements: List[Statement], val returnValue: Expr)