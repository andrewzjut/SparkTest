package com.tairan.zt.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hzzt on 2016/8/8.
 */
public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar");

		List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6);
		JavaRDD<Integer> distData = jsc.parallelize(data);

		distData.foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				System.out.println("number:"+integer+1);
			}
		});

		JavaRDD<String> lines = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/test/README.md");
		JavaRDD<Integer> lineLength = lines.map(new Function<String, Integer>() {
			@Override
			public Integer call(String v1) throws Exception {
				return v1.length();
			}
		});

		int totalLength = lineLength.reduce(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});


		JavaRDD<Integer> lineLengths2 = lines.map(new GetLength());
		int totalLength2 = lineLengths2.reduce(new Sum());

		System.out.println(totalLength);
		System.out.println(totalLength2);
		lineLength.foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				System.out.println(integer);
			}
		});
		jsc.close();
	}
}

class GetLength implements Function<String, Integer> {

	@Override
	public Integer call(String v1) throws Exception {
		return v1.length();
	}
}

class Sum implements Function2<Integer, Integer, Integer> {

	@Override
	public Integer call(Integer v1, Integer v2) throws Exception {
		return v1 + v2;
	}
}