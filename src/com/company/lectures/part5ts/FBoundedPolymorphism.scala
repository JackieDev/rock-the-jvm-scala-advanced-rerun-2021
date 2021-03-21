package com.company.lectures.part5ts

object FBoundedPolymorphism extends App {

//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // we want a List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // we want a List[Dog]
//  }

  // Solution 1 - naive!
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Cat] = ??? // we want a List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Dog] = ??? // we want a List[Dog]
//  }

  // Solution 2 - FBP
//  trait Animal[A <: Animal[A]] { // recursive type: FBounded Polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // we want a List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // we want a List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM - often found in database code: Object-relational mapping
//  class Person extends Comparable[Person] { // another example of FBP
//    override def compareTo(t: Person): Int = ???
//  }
//
//  // can still have problems, such as allowing this ...
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }


  // Solution 3 - FBP + self-types
//  trait Animal[A <: Animal[A]] { self: A =>
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = ??? // we want a List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ??? // we want a List[Dog]
//  }

  // so we can no longer do this: which is good!
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = ???
//  }

//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed: List[Animal[Fish]] = List(new Cod) // this is wrong! This is the limitation of FBP
//  }
//
//  class Cod extends Fish {
//    override def breed: List[Animal[Fish]] = ???
//  }

  // Exercise, can we think of a better solution?

  // from Daniel
  // Solution 4 - type classes

//  trait Animal
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  } // type class description
//
//  class Dog extends Animal
//  object Dog {
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = ???
//    }
//  } // type class instance
//
//  implicit class CanBreedOps[A](animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed // we can be sure that this will return List[Dog] :)
//  /*
//    new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
//    implicit value to pass to bread: Dog.DogsCanBreed
//   */
//
//   class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      def breed(a: Dog): List[Dog] = ???
//    }
//  }
//
//  val cat = new Cat
  //cat.breed // no implicits found for CanBreed[Cat]


  // Solution 5 - pure type classes
  trait Animal[A] {
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

  val cat = new Cat
  //cat.breed // we can't do this, which is good!!

}
