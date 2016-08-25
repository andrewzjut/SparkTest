package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.serial

import java.io._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by hzzt on 2016/8/25.
 */
class Person(var name: String) extends Serializable {
  val friends = new ArrayBuffer[Person]()

  def addFriend(friend: Person): Unit = {
    friends += friend
  }
  override def toString() = {
    var str = "My name is " + name + " and my friends name is "
    friends.foreach(str += _.name + ",")
    str
  }
}
object Test extends App{
  val p1 = new Person("zhang")
  val p2 = new Person("tong")
  val p3 = new Person("helloworld")

  p1.addFriend(p2)
  p1.addFriend(p3)

  println(p1)

  val out = new ObjectOutputStream(new FileOutputStream(new File("D:\\workspace\\SparkTest\\src\\main\\resources\\file4.txt")))
  out.writeObject(p1)
  out.close()

  val in = new ObjectInputStream(new FileInputStream(new File("D:\\workspace\\SparkTest\\src\\main\\resources\\file4.txt")))
  val p = in.readObject().asInstanceOf[Person]
  println(p)
}
