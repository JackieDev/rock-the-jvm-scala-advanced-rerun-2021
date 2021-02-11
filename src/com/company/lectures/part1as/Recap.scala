package com.company.lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // instructions vs expressions

  val aCodeBlock = {
    if (aCondition) 54
    56
  }



  // recursion: stack and tail
  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)


  // generics
  abstract class MyList[+A] // variance and variance problems later on


  // exceptions and try/catch/finally
  val throwsException = throw new RuntimeException
  val aPotentialFailure = try {
    throwsException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some logs")
  }


  val incrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1,2,3).map(anonymousIncrementer) //HOF


  // for-comprehension create pairs
  for {
    num <- List(1,2,3)
    char <- List('a','b','c')
  } yield num + "-" + char




}
