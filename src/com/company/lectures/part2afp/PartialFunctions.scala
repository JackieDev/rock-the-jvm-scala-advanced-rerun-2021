package com.company.lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }  // partial function value

  println(aPartialFunction(2)) // as expected
 // println(aPartialFunction(3)) // crashes with a MatchError

  //PF utilities
  println(aPartialFunction.isDefinedAt(4))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))
  // this is cool

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2)) // still going to get 56
  println(pfChain(45)) // will see 67

  // PFs extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  // Higher order functions pass functions as args or return a function
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can only have ONE argument type
   */

  /**
   *
   * Exercises
   *
   * 1 - construct a PF instance (anonymous class)
   * 2 - dumb chatbot as a PF
   */

  // Exercise 1 - this is my attempt
  def partialFunction[A, B](f: Function1[A, B]) = (a: A) => f(a)
  // ^not quite what Daniel meant

  // Daniel's solution
  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  println(s"aManualFussyFunction is defined for 23: ${aManualFussyFunction.isDefinedAt(23)}")


  // Exercise 2 - my attempt
  //scala.io.Source.stdin.getLines().foreach(dumbChatbot(_))

  def dumbChatbot(line: String) = line match {
    case "hello" => println("hello there")
    case "nice to meet you" => println("nice to meet you too")
    case "I'm Jackie" => println("I'm dumb chatbot")
    case _ => println("Take care")
  }
  //I didnt implement a PartialFunction like Daniel did

  // Daniel's solution
  val chatbot: PartialFunction[String, String] = {
    case "hello" => "hello there"
    case "nice to meet you" => "nice to meet you too"
    case "I'm Jackie" => "I'm dumb chatbot"
    case _ => "Take care"
  }
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)


}
