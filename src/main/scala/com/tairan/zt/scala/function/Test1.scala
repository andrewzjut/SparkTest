package com.tairan.zt.scala.function

import scala.collection.mutable

/**
 * Created by hzzt on 2016/8/25.
 */
object Test1 extends App {
  def values(fun: (Int) => Int, low: Int, high: Int) = {
    var arr = List[(Int,Int)]()
    low to high foreach {
      num =>
        arr = (num, fun(num)) :: arr
    }
    arr
  }

  println(values(x => x * x, -5, 5).mkString)

  val arr = Array(3, 2, 6, 8, 4, 6, 9, 3, 6, 7, 1, 2)
  println(arr.reduceRight((a, b) => if (a > b) a else b))
  println(1 to 10 reduceLeft (_ * _))
  println((1 to -10).foldLeft(1)(_ * _))

  def largest(fun: (Int) => Int, inputs: Seq[Int]) = {
    inputs.reduceLeft((a, b) => if (fun(a) > fun(b)) a else b)
  }

  println(largest(x => 10 * x - x * x, 1 to 10))


  var list = List[Int]()

  def adjustToPair(fun: (Int, Int) => Int)(tup: (Int, Int)) = {
    list = fun(tup._1, tup._2) :: list
    this
  }

  def map(fun: (Int, Int) => Int): Int = {
    list.reduceLeft(fun)
  }

  val pairs = (1 to 10) zip (11 to 20)
  for ((a, b) <- pairs) {
    adjustToPair(_ * _)((a, b))
  }
  println(map(_ + _))
  println(list)


  val a = Array("asd", "df", "aadc")
  val b = Array(3, 2, 4)
  val c = Array(3, 2, 1)

  println(a.corresponds(b)(_.length == _))
  println(a.corresponds(c)(_.length == _))

  val v = Vector(1, 2, 3, 4, 5, 6)
  println(v(3))
  println((1 to 10)(4))

  val scores = new scala.collection.mutable.HashMap[String, Int] with scala.collection.mutable.SynchronizedMap[String, Int]


  def indexes(s: String): mutable.Map[Char, mutable.SortedSet[Int]] = {
    val map = mutable.Map[Char,mutable.SortedSet[Int]]()
    for (i <- 0 until s.length) {
      map(s(i)) = (map.getOrElse(s(i), mutable.SortedSet(i)) += i)
      map.getOrElse(s(i), mutable.SortedSet(i)) += i
    }
    map
  }

  val map = indexes("Mississippi")
  println(map.mkString(","))

  def removeZero(lst: mutable.MutableList[Int]):mutable.MutableList[Int]={
    val mylist = lst.filter( _ != 0)
    mylist
  }
  val aa = mutable.MutableList(1,2,3,0,5,0,7)
  println(aa)
  println(removeZero(aa))

  def getScores(arr: Array[String], map: Map[String, Int]): Array[Int] = {
    val scores = arr.flatMap(map.get(_))
    scores
  }
  val arr1 = Array("Tom", "Fred", "Harry")
  val map1 = Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5)
  val scores1 = getScores(arr1, map1)
  println(scores1.mkString(","))

}
