package com.tairan.zt.spark;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class BasicAvgMapPartitions {
	class AvgCount implements Serializable {
		public AvgCount() {
			total_ = 0;
			num_ = 0;
		}
		public AvgCount(Integer total, Integer num) {
			total_ = total;
			num_ = num;
		}
		public AvgCount merge(Iterable<Integer> input) {
			for (Integer elem : input) {
				num_ += 1;
				total_ += elem;
			}
			return this;
		}
		public Integer total_;
		public Integer num_;
		public float avg() {
			return total_ / (float) num_;
		}
	}

	public static void main(String[] args) throws Exception {
		String master;
		if (args.length > 0) {
			master = args[0];
		} else {
			master = "spark://172.30.251.229:7077";
		}
		BasicAvgMapPartitions bamp = new BasicAvgMapPartitions();
		bamp.run(master);
	}

	public void run(String master) {
		JavaSparkContext sc = new JavaSparkContext(
				master, "basicavgmappartitions", "/tairan/spark","D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");
		JavaRDD<Integer> rdd = sc.parallelize(
				Arrays.asList(1, 2, 3, 4),1);
		FlatMapFunction<Iterator<Integer>, AvgCount> setup = new FlatMapFunction<Iterator<Integer>, AvgCount>() {
			@Override
			public Iterable<AvgCount> call(Iterator<Integer> input) {
				AvgCount a = new AvgCount(1, 1);
				while (input.hasNext()) {
					a.total_ += input.next();
					a.num_ += 1;
				}
				ArrayList<AvgCount> ret = new ArrayList();
				ret.add(a);
				return ret;
			}
		};
		Function2<AvgCount, AvgCount, AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>() {
			@Override
			public AvgCount call(AvgCount a, AvgCount b) {
				a.total_ += b.total_;
				a.num_ += b.num_;
				return a;
			}
		};

		rdd.mapPartitions(setup).collect();


	}
}