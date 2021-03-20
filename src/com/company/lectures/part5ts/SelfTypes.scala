package com.company.lectures.part5ts

object SelfTypes extends App {

  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // SELF TYPE: forces whoever implements Singer to implement Instrumentalist
    def sing(): Unit
  }

  // this is valid
  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // can't just extend Singer
//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("playing guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T


  // CAKE PATTERN => "dependency injection"

  // classic DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // Cake pattern
  trait ScalaComponent {
    // API
    def action(n: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose components
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependencies

//  class X extends Y
//  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }



}
