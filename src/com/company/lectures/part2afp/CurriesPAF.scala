package com.company.lectures.part2afp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int => 3 + y
  println(add3(5)) // gives us 8
  println(superAdder(3)(5)) // curried function

  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4) // needs the Int => Int type annotation to run

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!

  // my solutions
  simpleAddFunction (7, _)
  simpleAddMethod(7, _)
  curriedAddMethod(7)_
  curriedAddMethod(7) _
  curriedAddMethod(7)(_)

  // Daniel's solutions
  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_3 = curriedAddMethod(7) _ // got this above
  val add7_4 = curriedAddMethod(7)(_) // got this above
  val add7_5 = simpleAddMethod(7, _: Int) // got this above expect the type annotation

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")
  println(insertName("Jackie"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)
  println(fillInTheBlanks("Jackie", " Scala rocks!!!"))


  // EXERCISES
  /*
    1. Process a list of numbers and return their string representations with different formats
       Use the %4.2f, %8.6f and %14.12f and create a curried formatter function given a format and a number.
       Apply this def as a HOF
  */
  //my solution
  def formatter(formatStr: String)(number: BigDecimal): String =
    formatStr match {
      case str => if(str == "%4.2f" || str == "%8.6f" || str == "%14.12f") str.format(number) else "no applicable format was given"
      case _ => "no applicable format was given"
    }

  println(formatter("%4.2f")(100.100200300800))
  println(formatter("%8.6f")(100.100200300800))
  println(formatter("%14.12f")(100.100200300400555800))


  //Daniel's solution
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _
  println(numbers.map(preciseFormat))

  /*
    2. Difference between
      - functions vs methods
      - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /*
    calling byName and byFunction
     - int
     - method
     - parenMethod
     - lambda
     - Partially Applied Function
   */

  // my solutions
  val byName_int = byName(2)
  val byName_method = byName(method)
  val byName_parenMethod = byName(parenMethod())
  val x = 3
  val byName_lambda = byName(x + 4) // ??? There isn't passing a lambda
  val paf = (y: Int) => y + 9
  val byName_PAF = byName(paf.apply(1))

  val byFunction_int = byFunction(() => 2)
  val byFunction_method = byFunction(() => method)
  val byFunction_parenMethod = byFunction(() => parenMethod())
  //val byFunction_lambda = byFunction(() => _) ???
  // I couldn't figure out how to pass a lambda

  // Daniel's
  val byName_lambda_dan = byName((() => 42)())
  val byFunction_PAF = byFunction(parenMethod)






}
