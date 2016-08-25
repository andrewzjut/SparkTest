package com.tairan.zt.spark;

/**
 * Created by hzzt on 2016/8/9.
 */

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Serializable;
import scala.Tuple2;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

public class BasicJoinCsv {

	public static class ParseLine implements PairFunction<String, Integer, String[]> {
		public Tuple2<Integer, String[]> call(String line) throws Exception {
			CSVReader reader = new CSVReader(new StringReader(line));
			String[] elements = reader.readNext();
			Integer key = Integer.parseInt(elements[0]);
			return new Tuple2(key, elements);
		}
	}

	public static void main(String[] args) throws Exception {
		/*if (args.length != 3) {
			throw new Exception("Usage BasicJoinCsv sparkMaster csv1 csv2");
		}
		String master = args[0];
		String csv1 = args[1];
		String csv2 = args[2];*/
		BasicJoinCsv jsv = new BasicJoinCsv();
		jsv.run();

	}

	public void run() throws Exception {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<String> csvFile1 = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/int_string.csv");
		csvFile1.collect();
		JavaRDD<String> csvFile2 = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/favourite_animals.csv");
		csvFile2.collect();
		JavaPairRDD<Integer, String[]> keyedRDD2 = csvFile1.mapToPair(new ParseLine());
		JavaPairRDD<Integer, String[]> keyedRDD1 = csvFile2.mapToPair(new ParseLine());

		JavaPairRDD<Integer, Tuple2<String[], String[]>> result = keyedRDD1.join(keyedRDD2);
		List<Tuple2<Integer, Tuple2<String[], String[]>>> resultCollection = result.collect();
		System.out.println(resultCollection.get(0));
		jsc.close();
	}
}