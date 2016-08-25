package com.tairan.zt.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by hzzt on 2016/8/8.
 */
public final class BasicAvg {

	public static class AvgCount implements Serializable {
		public int total_;
		public int num_;

		public AvgCount(int total_, int num_) {
			this.total_ = total_;
			this.num_ = num_;
		}

		public float avg() {
			return total_ / (float) num_;
		}

		@Override
		public String toString() {
			return "AvgCount{" +
					"total_=" + total_ +
					", num_=" + num_ +
					'}';
		}
	}

	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");




	JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(1, 2, 3, 4), 2);
	Function2<AvgCount, Integer, AvgCount> addAndCount = new Function2<AvgCount, Integer, AvgCount>() {
		@Override
		public AvgCount call(AvgCount v1, Integer v2) throws Exception {
			v1.total_ += v2;
			v1.num_ += 1;
			return v1;
		}
	};

	Function2<AvgCount, AvgCount, AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>() {
		@Override
		public AvgCount call(AvgCount v1, AvgCount v2) throws Exception {
			v1.total_ += v2.total_;
			v1.num_ += v2.num_;
			return v1;
		}
	};

	AvgCount initial = new AvgCount(1, 1);
	AvgCount result = rdd.aggregate(initial, addAndCount, combine);
	System.out.println(result.num_);
	jsc.stop();
}
}
