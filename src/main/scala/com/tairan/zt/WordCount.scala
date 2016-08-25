package com.tairan.zt

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by hzzt on 2016/8/5.
 */
object WordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Scala WordCount").setMaster("spark://172.30.251.229:7077")
    val sc = new SparkContext(conf)
    sc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar")

    val textFile = sc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/README.txt")
    /*val count = textFile.count()
    println(count)
    textFile.first()
    val linesWithSpark = textFile.filter(line => line.contains("Spark"))
    linesWithSpark.foreach(line=>println(line))
    val longline = textFile.map(line => line.split(" ").size).reduce((a, b) => if (a > b) a else b)
    println(longline)*/
    val wordcount = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey((a, b) => a + b)
    val output = wordcount.collect()
    for (i <- 0 until output.length){
      println(output(i)._1+" "+output(i)._2)
    }
    sc.stop()
  }
}
