package com.tairan.zt.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;


/**
 * Created by hzzt on 2016/8/10.
 */
public class BasicSaveSequenceFile {
	public static class ConvertToWritableTypes implements PairFunction<Tuple2<String,Integer>,Text,IntWritable>{

		@Override
		public Tuple2<Text, IntWritable> call(Tuple2<String, Integer> record) throws Exception {
			return new Tuple2(new Text(record._1()),new IntWritable(record._2()));
		}
	}
		public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		List<Tuple2<String, Integer>> input = new ArrayList();
		input.add(new Tuple2("coffee", 1));
		input.add(new Tuple2("coffee", 2));
		input.add(new Tuple2("pandas", 3));
		JavaPairRDD<String, Integer> rdd = jsc.parallelizePairs(input);
		JavaPairRDD<Text,IntWritable> result = rdd.mapToPair(new ConvertToWritableTypes());
		result.
		saveAsHadoopFile("hdfs://172.30.251.229:9000/user/hzzt/input/sequence.txt",Text.class,IntWritable.class, SequenceFileOutputFormat.class);
	}
}
