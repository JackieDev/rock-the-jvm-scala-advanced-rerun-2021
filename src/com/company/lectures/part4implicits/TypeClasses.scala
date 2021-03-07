package com.company.lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com").toHtml
  /*
    Disadvantages
    1. Only works for the types WE write
    2. ONE implementation out of quite a number
   */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n,a,e) =>
      case _ =>
    }
  }
  /*
    Disadvantages
    1. lost type safely
    2. need to modify code every time
    3. still ONE implementation
   */


  // A better way ...
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))

  // Advantages
  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} </div>"
  }

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /*
    Exercise: Equality
   compare users by both name, and by (name and email)
   */

  // my solution
  trait Equal[T] {
    def equals(value1: T, value2: T): Boolean
  }

  object NameEqual extends Equal[User] {
    def equals(user1: User, user2: User): Boolean = user1.name == user2.name
  }

  implicit object NameAndEmailEqual extends Equal[User] {
    def equals(user1: User, user2: User): Boolean =
      (user1.name == user2.name) && (user1.email == user2.email)
  }
  // Daniel says ...
  // He does something very similar to the ^ :) AWESOME!!!


  // Lecture 36: Part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // we have access to the entire type class interface via the apply method
  println(HTMLSerializer[User].serialize(john))

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

  val anotherJohn = User("John", 45, "anotherjohn@rockthejvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism
  

}
