package com.company.lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String

    2
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner // o.Inner is a Type

  val oo = new Outer
  //val otherInner: oo.Inner = new o.Inner

  o.print(inner)
  // oo.print(inner)

  // path-dependent types

  // All inner types have a common supertype: Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)

  /*
    Exercise
     DB keyed by Int or String, but maybe others
   */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */
  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42 ) // this is ok
  get[StringItem]("home") // also ok

  //get[IntItem]("scala") // not ok


  // my attempt
//  class Item[T] {
//    class ItemT(value: T)
//
//    def get: ItemT = value
//  }

  //what does Daniel say??
  // See the traits ItemLike and Item[K] above

}
