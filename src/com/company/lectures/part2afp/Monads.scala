package com.company.lectures.part2afp

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
    left-identity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x) // Success case!
    Success(x).flatMap(f) = f(x) // proved!


    right-identity

    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(e).flatMap(Nothing...) = Fail(e)


    associativity

    attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)
    Success(v).flatMap(f).flatMap(g) = f(v).flatMap(g) OR Fail(e)
    Success(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g) OR Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad!!!")
  }
  println(attempt)


  /*
    EXERCISES:
    1. Implement a Lazy[T] monad = computation which will only be executed when it's needed.

    unit/apply
    flatMap

    2. Monads = unit + flatMap
       Monads = unit + map + flatten (create map and flatten methods using flatMap)

       Monad[T] {

         def flatMap[B](f: T => Monad[B]): Monad[B] ... we have already implemented this
         def map[B](f: T => B): Monad[B] = ???
         def flatten(m: Monad[Monad[T]]): Monad[T] = ???

         (have list in mind)
       }
   */

  // Exercise 1 - Daniel's solutions
  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I feel like doing loads of scala :)")
    42
  }

  //println(lazyInstance.use) // will see the "Today I feel like doing loads of scala :)" actually print to the console

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use



  // Exercise 2
//  class Monad[+A] {
//    def flatMap[B](f: A => Monad[B]): Monad[B]
//    def map[B](f: A => B): Monad[B] = flatMap(x => unit(f(x)))
//    def flatten(m: Monad[Monad[A]]): Monad[A] = m.flatMap((x: Monad[A] => x))
//  }

  val mapped = List(1,2,3).map(_ * 2).flatMap(x => List(x * 2))
}



