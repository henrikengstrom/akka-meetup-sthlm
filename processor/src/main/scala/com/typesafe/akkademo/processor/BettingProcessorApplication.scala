/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor

import akka.actor.{ Props, ActorSystem }
import com.typesafe.config._
import service.{ InitializeProcessor, BettingProcessor }

object BettingProcessorApplication extends App {
  val config = ConfigFactory.load()

  val system = ActorSystem("BettingProcessorActorSystem", config)

  val bettingProcessor = system.actorOf(Props[BettingProcessor], "bettingProcessor")

  bettingProcessor ! InitializeProcessor
}
