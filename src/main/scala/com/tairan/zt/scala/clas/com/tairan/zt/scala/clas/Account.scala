package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/23.
 */
object Account {
  private var lastNumber = 0

  private def newUniqueNumber() = {
    lastNumber += 1
    lastNumber
  }

  def main(args: Array[String]) {
    val account = new Account
    account.balance=100
    account.deposit(20)
    println(account.balance)
    println(account.id)
  }
}

class Account {
  val id = Account.newUniqueNumber()
  var balance = 0.0

  def deposit(amount: Double) {
    balance += amount
  }
}
