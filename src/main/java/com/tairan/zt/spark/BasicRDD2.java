package com.tairan.zt.spark;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by hzzt on 2016/8/19.
 */
public class BasicRDD2 {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), 3);
		Function<Integer, String> evenOrOdd = new Function<Integer, String>() {
			@Override
			public String call(Integer integer) throws Exception {
				return (integer % 2 == 0 ? "even" : "odd");
			}
		};
		JavaPairRDD<String, Iterable<Integer>> rdd1 = rdd.groupBy(evenOrOdd);

		rdd1.foreach(new VoidFunction<Tuple2<String, Iterable<Integer>>>() {
			@Override
			public void call(Tuple2<String, Iterable<Integer>> input) throws Exception {
				System.out.println(input._1() + "：" + StringUtils.join(input._2().iterator(), ","));
			}
		});

		System.out.println(rdd1.collect().toString());
		JavaRDD<String> rdd2 = jsc.parallelize(Arrays.asList("dog", "tiger", "lion", "cat", "spider", "eagle"), 2);

		JavaPairRDD<Integer, String> rdd3 = rdd2.keyBy(new Function<String, Integer>() {
			@Override
			public Integer call(String s) throws Exception {
				return s.length();
			}
		});

		JavaPairRDD<Integer, Iterable<String>> rdd4 = rdd3.groupByKey();
		rdd4.foreach(new VoidFunction<Tuple2<Integer, Iterable<String>>>() {
			@Override
			public void call(Tuple2<Integer, Iterable<String>> input) throws Exception {
				System.out.println(input._1() + "：" + StringUtils.join(input._2().iterator(), ","));
			}
		});



		JavaPairRDD<Integer, String> rdd5 = rdd3.mapValues(new Function<String, String>() {
			@Override
			public String call(String s) throws Exception {
				return "x" + s + "x";
			}
		});
		System.out.println(rdd5.collect().toString());

		FlatMapFunction<Iterator<Integer>, Tuple2<Integer, Integer>> func = new FlatMapFunction<Iterator<Integer>, Tuple2<Integer, Integer>>() {
			@Override
			public Iterable<Tuple2<Integer, Integer>> call(Iterator<Integer> input) throws Exception {
				ArrayList<Tuple2<Integer, Integer>> al = new ArrayList<Tuple2<Integer, Integer>>();
				Integer pre = input.next();
				while (input.hasNext()) {
					Integer cur = input.next();
					al.add(new Tuple2<Integer, Integer>(pre, cur));
					pre = cur;
				}
				return al;
			}
		};

		JavaRDD<Tuple2<Integer, Integer>> rdd6 = rdd.mapPartitions(func);
		System.out.println(rdd6.collect().toString());

		PairFunction<Integer, Integer, Integer> func2 = new PairFunction<Integer, Integer, Integer>() {
			@Override
			public Tuple2<Integer, Integer> call(Integer integer) throws Exception {
				return new Tuple2<>(integer, integer + 10);
			}
		};

		JavaPairRDD<Integer, Integer> rdd7 = rdd.mapToPair(func2);

		JavaRDD<Integer> rdd8 = rdd.mapPartitions(new FlatMapFunction<Iterator<Integer>, Integer>() {
			@Override
			public Iterable<Integer> call(Iterator<Integer> input) throws Exception {
				ArrayList<Integer> al = new ArrayList<>();
				while (input.hasNext()) {
					Integer cur = input.next();
					for (int i = 0; i < new Random().nextInt(10); i++) {
						al.add(cur);
					}
				}
				return al;
			}
		});
		System.out.println(rdd8.collect().toString());
	}
}
