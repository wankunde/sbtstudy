package com.wankun.scalademo1

import com.wankun.util.Logging

case class Complex(real: Double, imaginary: Double) {

  override def toString(): String = "" + re + (if (im < 0) "" else "+") + im + "i"

  def re: Double = real

  def im: Double = imaginary
}

object ComplexNumbers extends Logging{
  def main(args: Array[String]) {
    val c = Complex(2.3, 4.5)
    logInfo(c.im)

    logInfo(c)
    logInfo(Complex(6.7, -1.2))
  }
}
