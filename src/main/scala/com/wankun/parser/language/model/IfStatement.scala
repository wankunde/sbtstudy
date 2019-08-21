package com.wankun.parser.language.model

case class IfStatement(condition: Condition, trueBranch: List[Statement], falseBranch: List[Statement]) extends Statement