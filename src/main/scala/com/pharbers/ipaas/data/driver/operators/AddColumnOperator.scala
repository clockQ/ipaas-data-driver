/*
 * This file is part of com.pharbers.ipaas-data-driver.
 *
 * com.pharbers.ipaas-data-driver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * com.pharbers.ipaas-data-driver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pharbers.ipaas.data.driver.operators

import org.apache.spark.sql.{Column, DataFrame}
import com.pharbers.ipaas.data.driver.api.work._

/** 添加新列的算子
  *
  * @author clock
  * @version 0.1
  * @since 2019/6/15 18:10
  * @example 默认参数例子
  * {{{
  * inDFName: actionName // 要作用的 DataFrame 名字
  * newColName: newCol // 新增的列名
  * }}}
  */
case class AddColumnOperator(name: String,
                             defaultArgs: PhMapArgs[PhWorkArgs[Any]],
                             pluginLst: Seq[PhPluginTrait[Column]])
        extends PhOperatorTrait[DataFrame] {
    /** 要作用的 DataFrame 名字 */
    val inDFName: String = defaultArgs.getAs[PhStringArgs]("inDFName").get.get
    /** 新增的列名列表 */
    val newColName: String = defaultArgs.getAs[PhStringArgs]("newColName").get.get

    override def perform(pr: PhMapArgs[PhWorkArgs[Any]]): PhWorkArgs[DataFrame] = {
        val inDF = pr.getAs[PhDFArgs](inDFName).get.get
        val func = pluginLst.head.perform(pr).get
        val outDF = inDF.withColumn(newColName, func)

        PhDFArgs(outDF)
    }
}