package com.company.exercises

import com.company.lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

  /*
    Exercise: Equality
   compare users by both name, and by (name and email)
   */

  // my solution
  trait Equal[T] {
    def equals(value1: T, value2: T): Boolean
  }

  implicit object NameEqual extends Equal[User] {
    def equals(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  object NameAndEmailEqual extends Equal[User] {
    def equals(user1: User, user2: User): Boolean =
      (user1.name == user2.name) && (user1.email == user2.email)
  }
  // Daniel says ...
  // He does something very similar to the ^ :) AWESOME!!!


  /*
    Exercise: implement the TC pattern for the Equality tc
   */
  // my solution
  //  object Equal {
  //    def apply[T](implicit instance: Equal[T]) = instance
  //  }
  // I missed passing in a and b

  // Daniel says ...
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.equals(a, b)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 45, "anotherjohn@rockthejvm.com")
  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism


  /*
     Exercise - improve the Equal TC with an implicit conversion class
      - ===(anotherValue: T)
      - !==(anotherValue: T)
   */

  // my solution
  implicit class EqualEnrichment[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer.equals(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer.equals(value, anotherValue)
  }

  // What does Daniel say: His solution is the same :)

  println(john === anotherJohn)
  //TYPE SAFE ===


}
