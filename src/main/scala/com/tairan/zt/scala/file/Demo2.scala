package com.tairan.zt.scala.file

import java.io.{File, PrintWriter}

import scala.io.Source

/**
 * Created by hzzt on 2016/8/24.
 */
object Demo2 extends App {
  /* val source = Source.fromFile("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt").getLines().toArray.reverse
   val pw = new PrintWriter("D:\\workspace\\SparkTest\\src\\main\\resources\\file1.txt")
   source.foreach(line => pw.write(line + "\n"))
   source.foreach(println _)
   pw.flush()
   pw.close()*/

  /* val reader = Source.fromFile("D:\\workspace\\SparkTest\\src\\main\\resources\\file2.txt").getLines()
   val result = for (r <- reader) yield r.replaceAll("\\t", "      ")
   val pw2 = new PrintWriter("D:\\workspace\\SparkTest\\src\\main\\resources\\file1.txt")
   result.foreach(line => pw2.write(line + "\n"))
   pw2.flush()
   pw2.close()*/

  //Source.fromFile("D:\\\\workspace\\\\SparkTest\\\\src\\\\main\\\\resources\\\\file.txt").mkString.split("\\s+").foreach(arg => if(arg.length > 12) println(arg))

  /*val nums = Source.fromFile("D:\\\\workspace\\\\SparkTest\\\\src\\\\main\\\\resources\\\\file.txt").mkString.split("\\s+")

  var total = 0d
  nums.foreach(total += _.toDouble)
  println(total)

  val pw3 = new PrintWriter("D:\\workspace\\SparkTest\\src\\main\\resources\\file2.txt")
  for (n <-0 to 20){
    val t = BigDecimal(2).pow(n)
    pw3.write(t.toString())
    pw3.write("\t\t")
    pw3.write((1/t).toString())
    pw3.print("\n")
  }
  pw3.close()*/

  /*val source = Source.fromString("like this,maybe with \" or\"").mkString
  val pattern = "\\w+\\s?\"".r
  val matchs = pattern.findAllIn(source)
  for (m <-matchs){
    println(m)
  }*/

/*  val source = Source.fromFile("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt").mkString

  val pattern = """[((\d+\.){0,1}\d+)^\s+]+""".r

  pattern.findAllIn(source).map(_.trim).foreach(println(_))*/

  /*val source = Source.fromURL("http://www.163.com/").mkString

  val pattern = """<img[^>]+(src\s*=\s*"[^>^"]+")[^>]*>""".r

  for (pattern(str) <- pattern.findAllIn(source)) println(str)

  val out = new PrintWriter("D:\\workspace\\SparkTest\\src\\main\\resources\\file3.txt")
  for (pattern(str)<- pattern.findAllIn(source)) out.write(str)
  out.flush()
  out.close()

  def subdirs(dir: File): Iterator[File] = {

    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs _)
  }

  for (d <- subdirs(new File("D:\\workspace\\SparkTest\\src\\main\\scala\\com\\tairan\\zt"))) {
    println(d)
  }*/

  val dir = new File("D:\\workspace\\SparkTest\\src\\main\\scala\\com\\tairan\\zt")
  def subdirs(dir:File):Iterator[File]={
    val children = dir.listFiles().filter(_.getName.endsWith("scala"))
    children.toIterator ++ dir.listFiles().filter(_.isDirectory).toIterator.flatMap(subdirs _)
  }
  val n = subdirs(dir).length

  println(n)
  for (d <- subdirs(dir)) {
    println(d)
  }
}
