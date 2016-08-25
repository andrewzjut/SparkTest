package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest


/**
 * Created by hzzt on 2016/8/25.
 */
class ConsoleLogger extends Logged with Cloneable with Serializable{
  override def log(msg: String): Unit = {println("ConsoleLogger"+msg)}
}
