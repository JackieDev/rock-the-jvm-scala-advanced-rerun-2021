package com.company.lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritence" - type substitution of generics

  class Cage[T] // question - should a Cage[Cat] inherit from Cage[Animal] ?
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  //val icage: CCage[Animal] = new ICage[Cat]
  //val x: Int = "Hello world"

  // hell no - opposite - contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // Covariant position

  //class ContravariantCage[-T](val animal: T)
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  //class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

  //class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  class InvariantVariableCage[T](var animal: T) // compiler is happy :)

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // CONTRAVARIANT POSITION
//  }
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    // add method receives a type B which is a supertype of A
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
   // def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // would return an evil cat!!!
     */

    // S is a Subtype of T
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  //val evilCat = shop.get(true, new Cat) - doesn't compile
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova) // the Dog shop gives us a Dog :)

  /*
    Big rule
    - method arguments are in CONTRAVARIANT position
    - return types are in COVARIANT position

    Covariant: if the ordering of types is preserved. (More specific to more generic)
    Contravariant: if it reverses this ordering: More generic to more specific
   */

  /*
    Exercises:
    1. Invariant, Covariant, and contravariant versions of this API:
      Parking[T](things: List[T]) {
        def park(vehicle: T)
        def impound(vehicles: List[T])
        def checkVehicles(conditions: String): List[T]
      }

    2. used someone else's API: IList[T]
    3. Parking = monad!
        - flatMap

   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  // my solutions

  // invariant
  class InvariantParking[T](vehicles: List[T]) {
    def park(vehicle: T): Boolean = true
    def impound(vehicles: List[T]) = vehicles
    def checkVehicles(conditions: String): List[T] = impound(vehicles)
  }

  // covariant
  trait CovariantParking[+T] {
    // R is a supertype of T
    def park[R >: T](vehicle: R): Boolean
    def impound[R >: T](vehicles: List[R])
    def checkVehicles(conditions: String): List[T]
  }

  // contravariant
  trait ContravariantParking[-T] {
    // R is a subtype of T
    def park[R <: T](vehicle: R): Boolean
    def impound[R <: T](vehicles: List[R])
    def checkVehicles[R <: T](conditions: String): List[R]
  }

  // invariant IList
  class IList[T](things: List[T]) {
    def addThing(thing: T): List[T] = ???
  }

  // covariant
  class CovariantList[+T](things: List[T]) {
    // S is a supertype of T
    def addThing[S >: T](thing: S): List[S] = ???
  }

  // contravariant
  class ContravariantList[-T](things: List[T]) {
    // S is a subtype of T
    def addThing[S <: T](thing: S): List[S] = ???
  }


  // What does Daniel say??
  // invariant
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    // I didn't try to implement flatMap in my solution
    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  // covariant
  class CParking[+T](vehicles: List[T]) {
    // S is a supertype of T
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  // contravariant
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    // S is a subtype of T
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
  }

  /*
    Rule of thumb:
     - use covariance = COLLECTION OF THINGS
     - use contravariance = GROUP OF ACTIONS
   */


  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???
  }

}
