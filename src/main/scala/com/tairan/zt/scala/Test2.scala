package com.tairan.zt.scala

/**
 * Created by hzzt on 2016/8/22.
 */
object Test2 extends App {
  var x = 0
  if (x > 0) 1 else -1

  val s = if (x > 0) 1 else -1

  val s1 = if (x > 0) "positive" else -1

  if (x > 0) 1 else ()
  //没有输出值时  使用（）代表Unit

  val x1, y1 = 1

  var r, n = 1
  while (x > 0) {
    r = r * n;
    n -= 1
  }
  for (x <- 1 to 10) {
    r = r * x
  }

  val s2 = "hello"
  var sum = 0
  for (i <- 0 until s2.length)
    sum += s2(i)
  println(sum)

  var sum1 = 0
  for (ch <- "hello") sum1 += ch
  println(sum1)

  import scala.util.control.Breaks._

  breakable {
    for (ch <- "hello") {
      if (ch.equals('l')) {
        break()
      }
      println(ch)
    }
  }

  for (i <- 1 to 3; j <- 1 to 3) print((10 * i + j) + " ")
  println()
  for (i <- 1 to 3; j <- 1 to 3 if i != j) print((10 * i + j) + " ")
  println()
  for (i <- 1 to 3; from = 4 - i; j <- from to 3) print((10 * i + j) + " ")
  println()
  val v = for (i <- 1 to 10) yield i % 3
  println(v)


  def abs(x: Double) = if (x > 0) x else -x

  //递归函数必须指定返回值
  def fac(n: Int): Int = if (n <= 0) 1 else n * fac(n - 1)

  //带默认参数
  def decorate(str: String, left: String = "[", right: String = "]") = println(left + str + right)

  decorate("hello") //[hello]
  decorate("<<<", "hello", ">>>")
  decorate(left = "<<<", str = "hello", right = ">>>")
  decorate("hello", right = "]<<<")

  def sum(ins: Int*): Int = {
    var result = 0;
    for (in <- ins) result += in
    result
  }

  println(sum(1, 2, 3, 4, 5))
  println(sum(1 to 5: _*))

  //将 1 to 5 当作参数序列处理

  def recursiveSum(ins: Int*): Int = {
    if (ins.length == 0) 0
    else ins.head + recursiveSum(ins.tail: _*)
  }

  println(recursiveSum(1, 2, 3, 4, 5))

  def signNum(num: Int): Int = {
    if (num > 0) 1 else if (num == 0) 0 else -1
  }

  for (i <- 0 to 10 reverse) println(i)

  def countdown(n: Int) {
    (0 to n reverse) foreach print
  }

  countdown(10)

  println()

  def mulUnicode(str: String) {
    var mul: Long = 1
    for (ch <- str) {
      mul = mul * ch.toLong
    }
    println(mul)
  }

  mulUnicode("Hello")
  var t: Long = 1
  "Hello".foreach(t *= _.toLong)

  def recursiveMul(str: String): Long = {
    if (str.length == 1)
      str.charAt(0).toLong
    else
      str.take(1).charAt(0).toLong * recursiveMul(str.drop(1))
  }

  println(recursiveMul("Hello"))

  def mi(x: Int, n: Int): Double = {
    if (n == 0) 1
    else if (n > 0 && n % 2 == 0) mi(x, n / 2) * mi(x, n / 2)
    else if (n > 0 && n % 2 == 1) x * mi(x, n - 1)
    else 1/mi(x,-n)
  }

}
