package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas.traittest


/**
 * Created by hzzt on 2016/8/25.
 */
trait ShortLogger extends Logger {
  val maxLength :Int

   override def log(msg: String) {
    super.log(
      if (msg.length <= maxLength) msg
      else msg.substring(0, maxLength - 3) + "..."
    )
  }
}
