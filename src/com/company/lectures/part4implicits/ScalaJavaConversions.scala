package com.company.lectures.part4implicits

import java.{util => ju}

object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
    Bunch of convertor methods available:
    Iterator
    Iterable
    ju.List - scala.mutable.Buffer
    ju.Set - scala.mutable.Set
    ju.Map - scala.mutable.Map

   */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer)

  val numbers = List(1,2,3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala
  println(backToScala eq numbers) // false (different instances)
  println(backToScala == numbers) // true (same values)

 // juNumbers.add(7) // throws an exception, as juNumbers is immutable

  /*
    Exercise
    - create a Scala-Java Optional-Option
   */

  // my attempt - I have no idea
//  class ScalaJavaConvertor[T](optional: Option) {
//    def apply(value: T) = value.asScala
//  }

  // Daniel says ...
  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def asScalaOptional[T](option: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (option.isPresent) Some(option.get) else None
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala
  println(scalaOption)
}
