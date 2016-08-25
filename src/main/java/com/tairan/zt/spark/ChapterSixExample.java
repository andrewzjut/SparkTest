package com.tairan.zt.spark;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkFiles;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.util.StatCounter;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import scala.Tuple2;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzzt on 2016/8/10.
 */
public class ChapterSixExample {
	public static class SumInts implements Function2<Integer, Integer, Integer> {


		@Override
		public Integer call(Integer integer, Integer integer2) throws Exception {
			return integer + integer2;
		}
	}

	public static class VerifyCallLogs implements Function<CallLog[], CallLog[]> {

		@Override
		public CallLog[] call(CallLog[] callLogs) throws Exception {
			ArrayList<CallLog> res = new ArrayList<>();
			if (callLogs != null) {
				for (CallLog callLog : callLogs) {
					if (callLog != null && callLog.mylat != null && callLog.mylong != null) {
						res.add(callLog);
					}
				}
			}
			return res.toArray(new CallLog[0]);
		}
	}

	public static void main(String[] args) throws Exception {
		SparkConf sparkConf = new SparkConf().setAppName("JavaSpark").setMaster("spark://172.30.251.229:7077");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.addJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\sparktest.jar");

		final String inputFile1 = "hdfs://172.30.251.229:9000/user/hzzt/input/fake_logs/log1.log";
		String inputFile2 = "hdfs://172.30.251.229:9000/user/hzzt/input/fake_logs/log2.log";
		String outputFile = "hdfs://172.30.251.229:9000/user/hzzt/input/out";
		JavaRDD<String> rdd = jsc.textFile(inputFile1);
		final Accumulator<Integer> count = jsc.accumulator(0);
		rdd.foreach(new VoidFunction<String>() {
			@Override
			public void call(String s) throws Exception {
				if (s.contains("KK6JKQ")) {
					count.add(1);
				}
			}
		});
		System.out.println("Lines with 'KK6JKQ': " + count.value());
		final Accumulator<Integer> blankLines = jsc.accumulator(0);
		JavaRDD<String> callSigns = rdd.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String s) throws Exception {
				if (s.equals("")) {
					blankLines.add(1);
				}
				return Arrays.asList(s.split(" "));
			}
		});

		callSigns.saveAsTextFile(outputFile + "/callsigns");
		System.out.println("Blank Lines: " + blankLines.value());

		final Accumulator<Integer> validSignCount = jsc.accumulator(0);
		final Accumulator<Integer> invalidSignCount = jsc.accumulator(0);

		JavaRDD<String> validCallSigns = callSigns.filter(new Function<String, Boolean>() {
			@Override
			public Boolean call(String s) throws Exception {
				Pattern p = Pattern.compile("\\A\\d?\\p{Alpha}{1,2}\\d{1,4}\\p{Alpha}{1,3}\\Z");
				Matcher m = p.matcher(s);
				boolean b = m.matches();
				if (b) {
					validSignCount.add(1);
				} else {
					invalidSignCount.add(1);
				}
				return b;
			}
		});

		JavaPairRDD<String, Integer> contactCounts = validCallSigns.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String, Integer>(s, 1);
			}
		}).reduceByKey(new org.apache.spark.api.java.function.Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer integer, Integer integer2) throws Exception {
				return integer + integer2;
			}
		});

		contactCounts.count();
		if (invalidSignCount.value() < 0.1 * validSignCount.value()) {
			contactCounts.saveAsTextFile(outputFile + "/contactCount");
		} else {
			System.out.println("Too many errors " + invalidSignCount.value() + " for " + validSignCount.value());
			System.exit(1);
		}


		final Broadcast<String[]> signPrefixes = jsc.broadcast(loadCallSignTable());

		JavaPairRDD<String, Integer> countryContactCounts = contactCounts.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(Tuple2<String, Integer> callSignCount) throws Exception {
				String sign = callSignCount._1();
				String country = lookupCountry(sign, signPrefixes.value());
				return new Tuple2<String, Integer>(country, callSignCount._2());
			}
		}).reduceByKey(new SumInts());
		countryContactCounts.saveAsTextFile(outputFile + "/countries.txt");
		System.out.println("Saved country contact counts as a file");

		JavaPairRDD<String, CallLog[]> contactsContactLists = validCallSigns.mapPartitionsToPair(
				new PairFlatMapFunction<Iterator<String>, String, CallLog[]>() {
					@Override
					public Iterable<Tuple2<String, CallLog[]>> call(Iterator<String> input) throws Exception {
						ArrayList<Tuple2<String, CallLog[]>> callsignQsos = new ArrayList<>();
						ArrayList<Tuple2<String, ContentExchange>> requests = new ArrayList<>();
						ObjectMapper mapper = createMapper();
						HttpClient client = new HttpClient();
						try {
							client.start();
							while (input.hasNext()) {
								requests.add(createRequestForSign(input.next(), client));
							}
							for (Tuple2<String, ContentExchange> signExchange : requests) {
								callsignQsos.add(fetchResultFromRequest(mapper, signExchange));
							}
						} catch (Exception e) {
						}
						return callsignQsos;
					}
				}
		);

		System.out.println(StringUtils.join(contactsContactLists.collect(), ","));
		String distScript = System.getProperty("user.dir") + "/src/R/finddistance.R";
		String distScriptName = "finddistance.R";
		jsc.addFile(distScript);
		JavaRDD<String> pipeInputs = contactsContactLists.values().map(new VerifyCallLogs()).flatMap(
				new FlatMapFunction<CallLog[], String>() {
					public Iterable<String> call(CallLog[] calls) {
						ArrayList<String> latLons = new ArrayList<String>();
						for (CallLog call : calls) {
							latLons.add(call.mylat + "," + call.mylong +
									"," + call.contactlat + "," + call.contactlong);
						}
						return latLons;
					}
				});
		JavaRDD<String> distances = pipeInputs.pipe(SparkFiles.get(distScriptName));
		// First we need to convert our RDD of String to a DoubleRDD so we can
		// access the stats function
		JavaDoubleRDD distanceDoubles = distances.mapToDouble(new DoubleFunction<String>() {
			public double call(String value) {
				return Double.parseDouble(value);
			}
		});
		final StatCounter stats = distanceDoubles.stats();
		final Double stddev = stats.stdev();
		final Double mean = stats.mean();
		JavaDoubleRDD reasonableDistances =
				distanceDoubles.filter(new Function<Double, Boolean>() {
					public Boolean call(Double x) {
						return (Math.abs(x - mean) < 3 * stddev);
					}
				});
		System.out.println(StringUtils.join(reasonableDistances.collect(), ","));
		jsc.stop();
		System.exit(0);
	}

	static String[] loadCallSignTable() throws FileNotFoundException {
		Scanner callSignTb1 = new Scanner(new File("./files/callsign_tbl_sorted"));
		ArrayList<String> callSignList = new ArrayList<>();
		while (callSignTb1.hasNext()) {
			callSignList.add(callSignTb1.nextLine());
		}
		return callSignList.toArray(new String[0]);
	}

	static String lookupCountry(String callSign, String[] table) {
		Integer pos = java.util.Arrays.binarySearch(table, callSign);
		if (pos < 0) {
			pos = -pos - 1;
		}
		return table[pos].split(",")[1];
	}

	static ObjectMapper createMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	static Tuple2<String, ContentExchange> createRequestForSign(String sign, HttpClient client) throws Exception {
		ContentExchange exchange = new ContentExchange(true);
		exchange.setURI(new URI("http://new73s.herokuapp.com/qsos/" + sign + ".json"));
		client.send(exchange);
		return new Tuple2<>(sign, exchange);
	}

	static Tuple2<String, CallLog[]> fetchResultFromRequest(ObjectMapper mapper,
															Tuple2<String, ContentExchange> signExchange) {
		String sign = signExchange._1();
		ContentExchange exchange = signExchange._2();
		return new Tuple2(sign, readExchangeCallLog(mapper, exchange));
	}

	static CallLog[] readExchangeCallLog(ObjectMapper mapper, ContentExchange exchange) {
		try {
			exchange.waitForDone();
			String responseJson = exchange.getResponseContent();
			return mapper.readValue(responseJson, CallLog[].class);
		} catch (Exception e) {
			return new CallLog[0];
		}
	}
}
