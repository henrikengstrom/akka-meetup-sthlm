/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.processor

import akka.actor.{ Props, ActorSystem }
import com.typesafe.config._
import service.{ InitializeProcessor, BettingProcessor }

object BettingProcessorApplication extends App {
  val system = ActorSystem("BettingProcessorActorSystem", ConfigFactory.load())

  val bettingProcessor = system.actorOf(Props[BettingProcessor], "bettingProcessor")

  bettingProcessor ! InitializeProcessor
}
