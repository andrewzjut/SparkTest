package com.tairan.zt.scala

import scala.collection.SortedSet
import scala.collection.mutable.ListBuffer

/**
 * Created by hzzt on 2016/8/22.
 */
object Test1 {
  def main(args: Array[String]) {
    val list = List(1, 2, 3)
    list.map(_ + 1).foreach(print(_))
    println()
    val set = Set(1, 2, 3, 4, 4)
    set.foreach(print _)
    println()
    val xs = List(1, 2, 3, 4, 5)
    val git = xs.grouped(3)
    println(git.next())
    println(git.next())

    val sit = xs.sliding(3)
    println(sit.next())
    println(sit.next())
    println(sit.next())
    val xs1 = xs.updated(1, 8)
    println(xs1)

    val lb = new ListBuffer().+=(1)
    lb.+=(2)
    println(lb)
    lb.+=(3)
    println(lb)

    val fruit = Set("apple", "orange", "peach", "banana")
    println(fruit("peach"))

    val sst = SortedSet.empty[Int]
    val b = sst.+(1, 6, 3, 6, 1, 0, 9)
    println(b)

    val map = Map("北京" -> "中国", "东京" -> "日本", "首尔" -> "韩国", "华盛顿" -> "美国", "莫斯科" -> "俄罗斯", "伦敦" -> "英国")
    val keys = map.keys
    println(keys)
    val keyset = map.keySet
    println(keyset)
    val keyIterator = map.keysIterator;
    while (keyIterator.hasNext) {
      print(keyIterator.next())
    }
    println()
    val values = map.values
    println(values)
    val valueIterator = map.valuesIterator;
    while (valueIterator.hasNext) {
      print(valueIterator.next())
    }

    val map2: Map[String, String] = map.mapValues(value => "国家:" + value)
    val set2 = map2.keySet
    for (key <- set2) {
      println(map2.get(key))
    }

    val str = 1 #:: 2 #:: 3 #:: Stream.empty
    println(str)
    val v = Vector(1 to 10:_*)
    println(v)

    v map (_ + 1) map (_ * 2) //两次map浪费资源，但是逻辑清晰
    (v.view map (_ + 1) map (_ * 2)).force //将两个map转换伟视图 再强制转换为vector
  }

  def lazyMap[T, U](coll: Iterable[T], f: T => U) = new Iterable[U] {
    def iterator = coll.iterator map f
   }
}
