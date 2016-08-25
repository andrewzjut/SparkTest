package com.tairan.zt.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCount {

	public static class TokenizerMapper
			extends Mapper<Object, Text, Text, IntWritable> {
		private static final IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				this.word.set(itr.nextToken());
				context.write(this.word, one);
			}
		}
	}

	public static class IntSumReducer
			extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			this.result.set(sum);
			context.write(key, this.result);
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		conf.set("mapreduce.jobtracker.address", "172.30.251.229:9001");
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resourcemanager.address", "172.30.251.229:8032");
		conf.set("fs.defaultFS", "hdfs://172.30.251.229:9000");
		conf.set("mapreduce.app-submission.cross-platform", "true");
		/*conf.set("mapreduce.job.jar", "D:\\workspace\\scala\\out\\artifacts\\scala_jar\\scala.jar");

		conf.set("mapreduce.jobhistory.address","172.30.251.229:10020");
		conf.set("dfs.permissions","false");
		conf.set("yarn.resourcemanager.hostname", "172.30.251.229");
		conf.set("yarn.resourcemanager.admin.address", "172.30.251.229:8033");
		conf.set("yarn.resourcemanager.resource-tracker.address", "172.30.251.229:8031");
*/
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		/*if (otherArgs.length < 2) {
			System.err.println("Usage: wordcount <in> [<in>...] <out>");
			System.exit(2);
		}*/
		Job job = Job.getInstance(conf, "word count");
		job.setJar("D:\\workspace\\SparkTest\\out\\artifacts\\sparktest_jar\\main.jar");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path("hdfs://172.30.251.229:9000/user/hzzt/test/hello.txt"));

		FileOutputFormat.setOutputPath(job, new Path("hdfs://172.30.251.229:9000/user/hzzt/test/out"));
		System.exit(job.waitForCompletion(true) ? 0 : 1);


	}
}