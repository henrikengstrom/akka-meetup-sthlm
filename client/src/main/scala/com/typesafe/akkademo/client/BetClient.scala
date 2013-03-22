/**
 * Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.client

import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import com.typesafe.akkademo.common.{ Bet, RetrieveBets }
import com.typesafe.akkademo.common.Bet

object BetClient extends App {

  println("*** STARTING TEST OF BETTING APPLICATION")

  val config = ConfigFactory.parseString("""
    akka {
      actor {
        provider = "akka.remote.RemoteActorRefProvider"
      }
      remote {
        netty {
          hostname = "127.0.0.1"
          port = 2661
        }
      }
    }""")

  val system = ActorSystem("TestActorSystem", ConfigFactory.load(config))
  val service = system.actorFor("akka://BettingServiceActorSystem@127.0.0.1:2552/user/bettingService")

  try {
    // create the list of bets
    val bets = (1 to 200).map(p ⇒ Bet("ready_player_one", p % 10 + 1, p % 100 + 1))

    if (args.size > 0 && args.head == "send") {
      bets.foreach(bet ⇒ service ! bet)
      println("*** SENDING OK")
    } else {
      implicit val timeout = Timeout(2 seconds)
      val fBets = service.ask(RetrieveBets).mapTo[List[Bet]]
      val result = Await.result(fBets, 5 seconds).sorted
      assert(result == bets.sorted, s"expected ${bets.sorted}, got $result")
      println("*** TESTING OK")
    }
  } finally {
    // Wait a little to make sure that everything is finished before shutting the AS down
    Thread.sleep(1000)
    system.shutdown()
  }
}
