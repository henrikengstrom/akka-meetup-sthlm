# Akka Kata in Scala

This repository contains an Akka kata that can be used whenever you feel like doing some Akka Karate related training.

## Prerequisites

* A computer
* An installed OS
* Java
* [SBT 0.12.2](http://www.scala-sbt.org/download.html)
* [Git](http://git-scm.com/downloads) _(not mandatory)_

## Getting Started (Git installed)

So you decided to install Git (or already had it installed). Smart move!
Open a terminal and type:

```
> git clone git://github.com/henrikengstrom/akka-meetup-sthlm.git
```

## Getting Started (manually - Git unavailable)

Open a browser and point it to:

https://github.com/henrikengstrom/akka-meetup-sthlm/archive/master.zip

Select your preferred flavor of compression (zip or tar.gz), download and extract onto your machine.

## SBT

To compile your project code with SBT:

```
> sbt compile
```

## Eclipse the project

Open a terminal and type:

```
> sbt eclipse
```

Open Eclipse and point it to the project catalogue.

## IntelliJ the project

Open a terminal window and type:

```
> sbt gen-idea
```

Open IntelliJ and point it to the project catalogue.

## The Kata ("osu sensei")

The aim with this kata is to show some core elements of Akka:
* Remoting
* Supervision
* Some Akka patterns

To showcase the elements above we have selected to implement a simple betting application - or at least provide a skeleton of such an application.
The implemented application should simulate a transacted system, i.e. it should handle a crash of a JVM.
We will discuss pros and cons of alternative implementations during the meetup.

The application you create will run in two different JVMs (and actor systems). One "node", called _betting service_, receives bet messages from a client,
creates a transaction number and sends this message to the other "node" _betting processor_. The betting service keeps track of messages sent and should also
handle confirmation messages from the betting processor. It also handles re-sending of messages that have not been confirmed. 

The task of the betting processor is to spawn workers that do the dangerous job (in this case interacting with an unstable service), 
supervise these workers and send back confirmation that a task has been performed. 

The _betting service_ should be able to function without any available _betting processor_, i.e. should it receive bet(s) before the _betting processor_ has
registered it should keep these bets locally and send them as soon as a _betting processor_ becomes available.

Sometimes your servers crash(!) and therefore you should design with this in mind. Sending too many bets to the _betting processor_ will cause
it (the JVM) to crash. It is an essential part of this kata to make sure that the _betting service_ can handle such a crash.

We will provide some alternative implementations to show how to solve the different tasks/assignments raised in the code (see comments in provided code). 

## Starting The Parts of the Kata

To start the _betting service_ and the _betting processor_ you should create some start scripts. This is easily done by using with the command:

```
> sbt start-script
```

This will create bash scripts in `service/target/start`, `processor/target/start` and `client/target/start`. To start things you just have to run the scripts from different command prompts in the top level project directory starting with `service` followed by `processor`.

If you're on Windows without access to a bash shell then you will have to copy the command line and arguments into a script of your own, and rewrite the paths to windows style paths.

**Note that the scripts are by default placed in target directory and they will be deleted if you do:**

```
> sbt clean
```

Start the service

```
> service/target/start
```

The next step is to start the processor
```
> processor/target/start
```

Finally you should run the client. Start off by sending bets to the service

```
> client/target/start send
```

The final step is to retrieve the bets from the service

```
> client/target/start
```

Remember to clean out the _persistent store_ of bets in between runs of your system. It is stored in the file `persistent_store` in the top level project directory.

## Green Belt Akka

For a small collection of akka information useful for this kata see [Green Belt Akka](https://github.com/henrikengstrom/akka-meetup-sthlm/blob/master/GREEN_BELT_AKKA.md)

## Authors

* Henrik Engström : [@h3nk3](http://twitter.com/h3nk3)
* Björn Antonsson : [@bantonsson](http://twitter.com/bantonsson)

## Props

Props to [Typesafe](http://www.typesafe.com) for paying for the time taken to implement this kata example.