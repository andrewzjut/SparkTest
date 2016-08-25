package com.tairan.zt.scala.clas

import _root_.com.tairan.zt.scala.clas.com.tairan.zt.scala.clas._


/**
 * Created by hzzt on 2016/8/23.
 */
object ClazzDemo extends App {
  val myCounter = new Counter
 /* myCounter.increament()
  println(myCounter.current)*/

  println(myCounter.current)
  val person = new Person
  person.age = 10
  println(person.age)

  val person1 = new Person1

  person1.name="zhangtng"
  println(person1.name)

  val person2 = new Personal("zhangtong",18)
  person2.age
  person2.name
  person2.age=10

  val chatter = new NetWork
  val myFace  = new NetWork

  val fred = chatter.join("Fred")
  val wilma = chatter.join("Wilma")
  println(chatter.toString)

 val employee = new Employee("zhang",23,10000)
 val person22 = new Person2("zhang",23)
 person22.age=10
 person22.name="tong"
 employee.name="zz"
 employee.age=33

 val employee2 = new Employee2("Fred"){
  def gretting = "hello fred"
 }
 println(employee2)
}
