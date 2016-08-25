package com.tairan.zt.spark;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by hzzt on 2016/8/18.
 */
public class AdvancedDemo {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<String> rdd = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/README.txt");

		final Accumulator<Integer> blankLines = jsc.accumulator(0);
		JavaRDD<String> callSigns = rdd.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				if (s.equals("")){
					blankLines.add(1);
				}
				return Arrays.asList(s.split(" "));
			}
		});
		callSigns.saveAsTextFile("hdfs://172.30.251.229:9000/user/hzzt/input/output.txt");
		System.out.println(blankLines.value());
		callSigns.checkpoint();
	}
}
