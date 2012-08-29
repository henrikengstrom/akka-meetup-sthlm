/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common._

trait UnstableResource {
  def save(player: String, game: Int, amount: Int): Unit
  def findAll: Seq[Bet]
}

class ReallyUnstableResource extends UnstableResource {
  import java.util.concurrent.atomic.AtomicInteger

  var seq = new AtomicInteger()
  val bets = scala.collection.mutable.Map[Int, Bet]()

  def save(player: String, game: Int, amount: Int) = {
    val id = seq.getAndIncrement
    if (id % 3 == 0) throw new RuntimeException("Hey, I did not count on this happening...")
    if (id % 5 == 0) throw new DatabaseFailureException("Help. The database's gone haywire!")
    bets += id -> Bet(player, game, amount)
  }

  def findAll: Seq[Bet] = {
    bets.values.toSeq
  }
}

class DatabaseFailureException(msg: String) extends Exception