package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest


/**
 * Created by hzzt on 2016/8/25.
 */
trait ConsoleLogger1 extends Logger{
   override def log(msg: String) {
    println(msg)
  }
}
