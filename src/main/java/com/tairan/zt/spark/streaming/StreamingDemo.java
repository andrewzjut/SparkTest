package com.tairan.zt.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;
import org.apache.spark.api.java.StorageLevels;


import java.util.Arrays;

/**
 * Created by hzzt on 2016/8/19.
 */
public class StreamingDemo {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(3));
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream("172.30.251.229",9999,StorageLevels.MEMORY_AND_DISK_SER);
		JavaDStream<String> words = lines.flatMap(
				new FlatMapFunction<String, String>() {
					@Override
					public Iterable<String> call(String s) throws Exception {
						return Arrays.asList(s.split(" "));
					}
				}
		);

		JavaPairDStream<String,Integer> pairs = words.mapToPair(
				new PairFunction<String, String, Integer>() {
					@Override
					public Tuple2<String, Integer> call(String s) throws Exception {
						return new Tuple2<String, Integer>(s,1);
					}
				}
		);

		JavaPairDStream<String,Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer integer, Integer integer2) throws Exception {
				return integer+integer2;
			}
		});
		wordCounts.print();
		jssc.start();
		jssc.awaitTermination();
	}
}
