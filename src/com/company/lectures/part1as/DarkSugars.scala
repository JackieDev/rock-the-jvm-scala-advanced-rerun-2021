package com.company.lectures.part1as

import scala.util.Try

object DarkSugars extends App {

  // syntax sugar #1: methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // write some complex code
    42
  }

  val aTryInstance = Try {
    throw new RuntimeException
  }



  // syntax sugar #2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 //just pure magic :)

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello Scala")
  })

  val aSweeterThread = new Thread(() => println("Sweet Scala!!!")) // this is really cool!

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet") // really sweet!



  // syntax sugar #3: the :: and #:: methods
  val prependedList = 2 :: List(3, 4)
  // 2.::(List(3,4))
  // List(3,4).::(2)
  // ?!

  // scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4,5)
  List(4,5).::(3).::(2).::(1) // equivalent to the ^

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual code here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // this is crazy cool!



  // syntax sugar #4: multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip") //that's what she said haha
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is awesome!!!!!"



  // syntax sugar #5: infix types
  class Composite[A, B]
  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???



  // syntax sugar #6: update() is like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // rewritten to (2, 7) by anArray.update(2,7) ?
  // remember there is update as well as apply



  // syntax sugar #7: setters for mutable containers - ewww mutable!!
  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember // the "getter"
    def member_=(value: Int): Unit = internalMember = value // the "setter"  // I don't recall seeing this methodName_=(params) syntax before
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // this uses member_=(value.... method
  aMutableContainer.member_=(32) // I can call this directly

}
