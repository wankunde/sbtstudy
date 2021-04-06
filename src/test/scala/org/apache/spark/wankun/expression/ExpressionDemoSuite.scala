// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package org.apache.spark.wankun.expression

import com.wankun.FunSuiteBase
import javax.print.DocFlavor.STRING
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.BooleanType
import org.apache.spark.unsafe.types.UTF8String

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-03-05.
 */
class ExpressionDemoSuite extends FunSuiteBase {

  test("basic expression function") {
    // test basic expression
    val expr = lower(lit("Hi Expression")).expr
    expr.eval() should be(UTF8String.fromString("hi expression"))

    // array contains
    val myArr = Array("this", "is", "cool")
    array_contains(lit(myArr), "cool").expr.eval() shouldBe true
    array_contains(lit(myArr), "hot").expr.eval() shouldBe false
  }

  test("create in expression") {
    val input = InternalRow(1, "wankun")

    // method 1
    // Using Catalyst DSL to create an In expression
    import org.apache.spark.sql.catalyst.dsl.expressions._

    // HACK: Disable symbolToColumn implicit conversion
    // It is imported automatically in spark-shell (and makes demos impossible)
    // implicit def symbolToColumn(s: Symbol): org.apache.spark.sql.ColumnName
    trait ThatWasABadIdea
    implicit def symbolToColumn(ack: ThatWasABadIdea): ThatWasABadIdea = ack

    val value = 'a.long
    import org.apache.spark.sql.catalyst.expressions.{Expression, Literal}
    import org.apache.spark.sql.types.StringType
    val list: Seq[Expression] = Seq(1, Literal.create(null, StringType), true)

    val inExpr1 = value in (list: _*)
    inExpr1.isInstanceOf[Expression] shouldBe true
    inExpr1.dataType should be(BooleanType)
    inExpr1.sql should be("(`a` IN (1, CAST(NULL AS STRING), true))")
    //    e.eval() shouldBe(false)

    // method 2
    val nullValue = lit(null)
    val inExpr2 = (nullValue isin (list: _*)).expr
    inExpr2.sql should be("(NULL IN (1, CAST(NULL AS STRING), true))")
    assert(inExpr2.eval() == null)

    // method 3
    val inExpr3 = In(value = Literal(1), list = Seq(array("1", "2, 3").expr))
    logInfo(inExpr3.sql)
//    inExpr3.eval() shouldBe(true)
  }
}
