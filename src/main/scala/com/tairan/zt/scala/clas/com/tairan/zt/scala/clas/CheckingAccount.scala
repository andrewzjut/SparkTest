package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/24.
 */
class CheckingAccount(initialBalance:Double)extends BankAccount(initialBalance){
  override def deposit(amount: Double): Double = super.deposit(amount-1)

  override def withdraw(amount: Double): Double = super.withdraw(amount-1)
}
