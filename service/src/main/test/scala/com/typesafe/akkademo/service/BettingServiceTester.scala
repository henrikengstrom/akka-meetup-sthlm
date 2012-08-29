/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.akkademo.common.{Bet, RetrieveBets}

object BettingServiceTester extends App {
	println("*** STARTING TEST OF BETTING SERVICE")

	val config = ConfigFactory.parseString(""" 
	akka {
  	actor {
    	provider = "akka.remote.RemoteActorRefProvider"
  	}
  	remote {
    	transport = "akka.remote.netty.NettyRemoteTransport"
    	netty {
      	hostname = "127.0.0.1"
      	port = 2661
    	}
 	  }
	}""")

	// TODO : Implement better tests of the application

	val system = ActorSystem("TestSystem", ConfigFactory.load(config))
	val service = context.actorFor("akka://bettingServiceActorSystem@127.0.0.1:2552/user/bettingservice")
	val playerId = "ready_player_one"
	(1 to 200).foreach { p => service ! Bet(playerId, p % 10 + 1, p % 100 + 1) }

	implicit val timeout = Timeout(5 seconds)
	val fBets = service ? RetrieveBets

	// val fBets = ask(service, RetrieveBets).mapTo[Vector[Bet]]

	// In this case it is okay to use Await but are there any other ways of handling futures?
	val bets = Await.result(fBets, timeout.duration).asInstanceOf[Vector[Bet]]

	println("*** DONE TESTING")
}