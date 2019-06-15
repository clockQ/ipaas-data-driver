package com.pharbers.ipaas.data.driver.api.factory

import com.pharbers.ipaas.data.driver.api.model.{Operator, Plugin}
import com.pharbers.ipaas.data.driver.exceptions.PhOperatorException
import com.pharbers.ipaas.data.driver.api.work.{PhMapArgs, PhOperatorTrait2, PhPluginTrait2, PhStringArgs}

/** Operator实体工厂
  *
  * @param operator model.Operator 对象
  * @author dcs
  * @version 0.1
  * @since 2019/06/14 15:30
  */
case class PhOperatorFactory(operator: Operator) extends PhFactoryTrait[PhOperatorTrait2[Any]] {

    /** 构建 Operator 运行实例
      *
      * @throws PhOperatorException 构建算子时的异常
      * */
    override def inst(): PhOperatorTrait2[Any] = {
        import scala.collection.JavaConverters.mapAsScalaMapConverter

        val args = operator.getArgs match {
            case null => Map[String, PhStringArgs]().empty
            case one => one.asScala.map(x => (x._1, PhStringArgs(x._2))).toMap
        }

        try {
            val plugin = operator.getPlugin match {
                case null => Seq()
                case one: Plugin => Seq(getMethodMirror(one.getFactory)(one).asInstanceOf[PhFactoryTrait[PhPluginTrait2[Any]]].inst())
            }

            getMethodMirror(operator.getReference)(
                operator.getName,
                PhMapArgs(args),
                plugin
            ).asInstanceOf[PhOperatorTrait2[Any]]
        } catch {
            case e: Exception => throw PhOperatorException(List(operator.name), e)
        }
    }
}