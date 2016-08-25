package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/25.
 */
object Name {
  case class Currency(value: Double,unit:String)
  def unapply(input: String) = {
    val pos = input.indexOf(" ")
    if (pos == -1) None
    else Some((input.substring(0, pos), input.substring(pos + 1)))
  }

  def main(args: Array[String]) {
    val author = "zhang tong"
    val Name(first, last) = author
    println(first + " " + last)
    Currency(29.8,"EUR")

  }
}
