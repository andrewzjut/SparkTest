package com.tairan.zt.spark;


import com.google.common.collect.Iterables;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzzt on 2016/8/10.
 */
public class IntersectByKey {
	public static <K, V> JavaPairRDD<K, V> intersectByKey(JavaPairRDD<K, V> rdd1, JavaPairRDD<K, V> rdd2) {
		JavaPairRDD<K, Tuple2<Iterable<V>,Iterable<V>>> grouped = rdd1.cogroup(rdd2);
		return grouped.flatMapValues(new Function<Tuple2<Iterable<V>, Iterable<V>>, Iterable<V>>() {
			@Override
			public Iterable<V> call(Tuple2<Iterable<V>, Iterable<V>> input) {
				ArrayList<V> al = new ArrayList<V>();
				if (!Iterables.isEmpty(input._1()) && !Iterables.isEmpty(input._2())) {
					Iterables.addAll(al, input._1());
					Iterables.addAll(al, input._2());
				}
				return al;
			}
		});
	}

	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		List<Tuple2<String, Integer>> input1 = new ArrayList();
		input1.add(new Tuple2("coffee", 1));
		input1.add(new Tuple2("coffee", 2));
		input1.add(new Tuple2("pandas", 3));
		List<Tuple2<String, Integer>> input2 = new ArrayList();
		input2.add(new Tuple2("pandas", 20));
		JavaPairRDD<String, Integer> rdd1 = jsc.parallelizePairs(input1);
		JavaPairRDD<String, Integer> rdd2 = jsc.parallelizePairs(input2);
		JavaPairRDD<String, Integer> result = intersectByKey(rdd1, rdd2);
		for (Tuple2<String, Integer> entry : result.collect()) {
			System.out.println(entry._1() + ":" + entry._2());
		}
		System.out.println("Done");
		jsc.stop();
	}
}
