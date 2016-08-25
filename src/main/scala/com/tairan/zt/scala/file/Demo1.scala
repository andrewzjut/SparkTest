package com.tairan.zt.scala.file

import java.io._
import java.net.URL

import com.tairan.zt.scala.clas.Person

import scala.io.Source

/**
 * Created by hzzt on 2016/8/24.
 */
object Demo1 {
  def main(args: Array[String]) {
    val source = Source.fromFile("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt", "utf-8")
    /*val lineIterator = source.getLines()
    for (l <- lineIterator)
      println(l.length)

     val lines = source.getLines().toArray
     for (line <- lines)
       println(line)

     for (i <- 0 until(lines.length-1))
       println(lines(i))

    val contents = source.mkString
    println(contents)

    for (c <- source)
       println(c)

    val iter = source.buffered
     var count = 0
     while (iter.hasNext) {
       if (iter.head.equals('a')){
         count += 1
         println("No:" + count + "a")
         iter.next()
       }else{
         count += 1
         iter.next()
       }

     }*/

    /*val token = source.mkString.split("\\S+")
    val numbers = for (w <- token) yield w.toDouble
    for (num <- numbers) {
      println(num)
    }*/
    /*source.close()

    val source1 = Source.fromURL("http://www.baidu.com","utf-8")
    val lines = source1.getLines()
    for (line<-lines){
      println(line)
    }*/

    /*val file = new File("D:\\workspace\\SparkTest\\src\\main\\java\\com\\tairan\\zt\\hadoop\\JavaHdfsLR.java")
    val in = Some(new FileInputStream(file))
    //val out = Some(new FileOutputStream(new File("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt"), true))
    val out = new PrintWriter(new FileOutputStream(new File("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt"), true))
    val bytes = new Array[Byte](1024)
    var len = 0
    while ({len = in.get.read(bytes);len != -1}) {
      println(new String(bytes, 0, len))
     // out.println(new String(bytes, 0, len))
    }
    out.flush()
    in.get.close()
    out.close()*/

    for (d <- subdirs(new File("D:\\workspace\\SparkTest\\src\\main\\scala\\com\\tairan\\zt"))) {
      println(d)
    }

    /*val person = new Person(23)
    val person1 = new Person(24)
    val out = new ObjectOutputStream(new FileOutputStream("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt", true))

    out.writeObject(person)
    out.writeObject(person1)
    out.close()*/
    val in = new ObjectInputStream(new FileInputStream("D:\\workspace\\SparkTest\\src\\main\\resources\\file.txt"))

    val savedPerson1 = in.readObject().asInstanceOf[Person]
    println(savedPerson1.age)
    val savedPerson2 = in.readObject().asInstanceOf[Person]
    println(savedPerson2.age)
    println("----------------")
    val numPattern = "[0-9]+".r
    val wsnumwsPattern = """\s+[0-9]+\s+""".r
    for (matchString <- numPattern.findAllIn("99 bottles,98 bottles"))
      println(matchString)
    for (matchString <- wsnumwsPattern.findAllIn("99 bottles,98 bottles"))
      println(matchString)

    val ml = wsnumwsPattern.findAllIn(" 99 bottles, 98 bottles").toArray
    println(ml.mkString(","))

    val numitemPattern = "([0-9]+) ([a-z]+)".r
    //val numitemPattern(num,item) = " 99 bottles, 98 bottles"
    for (numitemPattern(num, item) <- numitemPattern.findAllIn(" 99 bottles, 98 bottles"))
      println(num + "" + item)
  }

  def subdirs(dir: File): Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs _)
  }


}
