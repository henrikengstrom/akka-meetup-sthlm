/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common._

trait UnstableResource {
  def save(idempotentId: Int, player: String, game: Int, amount: Int): Unit
  def findAll: Seq[Bet]
}

class ReallyUnstableResource extends UnstableResource {
  val bets = scala.collection.mutable.Map[Int, Bet]()
  val randomizer = new scala.util.Random

  def save(id: Int, player: String, game: Int, amount: Int) = {
    if (id % (randomizer.nextInt(10) + 10) == 0) throw new RuntimeException("Hey, I did not count on this happening...")
    if (id % (randomizer.nextInt(17) + 17) == 0) throw new DatabaseFailureException("Help. The database's gone haywire!")
    if (id % (randomizer.nextInt(121) + 50) == 0) System.exit(1)

    if (!bets.contains(id)) bets += id -> Bet(player, game, amount)
  }

  def findAll: Seq[Bet] = {
    bets.values.toSeq
  }
}

class DatabaseFailureException(msg: String) extends Exception