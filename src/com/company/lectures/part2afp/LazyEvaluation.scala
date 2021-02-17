package com.company.lectures.part2afp

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  // This wont crash the program, until x is called
  lazy val x: Int = throw new RuntimeException

  //lazy vals are evaluated only once which is when they are used or called for the first time
  lazy val y: Int = {
    println("hello")
    42
  }
  println(y)
  println(y)
  // hello will only print once even though y is called twice


  // examples of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")
  //Boo never prints as simpleCondition is false, the compiler doesn't check if lazyCondition is true or not as it doesn't need to


  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue)) // waiting prints 3 times

  // use lazy vals
  def byNameMethodLazy(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n
    t + t + t + 1
  }
  println(byNameMethodLazy(retrieveMagicValue)) // this time waiting prints just once


  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30)
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println("-----------------------------")
  println(gt20lazy) // this doesnt actually process, so the actual values are not printed
  gt20lazy.foreach(println) // this runs and prints the values

  // for comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a + 1
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)

  /*
    Exercise: implement a lazily evaluated, singly linked STREAM of elements.
   */
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B]  //prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    
  }
}
