package com.tairan.zt.scala.clas.com.tairan.zt.scala.clas

/**
 * Created by hzzt on 2016/8/23.
 */

import scala.collection.mutable.ArrayBuffer

class NetWork {

  class Member(val name: String) {
    val contacts = new ArrayBuffer[Member]

    override def toString = s"Member($contacts, $name)"
  }

  private val members = new ArrayBuffer[Member]

  def join(name: String) = {
    val m = new Member(name)
    members += m
  }

  override def toString = s"NetWork($members)"
}
