/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor.service

import akka.actor.{ActorLogging, Actor}
import com.typesafe.akkademo.common.{ PlayerBet, RetrieveBets }

class BettingProcessor extends Actor with ActorLogging {

  /**
   * TASKS :
   * Send remote registration message to service
   * Create worker for dangerous task (using UnstableRepository actor)
   * Supervise worker -> handle errors
   * Send confirmation message back to Betting service
   */
  def receive = {
    case bet: PlayerBet ⇒
    case RetrieveBets   ⇒
  }
}