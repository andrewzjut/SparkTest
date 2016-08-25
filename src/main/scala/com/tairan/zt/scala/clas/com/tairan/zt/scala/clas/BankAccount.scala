package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/24.
 */
class BankAccount(initialBalance: Double) {
  private var balance = initialBalance

  def deposit(amount: Double):Double = {
    balance += amount
    balance
  }

  def withdraw(amount: Double):Double = {
    balance -= amount
    balance
  }
}
