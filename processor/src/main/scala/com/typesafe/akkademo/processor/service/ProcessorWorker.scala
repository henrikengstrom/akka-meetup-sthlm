/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.service

import akka.actor.Actor
import com.typesafe.akkademo.common.{ ConfirmationMessage, RetrieveBets, Bet, PlayerBet }
import com.typesafe.akkademo.processor.repository.ReallyUnstableResource

class ProcessorWorker extends Actor {
  import ProcessorWorker._

  def receive = {
    case PlayerBet(id, Bet(player, game, amount)) ⇒
      repo.save(id, player, game, amount)
      sender ! ConfirmationMessage(id)
    case RetrieveBets ⇒ sender ! repo.findAll.toList
  }
}

// This companion object represents some sort of simple resource pool
object ProcessorWorker {
  val repo = new ReallyUnstableResource
}
