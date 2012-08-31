/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.common

case class Bet(player: String, game: Int, amount: Int)

case class PlayerBet(id: Int, bet: Bet)

case class ConfirmationMessage(id: Int)

case object RetrieveBets

case object RegisterProcessor
