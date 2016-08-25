
package com.tairan.zt.spark;

/*
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

*/
/**
 * Created by hzzt on 2016/8/9.
 *//*

public class BasicLoadJson {
	public static class Person implements Serializable {
		public String name;
		public Boolean lovesPandas = false;

	}

	public static class ParseJson implements FlatMapFunction<Iterator<String>, Person> {
		public Iterable<Person> call(Iterator<String> lines) throws Exception {
			ArrayList<Person> people = new ArrayList<Person>();
			ObjectMapper mapper = new ObjectMapper();
			while (lines.hasNext()) {
				String line = lines.next();
				try {
					people.add(mapper.readValue(line, Person.class));
				} catch (Exception e) {
					// Skip invalid input
				}
			}
			return people;
		}
	}

	public static class LikesPandas implements Function<Person, Boolean> {
		public Boolean call(Person person) {
			return person.lovesPandas;
		}
	}


	public static class WriteJson implements FlatMapFunction<Iterator<Person>, String> {
		public Iterable<String> call(Iterator<Person> people) throws Exception {
			ArrayList<String> text = new ArrayList<String>();
			ObjectMapper mapper = new ObjectMapper();
			while (people.hasNext()) {
				Person person = people.next();
				text.add(mapper.writeValueAsString(person));
			}
			return text;
		}
	}


	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf()
				.setAppName("WordCountHello")
				.setMaster("spark://172.30.251.229:7077");

		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		ctx.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar");

		JavaRDD<String> input = ctx.textFile("hdfs://172.30.251.229:9000/user/hzzt/input/pandainfo.json");

		JavaRDD<Person> result = input.mapPartitions(new ParseJson()).filter(new LikesPandas());

		JavaRDD<String> formatted = result.mapPartitions(new WriteJson());
		formatted.saveAsTextFile("hdfs://172.30.251.229:9000/user/hzzt/input/out");
		formatted.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				System.out.println(s);
			}
		});
	}
}
*/
public class BasicLoadJson {}