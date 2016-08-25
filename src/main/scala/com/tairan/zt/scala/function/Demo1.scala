package com.tairan.zt.scala.function

/**
 * Created by hzzt on 2016/8/25.
 */
object Demo1 extends App {

  import scala.math._

  val num = 3.14
  val fun = ceil _
  println(fun(num))
  Array(3.14, 1.42, 3.5).map(fun)

  (x: Double) => 3 * x
  val triple = (x: Double) => 3 * x

  def triple(x: Double) = 3 * x

  def valueAtOneQuarter(f: (Double) => Double) = f(0.25)

  println(valueAtOneQuarter(ceil _))
  println(valueAtOneQuarter(sqrt _))

  def mulBy(factor: Double) = (x: Double) => factor * x

  val quinttuple = mulBy(5)
  println(quinttuple(20))

  def mul(x: Int, y: Int) = x * y

  def mulOneAtATime(x: Int) = (y: Int) => x * y

  println(mulOneAtATime(5)(20))

  def mulOneAtATime2(x: Int)(y: Int) = x * y

}
