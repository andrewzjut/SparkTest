package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.chapter11

import breeze.numerics.abs

/**
 * Created by hzzt on 2016/8/25.
 */
class Fraction(n: Int, d: Int) {
  private val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d)
  private val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d)

  override def toString = num + "/" + den

  def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0

  def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)

  def +(other: Fraction): Fraction = {
    newFrac((this.num * other.den) + (other.num * this.den), this.den * other.den)
  }

  def -(other: Fraction): Fraction = {
    newFrac((this.num * other.den) - (other.num * this.den), this.den * other.den)
  }

  def *(other: Fraction): Fraction = {
    newFrac(this.num * other.num, this.den * other.den)
  }

  def /(other: Fraction): Fraction = {
    newFrac(this.num * other.den, this.den * other.num)
  }

  private def newFrac(a: Int, b: Int): Fraction = {
    val x: Int = if (b == 0) 1 else a * sign(b) / gcd(a, b);
    val y: Int = if (b == 0) 0 else b * sign(b) / gcd(a, b);
    new Fraction(x, y)
  }
}
object Test extends App{
  val f = new Fraction(15,-6)
  val p = new Fraction(20,60)
  println(f)
  println(p)
  println(f + p)
  println(f - p)
  println(f * p)
  println(f / p)
}
