package com.wankun.parser.language.model

case class FunctionCall(name: String, values: Map[String, Expr]) extends Expr with Statement
