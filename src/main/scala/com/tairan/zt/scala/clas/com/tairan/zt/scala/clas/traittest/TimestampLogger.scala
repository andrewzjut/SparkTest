package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest

import java.util.Date


/**
 * Created by hzzt on 2016/8/25.
 */
trait TimestampLogger extends Logger {
   override def log(msg: String){
    super.log(new Date() + " " + msg)
  }
}
