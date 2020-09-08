// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package org.apache.spark.dataframe

import org.apache.spark.sql.functions._
import org.apache.spark.sql.test.SharedSparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{QueryTest, Row}

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2020-09-04.
 */
class WithColumnSuite extends QueryTest with SharedSparkSession {

  import testImplicits._


  test("with lit column") {
    val df = List("susan", "king").toDF("name")
    val dfwithColumn1 = df.withColumn("age", lit(18))
    val dfwithColumn2 = df.transform(df => df.withColumn("age", lit(18)))

    val expectedData = spark.sparkContext.parallelize(Seq(Row("susan", 18), Row("king", 18)))
    val structType1 = StructType.fromDDL("name STRING, age Int")
    val structType2 =
      StructType(Seq(
        StructField("name", StringType),
        StructField("name", IntegerType)
      ))


    val expectedDF1 = spark.createDataFrame(expectedData, structType1)
    val expectedDF2 = spark.createDataFrame(expectedData, structType2)

    checkAnswer(dfwithColumn1, dfwithColumn2)
    checkAnswer(dfwithColumn1, expectedDF1)
    checkAnswer(dfwithColumn1, expectedDF2)
  }

}
