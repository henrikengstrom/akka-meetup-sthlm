# Akka Kata for Stockholm Meetup 

This repository contains an Akka kata to be used during the Stockholm Scala Meetup group (or whenever you feel like doing some Akka Karate related training).

## Prerequisites

* A computer
* An installed OS
* Java
* [SBT 0.12.0](http://www.scala-sbt.org/download.html)
* [Git](http://git-scm.com/downloads) _(not mandatory)_

## Getting Started (Git installed)

So you decided to install Git (or already had it installed). Smart move!
Open a terminal and type:

__> git clone git@github.com:henrikengstrom/akka-meetup-sthlm.git__

## Getting Started (manually - Git unavailable)

Open a browser and point it to:

__https://github.com/henrikengstrom/akka-meetup-sthlm/downloads__

Select your preferred flavor of compression (zip or tar.gz), download and extract onto your machine.

## SBT

To compile your project code with SBT:

> sbt compile

## Eclipse the project

Open a terminal and type:

> sbt eclipse

Open Eclipse and point it to the project catalogue.

## IntelliJ the project

Open a terminal window and type:

> sbt gen-idea

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

We will provide some alternative implementations to show how to solve the different tasks/assignments raised in the code (see comments in provided code).

## Authors

* Henrik Engström : [@h3nk3](http://twitter.com/h3nk3)
* Björn Antonsson : [@bantonsson](http://twitter.com/bantonsson)

## Props

Props to [Typesafe](http://www.typesafe.com) for paying for the time taken to implement this kata example.