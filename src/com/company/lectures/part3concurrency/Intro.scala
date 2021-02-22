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
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results
  // I always get 5 hello print followed by 5 goodbye
  // Hmmmmmmmmmmmmmmmm very fast processor???


  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //pool.execute(() => println("should not appear")) // throws an exception in the calling thread

  //pool.shutdownNow()
  println(pool.isShutdown) // will return true

}
