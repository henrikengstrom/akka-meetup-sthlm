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
      (a.player compare b.player) match {
        case 0 ⇒
          (a.game compare b.game) match {
            case 0 ⇒ a.amount compare b.amount
            case g ⇒ g
          }
        case p ⇒ p
      }
    }
  }
}

