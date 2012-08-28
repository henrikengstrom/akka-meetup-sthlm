/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor

import akka.kernel.Bootable
import akka.actor.{ Props, ActorSystem }
import com.typesafe.config._
import com.typesafe.akkademo.processor.service.BettingProcessor

class BettingProcessorApplication extends Bootable {
	val config = ConfigFactory.load()
	val system = ActorSystem("BettingProcessorActorSystem", config)

	def startup = {
		system.actorOf(Props[BettingProcessor], "bettingProcessor")
  }
 
  def shutdown = {
    system.shutdown()
  }
}