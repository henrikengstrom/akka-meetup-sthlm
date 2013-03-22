/**
 *  Copyright (C) 2011-2013 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.service

import akka.actor.{ Props, ActorSystem }
import com.typesafe.config._

object BettingServiceApplication extends App {
  val system = ActorSystem("BettingServiceActorSystem", ConfigFactory.load())

  val service = system.actorOf(Props[BettingService], "bettingService")
}
