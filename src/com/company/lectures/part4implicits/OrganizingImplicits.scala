package com.company.lectures.part4implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1,4,5,3,2).sorted)

  // scala.Predef - auto imported

  /*
     Potential implicits (used as implicit parameters):
      - val/var
      - objects
      - accessor methods = defs with no parentheses - putting () on the end of the reverseOrdering def above stops it being considered as an implicit
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 40)
  )

  object AlphabeticNameOrdering {
    // my solution
    implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)
  }

  import AlphabeticNameOrdering._
  println(persons.sorted)

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companion objects of all types involved in the method signature
       - List
       - Ordering
       - all the types involved = A or any supertype

   */
  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]


  /*
     Exercise: define implicit orderings for:
      - totalPrice = most used (50%)
      - by unit count = 25%
      - by unit price = 25%

   */
  case class Purchase(nUnits: Int, unitPrice: Double)

  // my solutions
  object Purchase {
    // most used so putting this inside the companion object
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((p1, p2) => (p1.nUnits * p1.unitPrice) < (p2.nUnits * p2.unitPrice))
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  // What does Daniel say?
  // Yeah my solutions above are good :)
}
