/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.service

import akka.actor.Actor
import com.typesafe.akkademo.common.Bet

class BettingService extends Actor {
  
  /**
  * TASKS:
  * Create unique sequence/transaction number
  * Create PlayerBet and call betting processor (remotely)
  * Handle timed out transactions (scheduler)
  */ 

  def receive = {
	case bet: Bet =>
  }
}