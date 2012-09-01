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
  val sequence = new AtomicInteger(1)
  var processor: Option[ActorRef] = None
  val bets = scala.collection.mutable.Map[Int, Bet]()

  context.system.eventStream.subscribe(self, classOf[RemoteServerClientDisconnected])
  context.system.scheduler.schedule(2 seconds, 2 seconds, self, HandleUnprocessedBets)

  def receive = {
    case RegisterProcessor ⇒
      log.info("processor registered")
      processor = Some(sender)
    case bet: Bet ⇒
      val playerBet = processBet(bet)
      for (p ← processor) p ! playerBet
    case RetrieveBets ⇒
      log.info("retrieving bets perhaps... processor defined? -> " + processor.isDefined)
      for (p ← processor) p.tell(RetrieveBets, sender)
    case ConfirmationMessage(id) ⇒ handleProcessedBet(id)
    case HandleUnprocessedBets   ⇒ handleUnprocessedBets()
    case r: RemoteServerClientDisconnected ⇒
      log.info("processor unregistered")
      processor = None
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

    // Since this is just a demo I'll just treat all messages in the map as unhandled and resend them all.
    // Please make sure you understand that I can do this since the processor repository is idempotent!

    log.info("handling unprocessed bets (size): " + bets.size)
    if (processor.isDefined) bets.keys.foreach { k ⇒ for (p ← processor) p ! PlayerBet(k, bets(k)) }
  }
}