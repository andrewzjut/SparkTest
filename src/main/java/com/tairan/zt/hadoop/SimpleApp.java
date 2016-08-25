package com.tairan.zt.hadoop;

/**
 * Created by hzzt on 2016/8/5.
 */
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {
	public static void main(String[] args) {
		String logFile = "hdfs://172.30.251.229:9000/user/hzzt/input/hello.txt"; // Should be some file on your system
		SparkConf conf = new SparkConf().setAppName("SimpleApp").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar");
		JavaRDD<String> logData = sc.textFile(logFile).cache();

		long numAs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("a"); }
		}).count();

		long numBs = logData.filter(new Function<String, Boolean>() {
			public Boolean call(String s) { return s.contains("b"); }
		}).count();

		System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
	}
}