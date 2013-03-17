/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.repository

import com.typesafe.akkademo.common._
import java.io.{ FileWriter, File }
import scala.io.Source

trait UnstableResource {
  def save(idempotentId: Int, player: String, game: Int, amount: Int): Unit
  def findAll: Seq[Bet]
}

class ReallyUnstableResource extends UnstableResource {
  val bets = scala.collection.mutable.Map[Int, Bet]()
  val randomizer = new scala.util.Random
  val store = new File("persistent_store")

  try {
    Source.fromFile(store).getLines().foreach(s ⇒ deserialize(s).foreach {
      case (id, player, game, amount) ⇒ if (!bets.contains(id)) bets.put(id, Bet(player, game, amount))
    })
  } catch {
    case _: Exception ⇒
  }

  def save(id: Int, player: String, game: Int, amount: Int) = {
    if (id % (randomizer.nextInt(10) + 10) == 0) throw new RuntimeException("Hey, I did not count on this happening...")
    if (id % (randomizer.nextInt(17) + 17) == 0) throw new DatabaseFailureException("Help. The database's gone haywire!")
    if (id % (randomizer.nextInt(121) + 50) == 0) System.exit(1)

    if (!bets.contains(id)) {
      bets += id -> Bet(player, game, amount)
      val fw = new FileWriter(store, true)
      try { fw.write(serialize(id, player, game, amount) + "\n") } finally { fw.close() }
    }
  }

  def findAll: Seq[Bet] = {
    bets.values.toSeq
  }

  protected def serialize(id: Int, player: String, game: Int, amount: Int): String = {
    id + ":" + player + ":" + game + ":" + amount
  }

  protected def deserialize(s: String): Option[(Int, String, Int, Int)] = {
    s.split(":").toList match {
      case id :: player :: game :: amount :: Nil ⇒
        try {
          Option((id.toInt, player, game.toInt, amount.toInt))
        } catch {
          case _: Exception ⇒ None
        }
      case _ ⇒ None
    }
  }
}

class DatabaseFailureException(msg: String) extends Exception
