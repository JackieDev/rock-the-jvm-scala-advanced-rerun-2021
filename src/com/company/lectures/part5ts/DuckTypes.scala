package com.company.lectures.part5ts

object DuckTypes extends App {

  // structural types

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
  }

  //def closeQuietly(closeable: JavaCloseable OR HipsterCloseable) // wouldn't it be nice to be able to do this

  type UnifiedCloseable = {
    def close(): Unit
  } // structural type

  def closeQuietly(closeable: UnifiedCloseable): Unit = closeable.close()

  closeQuietly(new JavaCloseable {
    override def close(): Unit = ???
  })

  closeQuietly(new HipsterCloseable)


  // TYPE REFINEMENTS

  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit = advancedCloseable.closeSilently()

  closeShh(new AdvancedJavaCloseable)
  //closeShh(new HipsterCloseable)


  // using structural types as standalone types
  // the close method given here is its own type
  def altClose(closeable: { def close(): Unit }): Unit = closeable.close()


  // type-checking => duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // with one CAVEAT: based on reflection, big performance cost

  /*
    Exercises
   */
  // 1.
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZ!"
  }

  def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)

  /*
    Is f compatible with a CBL and with a Human?

    My thoughts; I think no as class Human has a head method which is of a different type (Brain), so the T wouldn't match

    Daniel says it is compatible, so I was wrong.
   */

  case object CBNil extends CBL[Nothing] {
    def head: Nothing = ???
    def tail: CBL[Nothing] = ???
  }

  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // what type is T? T is Brain!!

  // 2.
  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  /*
    is HeadEqualizer compatible with a CBL and with a Human?

    My thoughts; I think the answer is no here as well for the same reason.
    The right answer is yes.
   */
  val brainzList = CBCons(new Brain, CBNil)
  val stringsList = CBCons("Brainz", CBNil)

  HeadEqualizer.===(brainzList, new Human) // both return a brain for the head
  // problem:
  HeadEqualizer.===(new Human, stringsList) // the first argument returns a Brain but the second returns a String ("Brainz") - so this is not typesafe

}
