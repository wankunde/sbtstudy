// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package com.wankun.parser.sql


import org.apache.commons.lang3.StringUtils

import scala.collection.mutable

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-04-12.
 */
case class Token(value: String, isKeyWord: Boolean)

class PrettySql(sql: String) {

  val limit = sql.length
  var position = 0
  var mark = position

  def nextToken(): String = {
    do {
      position = position + 1
    } while (sql(position) != ' ')
    val token = sql.substring(mark, position)
    mark = position
    token
  }

  /**
   * 判断pos 位置的token是否是关键字，如果是关键字，则
   * @param tokens
   * @param pos
   * @return
   */
  def transformTokens(tokens: Array[String], pos: Int): Array[Token] = {
    Array[Token]()
  }

  def trimSql(sql: String): String = {
//    var isComment = false
//    var after
//    for ((ch, pos) <- sql.zipWithIndex) {
//
//    }
//    val tmp =
//      for (ch <- sql if ch != '\n' && ch != '\r') {
//
//        ch
//      }
//
//    val res =
//      for ((ch, pos) <- tmp.zipWithIndex if ch != ' ' || (pos > 0 && tmp(pos - 1) != ' ')) yield ch
//    res.mkString
  }

  def formatSql(originSql: String): String = {
    val sql = trimSql(originSql)
    val res = StringBuilder.newBuilder

    var mark = 0
    var level = 0

    def appendNonKeyWord(startPos: Int, endPos: Int): Unit = {
      val buf = sql.substring(startPos, endPos)
      var i = 0
      var j = Math.max(Math.min(i + 50, buf.length - 1), 0)
      while (j < buf.length) {
        while (j < buf.length && buf(j) != ',') {
          j = j + 1
        }
        if (j > i) {
          res.append("    " * level + buf.substring(i, j))
          i = j
          j = Math.max(Math.min(i + 50, buf.length - 1), 0)
        }
      }
    }

    for ((ch, pos) <- sql.zipWithIndex) {
      //      println(ch + " -> " + pos)

      def findKeyWord(rootNode: Tree): Option[String] = {
        // 要判断关键字，后续的第一个字符必须为空格或EOF
        if (pos < sql.length - 1 && sql(pos + 1) != ' ') {
          return None
        }

        var currentNode = rootNode
        var backPos = pos
        var res: Option[String] = None
        var flag = true
        do {
          currentNode.getChild(sql(backPos)) match {
            case Some(childNode) if childNode.tokenEnd && backPos > 0 && sql(backPos - 1) == ' ' =>
              res = Some(sql.substring(backPos, pos + 1))
              flag = false

            case Some(childNode) if childNode.tokenEnd && backPos == 0 =>
              res = Some(sql.substring(backPos, pos + 1))
              flag = false

            case Some(childNode) if backPos > 0 =>
              backPos = backPos - 1
              currentNode = childNode

            case None =>
              flag = false
          }
        } while (flag)

        res
      }


      ch match {
        case '(' =>
          level = level + 1

        case ')' =>
          level = level - 1

        case _ =>
          findKeyWord(PrettySql.keyWordsTree) match {
            // 匹配到关键字
            // 1. flush 之前的buffer
            // 2. 匹配到函数关键字 AS, CASE WHEN 等，只需要把函数关键字的字母变成大写即可
            // 3. 新 buf = 换行 + (缩进 * level)
            case Some(keyword) =>
              appendNonKeyWord(mark, pos + 1 - keyword.length)
              if (PrettySql.FUNCTION_KEY_WORDS.contains(keyword)) {
                res.append(keyword)
              } else {
                res.append("\n" + "    " * level + StringUtils.rightPad(keyword, 8, ' '))
              }
              mark = pos + 1

            case None =>
          }
      }
    }

    if (mark < sql.length) {
      appendNonKeyWord(mark, sql.length)
    }

    res.mkString
  }

}

case class Tree(value: Char) {
  val childrenMap: mutable.Map[Char, Tree] = mutable.Map[Char, Tree]()

  def getChild(ch: Char): Option[Tree] = {
    val upperChar = ch.toUpper
    if (childrenMap.contains(upperChar)) {
      Some(childrenMap(upperChar))
    } else {
      None
    }
  }

  def getChildOrCompute(ch: Char): Tree = {
    val upperChar = ch.toUpper
    if (childrenMap.contains(upperChar)) {
      childrenMap(upperChar)
    } else {
      val child = Tree(upperChar)
      childrenMap += (upperChar -> child)
      child
    }
  }

  def tokenEnd: Boolean = childrenMap.isEmpty
}

object PrettySql {
  val SQL_KEY_WORDS = Array[String](
    "ALTER DATABASE",
    "ALTER TABLE",
    "ALTER VIEW",
    "CREATE DATABASE",
    "CREATE FUNCTION",
    "CREATE TABLE",
    "CREATE VIEW",
    "DROP DATABASE",
    "DROP FUNCTION",
    "DROP TABLE",
    "DROP VIEW",
    "REPAIR TABLE",
    "TRUNCATE TABLE",
    "USE",
    "INSERT INTO",
    "INSERT OVERWRITE",
    "LOAD",
    "WITH",
    "SELECT",
    "FROM",
    "WHERE",
//    "AND",
    "UNION",
    "INTERSECT",
    "EXCEPT",
    "ALL",
    "DISTINCT",
    "ORDER BY",
    "SORT BY",
    "CLUSTER BY",
    "DISTRIBUTE BY",
    "GROUP BY",
    "HAVING",
    "WINDOW",
    "OVER",
    "LIMIT",
    "JOIN",
    "ON",
    "LIKE",
    "AS",
    "TABLESAMPLE",
    "CASE",
    "WHEN",
    "PIVOT",
    "LATERAL VIEW",
    "EXPLAIN"
  )

  val FUNCTION_KEY_WORDS = Set[String](
    "INTERSECT",
    "EXCEPT",
    "ALL",
    "DISTINCT",
    "WINDOW",
    "OVER",
    "LIKE",
    "AS",
    "TABLESAMPLE",
    "CASE",
    "WHEN",
    "PIVOT",
    "LATERAL VIEW",
  )

  val keyWordsTree: Tree = {
    val header = Tree(' ')
    for (keyword <- SQL_KEY_WORDS) {
      addToTree(keyword, header)
    }
    header
  }

  def addToTree(keyword: String, header: Tree): Unit = {
    var node = header
    for (ch <- keyword.reverse) {
      node = node.getChildOrCompute(ch)
    }
  }

  def main(args: Array[String]): Unit = {

    val prettySql = new PrettySql("")
    val sql2 = prettySql.formatSql(
      s"""WITH storeinfo AS
         |  (-- `店铺信息`
         |SELECT DISTINCT DOMAIN,
         |                zone_name,
         |                customer_id,
         |                store_id,
         |                dt
         |   FROM tb_store.dim_store_meta
         |   WHERE dt=$${date | yyyyMMdd}),
         |     query_num AS
         |  (-- `query量`
         |SELECT count(query) AS querynum,
         |       store_id
         |   FROM tb_bot_conversation.dwd_bot_conversation_detail
         |   WHERE dt=$${date | yyyyMMdd}
         |     AND query<>''
         |   GROUP BY store_id),
         |     hit_num_fenzi AS
         |  (-- `问题识别率`
         |SELECT count(query) AS fenzinum,
         |       store_id
         |   FROM tb_bot_conversation.dwd_bot_conversation_detail
         |   WHERE intents[0].label <>'其他'
         |     AND dt=$${date | yyyyMMdd}
         |     AND query<>''
         |   GROUP BY store_id),
         |     valid_query AS
         |  (SELECT store_id,
         |          sum(query_count) AS query_count,
         |          sum(reply_count) AS reply_count,
         |          sum(reply_count)/sum(query_count) AS reply_rate
         |   FROM
         |     ( SELECT temp_stats_1.store_id,
         |              assistant_nick,
         |              buyer_nick,
         |              sum(temp_stats_1.query_count) AS query_count,
         |              sum(temp_stats_1.reply_count) AS reply_count
         |      FROM
         |        ( SELECT store_id,
         |                 assistant_nick,
         |                 buyer_nick,
         |                 alliance_id,
         |                 1 AS query_count,
         |                 if(sum(reply_count) > 0, 1, 0) AS reply_count
         |         FROM
         |           ( SELECT alliance_id,
         |                    store_id,
         |                    assistant_nick,
         |                    buyer_nick,
         |                    CASE
         |                        WHEN (action IN ('REPLY')
         |                              AND processing_result = 'SUCCESS'
         |                              AND reply_type <> 'DRT_GUARANTEED_ANSWER') THEN 1
         |                        ELSE 0
         |                    END AS reply_count
         |            FROM tb_bot_conversation.dwd_bot_conversation_detail
         |            WHERE dt = $${date | yyyyMMdd}
         |              AND candidate <> 'ACTION' ) AS temp_tb
         |         GROUP BY store_id,
         |                  assistant_nick,
         |                  buyer_nick,
         |                  alliance_id ) AS temp_stats_1
         |      GROUP BY temp_stats_1.store_id,
         |               assistant_nick,
         |               buyer_nick) AS temp_stats
         |   GROUP BY store_id),
         |     can_query AS
         |  (SELECT store_id,
         |          sum(query_count) AS query_count,
         |          sum(reply_count) AS reply_count,
         |          sum(reply_count)/sum(query_count) AS reply_rate
         |   FROM
         |     ( SELECT temp_stats_1.store_id,
         |              assistant_nick,
         |              buyer_nick,
         |              sum(temp_stats_1.query_count) AS query_count,
         |              sum(temp_stats_1.reply_count) AS reply_count
         |      FROM
         |        ( SELECT store_id,
         |                 assistant_nick,
         |                 buyer_nick,
         |                 alliance_id,
         |                 1 AS query_count,
         |                 if(sum(reply_count) > 0, 1, 0) AS reply_count
         |         FROM
         |           ( SELECT alliance_id,
         |                    store_id,
         |                    assistant_nick,
         |                    buyer_nick,
         |                    CASE
         |                        WHEN (action IN ('REPLY')
         |                              AND processing_result = 'SUCCESS')or(processing_result = 'FAIL'
         |                                                                   AND result_reason = 'EXPECTED') THEN 1
         |                        ELSE 0
         |                    END AS reply_count
         |            FROM tb_bot_conversation.dwd_bot_conversation_detail
         |            WHERE dt = $${date | yyyyMMdd}
         |              AND candidate <> 'ACTION' ) AS temp_tb
         |         GROUP BY store_id,
         |                  assistant_nick,
         |                  buyer_nick,
         |                  alliance_id ) AS temp_stats_1
         |      GROUP BY temp_stats_1.store_id,
         |               assistant_nick,
         |               buyer_nick) AS temp_stats
         |   GROUP BY store_id)
         |SELECT storeinfo.domain AS domain,
         |       storeinfo.zone_name AS zone_name,
         |       storeinfo.customer_id AS customer_id,
         |       storeinfo.store_id AS store_id,
         |       storeinfo.dt AS dt,
         |       query_num.querynum AS query_count,
         |       hit_num_fenzi.fenzinum/query_num.querynum AS hit_rate,
         |       valid_query.reply_count AS valid_reply_count,
         |       valid_query.reply_rate AS valid_reply_rate,
         |       can_query.reply_count AS can_reply_count,
         |       can_query.reply_rate AS can_reply_rate
         |FROM storeinfo
         |LEFT JOIN query_num ON storeinfo.store_id = query_num.store_id
         |LEFT JOIN hit_num_fenzi ON storeinfo.store_id = hit_num_fenzi.store_id
         |LEFT JOIN valid_query ON storeinfo.store_id = valid_query.store_id
         |LEFT JOIN can_query ON storeinfo.store_id = can_query.store_id
         |
         |
         |
         |""".stripMargin)
    println(sql2)

  }
}
