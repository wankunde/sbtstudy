package com.wankun.scalademo1

/**
 * similar to Interface in java
 */
trait Ord {
  def <(that: Any): Boolean

  def <=(that: Any): Boolean = (this < that) || (this == that)

  def >(that: Any): Boolean = !(this <= that)

  def >=(that: Any): Boolean = !(this < that)
}

class Date(y: Int, m: Int, d: Int) extends Ord {
  def year: Int = y

  def month: Int = m

  def day: Int = d

  override def toString(): String = year + "-" + month + "-" + day

  override def equals(that: Any): Boolean =
    that.isInstanceOf[Date] && {
      val o = that.asInstanceOf[Date]
      o.day == day && o.month == month && o.year == year
    }

  override def hashCode(): Int = (year + month + day).hashCode()

  override def <(that: Any): Boolean = {
    if (!that.isInstanceOf[Date]) {
      sys.error("cannot compare " + that + " and a Date")
    }

    val o = that.asInstanceOf[Date]
    (year < o.year) ||
      (year == o.year && ((month < o.month) ||
        (month == o.month && day == o.day)))
  }

}

object Date {
  def main(args: Array[String]) {
    val d1 = new Date(2015, 9, 10)
    val d2 = new Date(2015, 9, 10)
    val d3 = new Date(2015, 9, 11)
    print(d1 < d2)
    print(d1 == d2)
    print(d1 >= d3)
  }
}
