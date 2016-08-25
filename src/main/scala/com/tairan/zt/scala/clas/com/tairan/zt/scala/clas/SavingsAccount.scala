package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/24.
 */
class SavingsAccount(initialBalance: Double) extends BankAccount(initialBalance) {
  private var num: Int = _

  def earnMonthlyInterst() = {
    num = 3
    super.deposit(1)
  }

  override def deposit(amount: Double): Double = {
    num -= 1
    if (num < 0) super.deposit(amount - 1) else super.deposit(amount)
  }

  override def withdraw(amount: Double): Double = {
    num -= 1
    if (num < 0) super.withdraw(amount - 1) else super.withdraw(amount)
  }
}
