# Green Belt Akka

This doucment will briefly introduce some important parts of Akka _specific for the implementation of this kata_. 

The official Akka documentation is really good (if we may say so ourselves): [Akka Docs](http://doc.akka.io/docs/akka/2.1.2/)  

_Please note_ that this document describes parts of Akka very briefly and we refer to the original documentation for an in-depth description of Akka.

## Akka Essentials

Below is a brief introduction of some concepts you will need for this kata with some pointers to where you can read more.

### Actor Systems

An actor system is, among other things, the context in which actors operate. You can have multiple actor systems within the same JVM.

See [Actor Systems](http://doc.akka.io/docs/akka/2.1.2/general/actor-systems.html)

**Creating ActorSystems**

```
val system = ActorSystem("MyActorSystem")
```

### Working with Actors

See [Actors](http://doc.akka.io/docs/akka/2.1.2/scala/actors.html)


**Creating actors**

In the system context, called top level actors (to be used sparsely). This creates an actor under "/user/myActorName".

```
val myActor = system.actorOf(Props[MyActor], "myActorName")
```

In the actor context, called children (i.e. when you're inside an actor)

```
val myActor = context.actorOf(Props[MyActor], "myActorName")
```

**Looking up actors**

Look up an actor by name (complete path)

```
val myActor = system.actorFor("/user/myActorName")
val remoteActor = system.actorFor("akka://OtherActorSystem@host:port/user/otherActorName")
```

**Sending messages**

Fire and forget

```
myActor ! "A message"
```

As futures (has performance implications)

```
import akka.pattern.ask

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

**Replying**

class MyActor extends Actor {
  def receive = {
	case "A message" => sender ! "Got it"
  }
}
```

**Supervising actors**

See [Fault Tolerance](http://doc.akka.io/docs/akka/2.1.2/scala/fault-tolerance.html)

```
override val supervisorStrategy = OneForOneStrategy() {
  case _ => Restart // Stop, ...
} 
```

### Misc Tasks


**Scheduling messages**

To schedule a message to be sent sometime in the future, once or repeatedly use the scheduler.

See [Scheduler](http://doc.akka.io/docs/akka/2.1.2/scala/scheduler.html)

```
//Use the system's default dispatcher as ExecutionContext
import system.dispatcher
system.scheduler.schedule(2 seconds, 2 seconds, actor, "every other second message")
```

**Retrieving properties**

As you can see in the code there are some properties predefined in the ``application.conf`` file. To retrieve these properties, in the context of an actor, you can use the following:

```
context.system.settings.config.getString("...")

```
  