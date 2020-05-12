package com.wankun.parser.language.model

case class LoopStatement(times: Int, statements: List[Statement]) extends Statement
