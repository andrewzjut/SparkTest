package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.chapter11

/**
 * Created by hzzt on 2016/8/25.
 */
class Money(val dollar:BigInt,val cent:BigInt){

  def +(other:Money):Money={
    val (a,b) = (this.cent + other.cent) /% 100
    new Money(this.dollar + other.dollar + a,b)
  }

  def -(other:Money):Money={
    val (d,c) = (this.toCent() - other.toCent()) /% 100
    new Money(d,c)
  }

  private def toCent()={
    this.dollar * 100 + this.cent
  }

  def ==(other:Money):Boolean = this.dollar == other.dollar && this.cent == other.cent

  def <(other:Money):Boolean = this.dollar < other.dollar || (this.dollar == other.dollar && this.cent < other.cent)

  override def toString = "dollar = " + dollar + " cent = " + cent
}

object Money{
  def apply(dollar:Int,cent:Int):Money={
    new Money(dollar,cent)
  }

  def main(args:Array[String]){

    val m1 = Money(1,200)
    val m2 = Money(2,2)
    println(m1 + m2)
    println(m1 - m2)
    println(m1 == m2)
    println(m1 < m2)
    println(Money(1,75)+Money(0,50))
    println(Money(1,75)+Money(0,50)==Money(2,25))

  }
}