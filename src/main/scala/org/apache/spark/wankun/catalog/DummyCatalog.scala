// Copyright 2019 Leyantech Ltd. All Rights Reserved.
package org.apache.spark.wankun.catalog

import org.apache.spark.sql.connector.catalog.CatalogPlugin
import org.apache.spark.sql.util.CaseInsensitiveStringMap

/**
 * @author kun.wan, <kun.wan@leyantech.com>
 * @date 2021-04-06.
 */
class DummyCatalog extends CatalogPlugin {
  override def initialize(name: String, options: CaseInsensitiveStringMap): Unit = {
    _name = name
  }
  private var _name: String = null
  override def name(): String = _name
  override def defaultNamespace(): Array[String] = Array("a", "b")
}
