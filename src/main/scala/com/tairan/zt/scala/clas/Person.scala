package com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/23.
 */
@SerialVersionUID(42L) class Person(private var privateAge: Int = 0) extends Serializable {
  def age = privateAge

  def age_=(newValue: Int) {
    if (newValue > privateAge) privateAge = newValue
  }
}
