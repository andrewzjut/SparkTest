package com.tairan.zt.scala

import java.util

import scala.collection.mutable.ArrayBuffer

/**
 * Created by hzzt on 2016/8/23.
 */
object ArrayDemo {
  def main(args: Array[String]) {
    val nums = new Array[Int](3);
    val nums1 = Array(1, 2, 3)
    val strings = "a b c d e f".split(" ")

    val b = ArrayBuffer[Int]()
    b += 1 //添加一个元素
    b +=(1, 2, 3, 4, 5) //在尾端添加多个元素
    b ++= Array(6, 7, 8) //在尾端添加给数组
    b.trimEnd(5) //移除最后五个
    b.insert(2, 6)
    b.insert(2, 7, 8, 9) //从第二个开始插入三个
    b.remove(2)
    b.remove(2, 3) //从第二个开始移除三个
    b.toArray
    println(b)
    for (i <- 0 until b.length) print(b(i))
    println()
    for (i <- 0 until(b.length, 2)) print(b(i))
    println()
    for (i <- (0 until(b.length, 2) reverse)) print(b(i))
    println()
    for (elem <- b) print(elem)

    val result = for (elem <- b if elem % 2 == 0) yield 2 * elem
    println(result)
    b.filter(_ % 2 == 0).map(2 * _).foreach(print)

    val a = ArrayBuffer(1, 2, 3, -4, 5, -6, 7, -8)
    var first = true
    var n = a.length
    var i = 0
    while (i < n) {
      if (a(i) >= 0) i += 1
      else {
        if (first) {
          first = false
          i += 1
        } else {
          a.remove(i)
          n -= 1
        }
      }
    }
    println(a)

    val c = ArrayBuffer(1, 2, 3, -4, 5, -6, 7, -8)
    var first1 = true

    val indexes = for (i <- 0 until c.length if (c(i) < 0)) yield {
      i
    }
    println(indexes)
    for (elem <- indexes.tail reverse) {
      print(elem)
      c.remove(elem)
    }
    println(c)

    val cc = ArrayBuffer(1, 7, 2, 9)
    val ccSorted = cc.sorted
    print(ccSorted)
    val ccDescending = cc.sortWith(_ > _)
    println(ccDescending)


    import scala.util.Sorting
    println(Sorting.quickSort(cc.toArray))

    val maxtrix = Array.ofDim[Double](3,4)
    val triangle = new Array[Array[Int]](10)
    for (i <- 0 until (triangle.length))
      triangle(i) = new Array[Int](i + 1)

    import scala.collection.JavaConversions.bufferAsJavaList
    import scala.collection.mutable.ArrayBuffer

    val command = ArrayBuffer("ls", "-al", "/home/cay")
    val pb = new ProcessBuilder(command)


    import scala.math.random
    def randomArray(n: Int) = {
      for (i <- 0 until n) yield random * n toInt
    }
    println(randomArray(10).mkString(","))

    def change(arr: Array[Int]): Array[Int] = {
      val t = arr.toBuffer
      for (i <- 1 until(t.length, 2); tmp = t(i); j <- i - 1 until (i)) {
        t(i) = t(j)
        t(j) = tmp
      }
      t.toArray
    }
    println(change(Array(1,2,3,4,5)).mkString(","))

    val aa = Array(1,3,-3,-5,-7,3,2)
    def reorderArray(arr: Array[Int]) = {
      val  b = arr.filter(_ > 0)
      val  c = arr.filter(_ <= 0)
      val newarr = b ++ c
      print(newarr.toBuffer.toString())
    }
    reorderArray(aa)
  }
}
