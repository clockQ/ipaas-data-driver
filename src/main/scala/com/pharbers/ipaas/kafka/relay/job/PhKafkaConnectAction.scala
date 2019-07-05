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

package com.pharbers.ipaas.kafka.relay.job

import com.pharbers.ipaas.data.driver.api.work._
import com.pharbers.ipaas.data.driver.exceptions.PhOperatorException
import com.pharbers.ipaas.data.driver.libs.log.PhLogDriver
import com.pharbers.ipaas.data.driver.libs.spark.PhSparkDriver

/** Kafka Connect Action 运行实体
  *
  * @param name        Kafka Connect Action 名字
  * @param defaultArgs 配置参数
  * {{{
  *  protocol: http // 调用的 Kafka Connect HTTP 协议，默认是http
  *  ip: 192.168.1.1 // 调用的 Kafka Connect HTTP ip
  *  port: 8080 // 调用的 Kafka Connect HTTP 端口
  * }}}
  * @param operatorLst Kafka Connect Action 包含的 Operator 列表
  * @author clock
  * @version 0.1
  * @since 2019/7/5 12:20
  */
case class PhKafkaConnectAction(name: String,
                                defaultArgs: PhMapArgs[PhWorkArgs[Any]],
                                operatorLst: Seq[PhOperatorTrait[String => Unit]])
        extends PhActionTrait {
    /** 调用的 Kafka Connect HTTP 协议 */
    val protocol: String = defaultArgs.getAs[PhStringArgs]("protocol") match {
        case Some(one) => one.get
        case None => "http"
    }
    /** 调用的 Kafka Connect HTTP ip */
    val ip: String = defaultArgs.getAs[PhStringArgs]("ip").get.get
    /** 调用的 Kafka Connect HTTP 端口 */
    val port: String = defaultArgs.getAs[PhStringArgs]("port").get.get

    /** Kafka Connect Action 执行入口
      *
      * @author clock
      * @version 0.1
      * @since 2019/7/5 12:20
      */
    def perform(pr: PhMapArgs[PhWorkArgs[Any]]): PhWorkArgs[Any] = {
        val log: PhLogDriver = pr.get("logDriver").asInstanceOf[PhLogDriverArgs].get

        val local = s"$protocol://$ip:$port"

        operatorLst.foreach { oper =>
            log.setInfoLog(oper.name)
            oper.perform(pr).get(local)
        }

        PhNoneArgs
    }
}
