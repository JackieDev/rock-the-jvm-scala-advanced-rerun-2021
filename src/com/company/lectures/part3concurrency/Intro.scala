package com.company.lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }
  val aThread = new Thread(runnable)

  aThread.start() // this gives the signal to the JVM to start a JVM thread
  // creating a JVM thread => creates another OS thread?

  runnable.run() // doesn't do anything in parallel!
  aThread.join() // will block until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  //threadHello.start()
  //threadGoodbye.start()
  // different runs produce different results
  // I always get 5 hello print followed by 5 goodbye
  // Hmmmmmmmmmmmmmmmm very fast processor???


  // executors
  val pool = Executors.newFixedThreadPool(10)
  //pool.execute(() => println("something in the thread pool"))

//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("done after 1 second")
//  })
//
//  pool.execute(() => {
//    Thread.sleep(1000)
//    println("almost done")
//    Thread.sleep(1000)
//    println("done after 2 seconds")
//  })

  pool.shutdown()
  //pool.execute(() => println("should not appear")) // throws an exception in the calling thread

  //pool.shutdownNow()
  println(pool.isShutdown) // will return true


  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  //for (_ <- 1 to 10000) runInParallel
  // race condition

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //println("I've bought " + thing)
    //println("my account is now " + account)
  }

//  for (_ <- 1 to 10000) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "shoes", 3000))
//    val thread2 = new Thread(() => buy(account, "iPhone12", 4000))
//
//    thread1.start()
//    thread2.start()
//    Thread.sleep(10)
//    if (account.amount != 43000) println("AHA: " + account.amount)
//    //println()
//  }

  // There is a chance that thread2 will overwrite the account.amount to be 46000 and the 3000 for the shoes will never be taken from the account of 50000

  // solutions to solve this problem
  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      // no two threads can evaluate this at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("my account is now " + account)
    }

  // option #2: use @volatile


  /**
   * Exercises
   *
   * 1) Construct 50 "inception" threads
   *     Thread1 -> thread2 -> thread3 -> ...
   *     println("hello from thread #3")
   *     in REVERSE ORDER
   */
    // my solution, this won't print anything
  def inceptionThread(threadName: String): Thread = new Thread(() => println(s"hello from $threadName"))
  val inceptionThreads = for (i <- (1 to 50).reverse) inceptionThread("thread #" + i.toString)

  println(inceptionThreads)

  // Daniel's solution
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })

  inceptionThreads(50).start()
  // they print out in reverse order because I think the join above blocks until all threads have been created and then the println statement after is run starting from
  // last thread made to the first


  // Exercise 2
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())
  /**
   * Questions
   * 1. what is the biggest value possible for x? // my answer = 1 - incorrect, it could be 100
   * 2. what is the smallest value possible for x? // my answer = 1 - correct :)
   */

  // Exercise 3 - sleep fallacy
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala does NOT suck"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  // Question - whats the value of message?
  // is it guaranteed? why or why not?

  // my guess is the message will be "Scala is awesome" // this guess was correct :)
  // I think it is guaranteed as the line awesomeThread.start() comes after line message = "Scala does NOT suck"

  // Daniel says
  /*
    the message is ALMOST always "Scala is awesome"
    it is NOT guaranteed

    This can be fixed by using .join() on the awesomeThread
   */
  

}
