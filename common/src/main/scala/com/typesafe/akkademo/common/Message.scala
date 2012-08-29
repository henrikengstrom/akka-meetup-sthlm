/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.common

case class Bet(player: String, game: Int, amount: Int)

case class PlayerBet(id: Long, bet: Bet)

case class ConfirmationMessage(id: Long)

case object RetrieveBets