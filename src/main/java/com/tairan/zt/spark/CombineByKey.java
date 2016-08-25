package com.tairan.zt.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by hzzt on 2016/8/15.
 */
public class CombineByKey {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");


	}
}
