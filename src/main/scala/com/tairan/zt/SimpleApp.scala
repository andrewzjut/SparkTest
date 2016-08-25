package com.tairan.zt

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by hzzt on 2016/8/5.
 */
object SimpleApp {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SimpleApp").setMaster("spark://172.30.251.229:7077")
    val sc = new SparkContext(conf)
    sc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar")
    val logData = sc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/hello.txt", 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(_.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
    sc.stop()




  }
}
