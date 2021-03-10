package com.company.lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    // my new methods
    //def times(function: () => Int): Int = function(value)
    def *(list: List[Int]): List[Int] = {
      def go(newList: List[Int], index: Int): List[Int] = {
        if (index >= value) newList
        else go(newList ::: list, index + 1)
      }
      go(List(), 0)
    }

    // Dans methods:
    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
      else {
          function()
          timesAux(n - 1)
        }
      timesAux(value)
    }

    def **[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
  new RichInt(42).sqrt

  42.isEven  // behind the scenes: new RichInt(42).isEven
  // type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiler doesn't do multiple implicit searches.
  //42.isOdd - can't do this

  /*
    Exercises: Enrich the String class
     - asInt
     - encrypt
       "John" -> Lqjp

     Keep enriching the Int class
      - times(function)
        3.times(() => ...)
      - *
        3 * List(1,2) => List(1,2,1,2,1,2)
  */

  // My solutions
  implicit class RichString(value: String) {
   // def asInt: Int = value.toInt
    //def encrypt: String = ??? // didn't finish this one

    // Daniel's methods
    def asIntDan: Int = Integer.valueOf(value)
    def encryptDan(cypherDistance: Int): String = value.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asIntDan + 4)
  println("John".encryptDan(2))

  3.times(() => println("Scala Rocks!"))
  println(4 * List(1,2)) // My method
  println(4 ** List(1,2)) // Dans method

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2)

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)


}
