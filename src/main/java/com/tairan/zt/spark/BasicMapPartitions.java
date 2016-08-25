package com.tairan.zt.spark;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by hzzt on 2016/8/10.
 */
public class BasicMapPartitions {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");

		JavaRDD<String> rdd = jsc.parallelize(Arrays.asList("KK6JKQ", "Ve3UoW", "kk6jlk", "W6BB"));
		JavaRDD<String> result = rdd.mapPartitions(new FlatMapFunction<Iterator<String>, String>() {
			@Override
			public Iterable<String> call(Iterator<String> input) throws Exception {
				ArrayList<String> content = new ArrayList();
				ArrayList<ContentExchange> cea = new ArrayList();
				HttpClient client = new HttpClient();
				try {
					client.start();
					while (input.hasNext()){
						ContentExchange exchange = new ContentExchange(true);

						exchange.setURI(new URI("http://qrzcq.com/call/" + input.next()));
						client.send(exchange);
						cea.add(exchange);
					}
					for (ContentExchange exchange:cea){
						exchange.waitForDone();
						content.add(exchange.getResponseContent());
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				return content;
			}
		});
		System.out.println(StringUtils.join(result.collect(),","));
	}
}
