package com.pharbers.ipaas.data.driver.libs.spark

import org.scalatest.{BeforeAndAfterAll, FunSuite}

class testSparkDriver extends FunSuite with BeforeAndAfterAll {
    implicit var sd: PhSparkDriver = PhSparkDriver("testSparkDriver")
    import util._

    val mongodbHost: String = "192.168.100.176"
    val mongodbPort: String = "27017"
    val databaseName: String = "pharbers-max-repository"
    val collName: String = "chc"
    val falseCollName: String = "false"

    val fileName: String = "/test/CPA&GYCX/CPA_GYC_PHA.csv"
    val falseFileName: String = "/test/CPA&GYCX/false.csv"

    val parquetName: String = "/repository/pha"
    val falseParquetName: String = "/repository/false"

    def beforeAll(configMap: Map[String, Any]) {
        sd = PhSparkDriver("testSparkDriver")
        require(sd != null)
    }

    def afterAll(configMap: Map[String, Any]) {
        sd.stopSpark()
    }

    test("mongo2RDD") {
        val trueResult = sd.setUtil(mongo2RDD()).mongo2RDD(mongodbHost, mongodbPort, databaseName, collName)
        assert(trueResult.count() != 0)

        val falseResult = sd.setUtil(mongo2RDD()).mongo2RDD(mongodbHost, mongodbPort, databaseName, falseCollName)
        assert(falseResult.count() == 0)
    }

    test("readCsv") {
        val trueResult = sd.setUtil(readCsv()).readCsv(fileName)
        assert(trueResult.count() != 0)

        try{
            sd.setUtil(readCsv()).readCsv(falseFileName)
        } catch {
            case _: org.apache.spark.sql.AnalysisException => Unit
            case ex: Exception => throw ex
        }
    }

    test("readMongo") {
        val trueResult = sd.setUtil(readMongo()).readMongo(mongodbHost, mongodbPort, databaseName, collName)
        assert(trueResult.count() != 0)

        val falseResult = sd.setUtil(readMongo()).readMongo(mongodbHost, mongodbPort, databaseName, falseCollName)
        assert(falseResult.count() == 0)
    }

    test("readParquet") {
        val trueResult = sd.setUtil(readParquet()).readParquet(parquetName)
        assert(trueResult.count() != 0)

        try{
            sd.setUtil(readParquet()).readParquet(falseParquetName)
        } catch {
            case _: org.apache.spark.sql.AnalysisException => Unit
            case ex: Exception => throw ex
        }
    }
}
