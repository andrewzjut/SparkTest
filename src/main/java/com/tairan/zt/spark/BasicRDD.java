package com.tairan.zt.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hzzt on 2016/8/16.
 */
public class BasicRDD {
	public static void main(String[] args) {
		final SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i = 1; i < 6; i++) {
			array.add(i);
		}
		JavaRDD<Integer> rdd1 = jsc.parallelize(array);
//map
		rdd1.map(new Function<Integer, Integer>() {
			@Override
			public Integer call(Integer v1) throws Exception {
				return v1 + 2;
			}
		}).foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				System.out.println(integer);
			}
		});
		rdd1.map(new Function<Integer, Iterable<Integer>>() {
			@Override
			public Iterable<Integer> call(Integer v1) throws Exception {
				ArrayList<Integer> al = new ArrayList<Integer>();
				for (int i = 1; i <= v1; i++) {
					al.add(i);
				}
				return al;
			}
		}).foreach(new VoidFunction<Iterable<Integer>>() {
			@Override
			public void call(Iterable<Integer> integers) throws Exception {
				System.out.println(integers);
			}
		});
//mapPartition
		ArrayList<Tuple2<String, String>> arrayList = new ArrayList<>();
		arrayList.add(new Tuple2<String, String>("kpop", "female"));
		arrayList.add(new Tuple2<String, String>("zorro", "male"));
		arrayList.add(new Tuple2<String, String>("mobin", "male"));
		arrayList.add(new Tuple2<String, String>("lucy", "female"));

		JavaRDD rdd2 = jsc.parallelize(arrayList);
		FlatMapFunction<Iterator<Tuple2<String, String>>, String> partitionFun = new FlatMapFunction<Iterator<Tuple2<String, String>>, String>() {
			@Override
			public Iterable<String> call(Iterator<Tuple2<String, String>> iterator) throws Exception {
				ArrayList<String> women = new ArrayList<>();
				while (iterator.hasNext()) {
					Tuple2<String, String> tuple2 = iterator.next();
					switch (tuple2._2()) {
						case "female":
							women.add(tuple2._1());
						default:
					}
				}
				return women;
			}
		};
		JavaRDD<String> rdd3 = rdd2.mapPartitions(partitionFun);
		rdd3.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				System.out.println(s);
			}
		});






	}
}
