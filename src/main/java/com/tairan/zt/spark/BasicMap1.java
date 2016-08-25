package com.tairan.zt.spark;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;

import java.util.*;

/**
 * Created by hzzt on 2016/8/10.
 */
public class BasicMap1 {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(1, 2, 3, 4), 2);

		final JavaRDD<Integer> result = rdd.map(new Function<Integer, Integer>() {
			@Override
			public Integer call(Integer v1) throws Exception {
				return v1 * v1;
			}
		});
		System.out.println(StringUtils.join(result.collect(), ","));

		rdd.foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				System.out.println(integer);
			}
		});

		JavaDoubleRDD doubleRDD = rdd.mapToDouble(new DoubleFunction<Integer>() {
			@Override
			public double call(Integer integer) throws Exception {
				return (double) integer * integer;
			}
		});
		System.out.println(StringUtils.join(doubleRDD.collect(), ","));

		final Integer sum = rdd.fold(0, new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer integer, Integer integer2) throws Exception {
				return integer + integer2;
			}
		});

		System.out.println(sum);

		Integer mul = rdd.fold(1, new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer integer, Integer integer2) throws Exception {
				return integer * integer2;
			}
		});
		System.out.println(mul);


		JavaRDD<Integer> rdd1 = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3);

			Function2 addIndex = new Function2<Integer, Iterator<Integer>, Iterator<String>>() {
				@Override
				public Iterator<String> call(Integer v1, Iterator<Integer> v2) throws Exception {
					ArrayList<String> content = new ArrayList<>();
					while (v2.hasNext()) {
						content.add("分区" + v1 + ":" + v2.next());
					}
					return content.iterator();
				}
		};

		JavaRDD<String> rdd2 = rdd1.mapPartitionsWithIndex(addIndex, true);

		rdd2.collect();
		rdd2.foreach(new VoidFunction<String>() {
			@Override
			public void call(String string) throws Exception {
				System.out.println(string);
			}
		});

		rdd1.flatMap(
				new FlatMapFunction<Integer, Integer>() {
			@Override
			public Iterable<Integer> call(Integer integer) throws Exception {
				ArrayList<Integer> array = new ArrayList<Integer>();
				for (int i = 1; i <= integer; i++) {
					array.add(i);
				}
				return array;
			}
		}).foreach(new VoidFunction<Integer>() {
			@Override
			public void call(Integer integer) throws Exception {
				System.out.println(integer);
			}
		});

		Tuple2<Integer, Integer> t1 = new Tuple2<>(1, 2);
		Tuple2<Integer, Integer> t2 = new Tuple2<>(3, 4);
		Tuple2<Integer, Integer> t3 = new Tuple2<>(3, 6);

		List<Tuple2<Integer, Integer>> list = new ArrayList<>();
		list.add(t1);
		list.add(t2);
		list.add(t3);


		JavaPairRDD<Integer, Integer> rdd3 = jsc.parallelizePairs(list);

		rdd3.foreach(new VoidFunction<Tuple2<Integer, Integer>>() {
			@Override
			public void call(Tuple2<Integer, Integer> input) throws Exception {
				System.out.println(input._1() + ":" + input._2());
			}
		});

		rdd3.flatMapValues(new Function<Integer, Iterable<Integer>>() {

			@Override
			public Iterable<Integer> call(Integer v1) throws Exception {
				ArrayList<Integer> al = new ArrayList<Integer>();
				if (v1<5){
					for (int i = v1;i<=5;i++){
						al.add(i);
					}
				}
				return al;
			}
		}).foreach(new VoidFunction<Tuple2<Integer, Integer>>() {
			@Override
			public void call(Tuple2<Integer, Integer> input) throws Exception {
				System.out.println(input._1()+"::"+input._2());
			}
		});

		rdd3.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}).foreach(new VoidFunction<Tuple2<Integer, Integer>>() {
			@Override
			public void call(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
				System.out.println("key:"+integerIntegerTuple2._1()+"sum:"+integerIntegerTuple2._2());
			}
		});



		JavaRDD<Integer> rdd4 = jsc.parallelize(Arrays.asList(1,2,3,4,5,6),1);
		Function2<Integer,Integer,Integer> getMax =new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return Math.max(v1, v2);
			}
		};
		Function2<Integer,Integer,Integer> getSum = new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		};
		Integer out =  rdd4.aggregate(0, getMax, getSum);
		System.out.println("****************"+out);

		JavaRDD<String> rdd5 = jsc.parallelize(Arrays.asList("12","23","345",""),2);
		System.out.println(rdd5.aggregate("", new Function2<String, String, String>() {
			@Override
			public String call(String v1, String v2) throws Exception {
				return String.valueOf(Math.min(v1.length(), v2.length()));
			}
		}, new Function2<String, String, String>() {
			@Override
			public String call(String v1, String v2) throws Exception {
				return v1+v2;
			}
		}));


		Tuple2<String,Integer> tuple1 = new Tuple2<>("cat",2);
		Tuple2<String,Integer> tuple2 = new Tuple2<>("cat",5);
		Tuple2<String,Integer> tuple3 = new Tuple2<>("mouse", 4);
		Tuple2<String,Integer> tuple4 = new Tuple2<>("cat", 12);
		Tuple2<String,Integer> tuple5 = new Tuple2<>("dog", 12);
		Tuple2<String,Integer> tuple6 = new Tuple2<>("mouse", 2);

		ArrayList<Tuple2<String,Integer>> arrayList = new ArrayList<>();
		arrayList.add(tuple1);
		arrayList.add(tuple2);
		arrayList.add(tuple3);
		arrayList.add(tuple4);
		arrayList.add(tuple5);
		arrayList.add(tuple6);

		JavaPairRDD<String,Integer> rdd6 = jsc.parallelizePairs(arrayList,2);
		rdd6.aggregateByKey(100,
				new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return Math.max(v1,v2);
			}
		},
				new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}).foreach(new VoidFunction<Tuple2<String, Integer>>() {
			@Override
			public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
				System.out.println("animal name:"+stringIntegerTuple2._1()+" numbers:"+stringIntegerTuple2._2());
			}
		});


		JavaRDD<Integer> rdd7 = jsc.parallelize(Arrays.asList(1,2,3,4,5));
		JavaRDD<Integer> rdd8 = jsc.parallelize(Arrays.asList(6,7,8,9,10));

		JavaPairRDD<Integer,Integer> rdd9 = rdd7.cartesian(rdd8);
		rdd9.foreach(new VoidFunction<Tuple2<Integer, Integer>>() {
			@Override
			public void call(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
				System.out.println(integerIntegerTuple2._1()+" "+integerIntegerTuple2._2());
			}
		});

		JavaRDD<Integer> b = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 2, 4, 2, 1, 1, 1, 1, 1));
		Map<Integer,Long> map = b.countByValue();
		Set set = map.keySet();
		for (Map.Entry<Integer,Long> entry:map.entrySet()){
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

		for (Integer key : map.keySet()) {
			System.out.println("Key = " + key+ ", Value = " +map.get(key));
		}
		Iterator<Map.Entry<Integer, Long>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {

			Map.Entry<Integer, Long> entry = entries.next();

			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

		}



		jsc.close();
	}
}
