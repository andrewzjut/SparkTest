package com.tairan.zt.scala

/**
 * Created by hzzt on 2016/8/22.
 */

import java.util

import scala.collection.mutable
import scala.collection.mutable.Map

object MapMaker {
  def makMap: Map[String, String] = {
    new mutable.HashMap[String, String] with
      mutable.SynchronizedMap[String, String] {
      override def default(key: String): String =
        "Why do you want to know?"
    }
  }

  def main(args: Array[String]) {
    val capital = MapMaker.makMap
    capital.++=(Map("US" -> "Washington",
      "Paris" -> "France", "Japan" -> "Tokyo"))
    println(capital("Japan"))
    //capital("New Zealand")
    capital += ("New Zealand" -> "Wellington")
    println(capital("New Zealand"))

    val a = capital.getOrElse("hangzhou", 0)
    println(a.getClass)

    val scores = Map(("alice", 10), ("bob", 3), ("cindy", 8))
    println(scores("alice"))
    val change = for ((k, v) <- scores) yield (v, k)
    for (v <- change.values) println(v)

    import scala.collection.JavaConversions.mapAsScalaMap
    val score: scala.collection.mutable.Map[String, Int] = new util.TreeMap[String, Int]()



  }
}
