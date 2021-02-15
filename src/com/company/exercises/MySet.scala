package com.company.exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  /*
    Exercise - implement a functional set
   */
  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  /*
    Lecture 11: Enhancing a Functional Set
    Exercise #2
     - removing an element
     - intersection with another set
     - difference with another set
   */
  def -(elem: A): MySet[A]
  def difference(anotherSet: MySet[A]): MySet[A]
  def intersect(anotherSet: MySet[A]): MySet[A] // Daniel names this & and returns a MySet[A], I originally had Option[A]

  // Exercise #3 - implement a unary_! = NEGATION of a set // return everything but the given elements
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this) // Daniel uses this rather than null here, I was using null and MySetPlayground would crash with NullPointerException
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = new EmptySet[A] // Daniel uses this here as well
  def foreach(f: A => Unit): Unit = ()

  /*
    Lecture 11: Enhancing a Functional Set/ part 2
    Exercise #2
     - removing an element
     - intersection with another set
     - difference with another set
   */
  def -(elem: A): MySet[A] = new EmptySet[A] // Daniel returns this for all three functions here
  def difference(anotherSet: MySet[A]): MySet[A] = new EmptySet[A]
  def intersect(anotherSet: MySet[A]): MySet[A] = new EmptySet[A]

  // Exercise #3 - implement a unary_! = NEGATION of a set // return everything but the given elements
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}


//class AllInclusiveSet[A] extends MySet[A] {
//
//  def contains(elem: A): Boolean = true
//  def +(elem: A): MySet[A] = this
//  def ++(anotherSet: MySet[A]): MySet[A] = this
//  def map[B](f: A => B): MySet[B] =
//  def flatMap[B](f: A => MySet[B]): MySet[B]
//  def filter(predicate: A => Boolean): MySet[A]
//  def foreach(f: A => Unit): Unit
//  def -(elem: A): MySet[A]
//  def difference(anotherSet: MySet[A]): MySet[A]
//  def intersect(anotherSet: MySet[A]): MySet[A]
//  def unary_! : MySet[A]
//}

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

  def contains(elem: A): Boolean = property(elem)
  def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x == elem)

  def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  def foreach(f: A => Unit): Unit = politelyFail

  def -(elem: A): MySet[A] = filter(_ != elem)
  def difference(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def intersect(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}


class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean =
    head == elem || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = new NonEmptySet[A](this.head, tail ++ anotherSet)

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head) // Daniel's solution

  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head) // Daniel's solution

  def filter(predicate: A => Boolean): MySet[A] = { // Daniel's solution
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }


  /*
    Lecture 11: Enhancing a Functional Set
    Exercise #2
     - removing an element
     - intersection with another set
     - difference with another set
   */

  def -(elem: A): MySet[A] =
    if (this contains(elem)) this.-(elem)
    else this

  // Daniel's def - solution:
  def remove(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head

  // Daniel's solutions
  def difference(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def intersect(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)  //intersection is the same thing as filtering

  // Exercise #3 - implement a unary_! = NEGATION of a set // return everything but the given elements
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))

}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10 * x)) filter(_ % 2 == 0) foreach println

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0) // this is keep all the evens that are not 2 or 4
  println(negativeEven(5)) // so this could be 6, 8, 10, 12, etc
  println(s"expecting this to be true: ${negativeEven(8)}") // awesome

  val negativeEvenPlus5 = negativeEven + 5 // all the even numbers > 4 + 5
  println(negativeEvenPlus5(5)) // so this will be true as 5 was added and it's not in s
}
