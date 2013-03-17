/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.service

import akka.actor.{ OneForOneStrategy, Props, ActorLogging, Actor }
import scala.concurrent.duration._
import com.typesafe.akkademo.common.{ RegisterProcessor, PlayerBet, RetrieveBets }
import akka.actor.SupervisorStrategy.Restart
import com.typesafe.akkademo.processor.repository.DatabaseFailureException
import scala.concurrent.ExecutionContext.Implicits.global

case object InitializeProcessor

class BettingProcessor extends Actor with ActorLogging {
  val worker = context.actorOf(Props[ProcessorWorker], "theWorker")
  val service = context.actorFor(context.system.settings.config.getString("betting-service-actor"))
  val scheduler = context.system.scheduler.schedule(1 seconds, 1 seconds, self, InitializeProcessor)

  override def postStop() {
    // Prevents the scheduler from being scheduled more than once (in case of restart of this actor)
    scheduler.cancel()
  }

  override val supervisorStrategy = OneForOneStrategy() {
    case r: RuntimeException         ⇒ Restart
    case d: DatabaseFailureException ⇒ Restart
    // Read more about fault tolerance here: http://doc.akka.io/docs/akka/2.0.3/scala/fault-tolerance.html
  }

  def receive = {
    case InitializeProcessor ⇒ service ! RegisterProcessor

    case bet: PlayerBet ⇒
      log.info("Storing bet: " + bet)
      worker.forward(bet)

    case RetrieveBets ⇒
      log.info("Retrieving all bets")
      worker.forward(RetrieveBets)
  }
}