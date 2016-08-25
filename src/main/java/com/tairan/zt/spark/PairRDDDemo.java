package com.tairan.zt.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Created by hzzt on 2016/8/16.
 */
public class PairRDDDemo {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");
		JavaRDD<String> lines = jsc.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/README.txt");

		PairFunction<String, String, String> keyData = new PairFunction<String, String, String>() {
			@Override
			public Tuple2<String, String> call(String s) throws Exception {
				return new Tuple2<>(s.split(" ")[0], s);
			}
		};
		JavaPairRDD<String, String> pairs = lines.mapToPair(keyData);
		/*pairs.foreach(new VoidFunction<Tuple2<String, String>>() {
			@Override
			public void call(Tuple2<String, String> tuple2) throws Exception {
				System.out.println(tuple2._1() + "****" + tuple2._2());
			}
		});*/

		Function<Tuple2<String, String>, Boolean> longWordFilter = new Function<Tuple2<String, String>, Boolean>() {
			@Override
			public Boolean call(Tuple2<String, String> v1) throws Exception {
				return (v1._2().length() < 20);
			}
		};
		/*JavaPairRDD<String, String> result = pairs.filter(longWordFilter);
		result.foreach(new VoidFunction<Tuple2<String, String>>() {
			@Override
			public void call(Tuple2<String, String> tuple2) throws Exception {
				System.out.println(tuple2._1() + "****" + tuple2._2());
			}
		});*/


		System.out.println("----------------------------");
		StringComparator comp = new StringComparator();
		JavaPairRDD<String, String> result = pairs.sortByKey(comp);
		result.foreach(new VoidFunction<Tuple2<String, String>>() {
			@Override
			public void call(Tuple2<String, String> tuple2) throws Exception {
				System.out.println(tuple2._1() + "****" + tuple2._2());
			}
		});

	}
}
class StringComparator implements Comparator<String> ,Serializable{

	@Override
	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}
}
