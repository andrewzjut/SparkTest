package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/23.
 */
object Direction extends Enumeration {
  val North, East, South, West = Value
}

object Direction2 extends Enumeration {
  val North = Value(1, "North")
  val East = Value(2, "East")
  val South = Value(3, "South")
  val West = Value(0, "West")
}

object TrafficLightColor extends Enumeration {
  type TrafficLightColor = Value
  val Red = Value(0, "Stop")
  val Yellow = Value(10)
  val Green = Value("Go")
}

object Test3 {
  def main(args: Array[String]) {
    for (dir <- 0 to Direction2.maxId - 1) {
      print(Direction2(dir) + "\t");
      println(Direction2(dir).id) //枚举值从0开始计数，可以用枚举值id方法获得它的计数值：
    }
    println(Direction2.North.id)
    println(TrafficLightColor.Red)


  }
}