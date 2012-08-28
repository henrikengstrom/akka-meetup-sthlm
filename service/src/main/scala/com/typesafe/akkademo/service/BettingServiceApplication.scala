/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.service

import akka.kernel.Bootable
import akka.actor.{ Props, ActorSystem }
import com.typesafe.config._

class BettingServiceApplication extends Bootable {
	val config = ConfigFactory.load()
	val system = ActorSystem("BettingServiceActorSystem", config)

	def startup = {
		system.actorOf(Props[BettingService], "bettingService")
  }
 
  def shutdown = {
    system.shutdown()
  }
}