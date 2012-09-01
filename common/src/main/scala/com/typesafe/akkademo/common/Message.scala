/**
 *  Copyright (C) 2011-2012 Typesafe <http://typesafe.com/>
 */
package com.typesafe.akkademo.common

case class Bet(player: String, game: Int, amount: Int)

case class PlayerBet(id: Int, bet: Bet)

case class ConfirmationMessage(id: Int)

case object RetrieveBets

case object RegisterProcessor

object Bet {
  implicit object BetOrdering extends Ordering[Bet] {
    def compare(a: Bet, b: Bet): Int = {
      val p = a.player compare b.player
      if (p == 0) {
        val g = a.game compare b.game
        if (g == 0) {
          a.amount compare b.amount
        } else
          g
      } else
        p
    }
  }
}

