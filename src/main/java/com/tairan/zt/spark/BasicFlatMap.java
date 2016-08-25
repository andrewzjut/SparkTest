package com.tairan.zt.spark;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by hzzt on 2016/8/9.
 */
public class BasicFlatMap {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<String> rdd = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/README.txt");
		JavaRDD<String> words = rdd.flatMap(
				new FlatMapFunction<String, String>() {
					@Override
					public Iterable<String> call(String s) throws Exception {
						return Arrays.asList(s.split(" "));
					}
				}
		);
		Map<String,Long> result = words.countByValue();
		for (Map.Entry<String,Long> entry:result.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}
}
