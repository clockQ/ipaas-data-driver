package com.pharbers.ipaas.data.driver.operators

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import com.pharbers.ipaas.data.driver.libs.spark.PhSparkDriver
import com.pharbers.ipaas.data.driver.plugins.ExprPlugin
import org.apache.spark.sql.DataFrame
import com.pharbers.ipaas.data.driver.api.work.{PhDFArgs, PhMapArgs, PhStringArgs}

class TestAddColumnOperator extends FunSuite with BeforeAndAfterAll {
    implicit var sd: PhSparkDriver = _
    var testDF: DataFrame = _

    override def beforeAll(): Unit = {
        sd = PhSparkDriver("test-driver")
        val tmp = sd.ss.implicits
        import tmp._

        testDF = List(
            ("name1", "prod1", "201801", 1),
            ("name2", "prod1", "201801", 2),
            ("name3", "prod2", "201801", 3),
            ("name4", "prod2", "201801", 4)
        ).toDF("NAME", "PROD", "DATE", "VALUE")

        require(sd != null)
        require(testDF != null)
    }

    test("add column") {
        val operator = AddColumnOperator(
            "AddColumnOperator",
            PhMapArgs(Map(
                "inDFName" -> PhStringArgs("inDFName"),
                "newColName" -> PhStringArgs("newColName")
            )),
            Seq(ExprPlugin(
                "ExprPlugin",
                PhMapArgs(Map(
                    "exprString" -> PhStringArgs("cast(VALUE as double)")
                )),
                Seq.empty
            ))
        )
        val result = operator.perform(PhMapArgs(Map("inDFName" -> PhDFArgs(testDF))))
        assert(result.toDFArgs.get.columns.contains("newColName"))
    }
}