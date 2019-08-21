package com.wankun.scalademo1

case class Complex(real: Double, imaginary: Double) {

  override def toString() = "" + re + (if (im < 0) "" else "+") + im + "i"

  def re = real

  def im = imaginary
}

object ComplexNumbers {
  def main(args: Array[String]) {
    val c = Complex(2.3, 4.5)
    println(c.im)

    println(c)
    println(Complex(6.7, -1.2))
  }
}