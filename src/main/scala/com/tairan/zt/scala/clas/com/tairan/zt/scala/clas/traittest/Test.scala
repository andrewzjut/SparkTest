package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest


/**
 * Created by hzzt on 2016/8/25.
 */
object Test extends App{

  val acct1 = new SavingsAccount with TimestampLogger with ShortLogger
  acct1.deposit(100)

  acct1.withDraw(1000)
}
