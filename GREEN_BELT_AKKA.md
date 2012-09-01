# Green Belt Akka

This doucment will briefly introduce some important parts of Akka _specific for the implementation of this kata_. 

The official Akka documentation is really good (if we may say so ourselves): [Akka Docs](http://doc.akka.io/docs/akka/2.0.3/)  

_Please note_ that this document describes parts of Akka very briefly and we refer to the original documentation for an in-depth description of Akka.

## Akka Essentials

Below is a brief introduction of some concepts you will need for this kata with some pointers to where you can read more.

### Actor Systems

An actor system is, among other things, the context in which actors operate.

See [Actor Systems](http://doc.akka.io/docs/akka/2.0.3/general/actor-systems.html)

**Creating ActorSystems**

```
val system = ActorSystem("MyActorSystem")
```

### Working with Actors

See [Actors](http://doc.akka.io/docs/akka/2.0.3/scala/actors.html)


**Creating actors**

In system context ()to be use sparesly) 

```
val myActor = system.actorOf(Props[MyActor], "myActorName")
```
In actor context (i.e. when you already have an actor)

```
val myActor = context.actorOf(Props[MyActor], "myActorName")
```

**Sending messages**

Fire and forget

```
myActor ! "A message"
```

As futures

```
val myFuture = myActor ? "A message"
```

Passing along the original sender

```
myActor.forward("Another message")
```

**Receiving messages**

```
class MyActor extends Actor {
  def receive = {
	case "A message" => println(""Received the message")
	case "Another message" => println("Received that other message") 
  }
}
```

**Supervising actors**

See [Fault Tolerance](http://doc.akka.io/docs/akka/2.0.3/scala/fault-tolerance.html)

```
override val supervisorStrategy = OneForOneStrategy() {
  case _ => Restart // Stop, ...
} 
```

### Misc Tasks

**Subscribing to events**

Could be good to use when to find out things about the context you're operating in. All subscribed events will be sent to the actor as a message, i.e. you should handle them in the actor's receive method.

See [Event Bus](http://doc.akka.io/docs/akka/2.0.3/scala/event-bus.html)

```
context.system.eventStream.subscribe(
  self, 
  classOf[RemoteServerClientDisconnected])
    
// …

def receive = {
  case r: RemoteServerClientDisconnected => println("Darn!!")
}  
```
