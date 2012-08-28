/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

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
	service ! Bet("ready_player_one", 1, 1)

	println("*** DONE TESTING")
}