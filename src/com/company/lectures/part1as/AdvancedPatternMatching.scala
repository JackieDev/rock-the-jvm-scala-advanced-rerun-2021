package com.company.lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element it $head")
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n and I am $a yo."
  }
  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)


  /*
    Exercise. Custom pattern matching
   */
  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  } // make this nicer

  // My solution
  class MathProperty(n: Int)
  object MathProperty {
//    def unapply(mathProperty100: MathProperty): Option[Int] = {
//      if (mathProperty100.n > 100) Some(1) else None
//    }

    def unapply(number: Int): Option[String] = number match {
      case x if x < 10 => Some("single digit")
      case x if x % 2 == 0 => Some("an even number")
      case _ => Some("no property")
    }
  }

  val mathProperty3 = new MathProperty(3)
  val mathProp3 = mathProperty3 //match {
    //case MathProperty(prop) => println(prop)
  //}

  // Daniel's solution
  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n2: Int = 8
  val mathProperty2 = n2 match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  println(s"Daniel's solution result: $mathProperty2")

}
