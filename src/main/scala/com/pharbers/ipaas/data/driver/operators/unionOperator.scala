package com.pharbers.ipaas.data.driver.operators

import com.pharbers.ipaas.data.driver.api.work._
import org.apache.spark.sql.functions.expr

/**
  * @author dcs
  * @param $args
  * @tparam T
  * @note
  */
case class unionOperator(plugin: PhPluginTrait, name: String, defaultArgs: PhWorkArgs[_]) extends PhOperatorTrait {

    override def perform(pr: PhWorkArgs[_]): PhWorkArgs[_] = {
        val defaultMapArgs = defaultArgs.toMapArgs[PhWorkArgs[_]]
        val prMapArgs = pr.toMapArgs[PhWorkArgs[_]]
        val inDFName = defaultMapArgs.getAs[PhStringArgs]("inDFName").get.get
        val unionDFName = defaultMapArgs.getAs[PhStringArgs]("unionDFName").get.get
        val inDF = prMapArgs.getAs[PhDFArgs](inDFName).get.get
        val unionDF = prMapArgs.getAs[PhDFArgs](unionDFName).get.get
        val outDF = inDF.unionByName(unionDF)

        PhDFArgs(outDF)
    }
}