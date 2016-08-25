package com.tairan.zt.spark;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

/**
 * Created by hzzt on 2016/8/10.
 */
public class BasicLoadSequenceFile {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar");

		JavaPairRDD<Text,IntWritable> input = jsc.sequenceFile("hdfs://172.30.251.229:9000/user/hzzt/input/sequence.txt",Text.class,IntWritable.class);
		JavaPairRDD<String,Integer> result = input.mapToPair(new ConvertToNativeTypes());

		List<Tuple2<String,Integer>> resultList = result.collect();
		for (Tuple2<String,Integer> record:resultList){
			System.out.println(record);
		}

	}
	public static class ConvertToNativeTypes implements PairFunction<Tuple2<Text,IntWritable>,String,Integer>{

		@Override
		public Tuple2<String, Integer> call(Tuple2<Text, IntWritable> record) throws Exception {
			return new Tuple2<>(record._1().toString(),record._2().get());
		}
	}
}
