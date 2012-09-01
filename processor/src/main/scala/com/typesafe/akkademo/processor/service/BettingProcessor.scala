/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.service

import akka.actor.{OneForOneStrategy, Props, ActorLogging, Actor}
import com.typesafe.akkademo.common.{RegisterProcessor, PlayerBet, RetrieveBets}
import akka.actor.SupervisorStrategy.Restart
import com.typesafe.akkademo.processor.repository.DatabaseFailureException

case object InitializeProcessor

class BettingProcessor extends Actor with ActorLogging {

  val worker = context.actorOf(Props[ProcessorWorker], "theWorker")

  override val supervisorStrategy = OneForOneStrategy() {
    case r: RuntimeException => Restart
    case d: DatabaseFailureException => Restart
    // Read more about fault tolerance here: http://doc.akka.io/docs/akka/2.0.3/scala/fault-tolerance.html
  }

  def receive = {
    case InitializeProcessor =>
      log.info("Processor initializing...")
      context.actorFor(context.system.settings.config.getString("betting-service-actor")) ! RegisterProcessor

    case bet: PlayerBet =>
      log.info("Storing bet: " + bet)
      worker.tell(bet, sender)

    case RetrieveBets   =>
      log.info("Retrieving all bets")
      worker.tell(RetrieveBets, sender)
  }
}