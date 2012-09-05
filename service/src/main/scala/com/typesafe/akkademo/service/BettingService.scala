/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.service

import akka.actor._
import akka.util.duration._
import akka.remote.RemoteServerClientDisconnected

import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.akkademo.common._
import com.typesafe.akkademo.common.Bet
import com.typesafe.akkademo.common.ConfirmationMessage
import com.typesafe.akkademo.common.PlayerBet

import scala.Some

case object HandleUnprocessedBets

class BettingService extends Actor with ActorLogging {
  val ActivePeriod = 2000L
  val sequence = new AtomicInteger(1)
  var processor: Option[(ActorRef, Long)] = None

  // Note: To make this solution (even) more bullet proof you would have to persist the incoming bets.
  val bets = scala.collection.mutable.Map[Int, Bet]()
  val scheduler = context.system.scheduler.schedule(2 seconds, 2 seconds, self, HandleUnprocessedBets)

  override def postStop() {
    // Prevents the scheduler from being scheduled more than once (in case of restart of this actor)
    scheduler.cancel()
  }

  def receive = {
    case RegisterProcessor ⇒ registerProcessor(sender)
    case bet: Bet ⇒
      val playerBet = processBet(bet)
      for (p ← getActiveProcessor) p ! playerBet
    case RetrieveBets            ⇒ for (p ← getActiveProcessor) p.forward(RetrieveBets)
    case ConfirmationMessage(id) ⇒ handleProcessedBet(id)
    case HandleUnprocessedBets   ⇒ handleUnprocessedBets()
    // In the upcoming clustering we will be able to listen to remote clients and their status.
    // With this it will be possible to prevent sending messages to a client that is no longer available.
    // e.g. case RemoteClientDead (or similar) => processor = None
    // In this solution we use heartbeats instead.
  }

  def registerProcessor(sender: ActorRef) = {
    processor = Some((sender, System.currentTimeMillis))
  }

  def getActiveProcessor: Option[ActorRef] = {
    processor.flatMap {
      case (s, t) => if (System.currentTimeMillis - t < ActivePeriod) Some(s) else None
    }
  }

  def processBet(bet: Bet): PlayerBet = {
    val id = sequence.getAndIncrement()
    bets += id -> bet
    PlayerBet(id, bet)
  }

  def handleProcessedBet(id: Int) = {
    log.info("processed bet: " + id)
    bets -= id
  }

  def handleUnprocessedBets() = {
    // In a real world solution you should probably timestamp each message sent so that you do not
    // resend just sent messages -> takes some pressure off the processor.

    // Since this is just a demo we'll just treat all messages in the map as unhandled and resend them all.
    // Please make sure you understand that I can do this since the processor repository is idempotent!

    // To not flood the processor actor system you might want to use throttling. A good blog post about this van be found here:
    // http://letitcrash.com/post/28901663062/throttling-messages-in-akka-2

    log.info("handling unprocessed bets (size): " + bets.size)
    getActiveProcessor.foreach {p => bets.keys.foreach { k => p ! PlayerBet(k, bets(k))}}
  }
}