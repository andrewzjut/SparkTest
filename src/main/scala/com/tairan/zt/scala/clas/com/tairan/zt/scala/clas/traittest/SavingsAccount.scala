package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest

import com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.Account

/**
 * Created by hzzt on 2016/8/25.
 */
class SavingsAccount extends Account with ConsoleLogger1 {

  val maxLength = 20

  override def log(msg: String): Unit = super.log(msg)

  def withDraw(amount: Double): Unit = {
    if (amount > balance) log("Insuficient funds")
    else balance -= amount
  }
}
