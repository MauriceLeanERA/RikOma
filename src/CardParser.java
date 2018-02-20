/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 * @author Maurice Bernard
 */
public class CardParser {

  private String[] cards;
  //private LoggerOutput logger = new LoggerOutput("cardParser");

  public void parseCommunityCards(Table table, String cardsString) {
    cardsString = cardsString.substring(1, cardsString.length() - 1);
    cards = cardsString.split(",");
    switch (cards.length) {

      case 3:
        table.getcCards().setFlop(parseCards(0, 2, CardOwnerEnum.Community));
        break;

      case 4:
        table.getcCards().setFlop(parseCards(0, 2, CardOwnerEnum.Community));
        table.getcCards().setTurn(parseCards(3, 3, CardOwnerEnum.Community));
        //turn
        break;

      case 5:
        table.getcCards().setFlop(parseCards(0, 2, CardOwnerEnum.Community));
        table.getcCards().setTurn(parseCards(3, 3, CardOwnerEnum.Community));
        table.getcCards().setRiver(parseCards(4, 4, CardOwnerEnum.Community));
        //river
        break;

      default:
        break;
    }
  }

  public void parseHand(Player player, String handString) {
    handString = handString.substring(1, handString.length() - 1);
    cards = handString.split(",");
    player.setHand(new Hand(parseCards(0, 3, CardOwnerEnum.Self)));
  }

  private ArrayList<Card> parseCards(int startIndex, int endIndex, CardOwnerEnum owner) {
    ArrayList<Card> comCards = new ArrayList<>();
    for (int i = startIndex; startIndex <= endIndex; startIndex++) {
      String value = cards[i].substring(0, cards[i].length() - 1);
      String color = cards[i].substring(cards[i].length() - 1, cards[i].length());
      CardColorEnum colorEnum = CardColorEnum.None;
      int cardValue = 0;
      int CardValue2 = 0;
      switch (color) {
        case "d":
          colorEnum = CardColorEnum.Diamonds;
          break;

        case "h":
          colorEnum = CardColorEnum.Hearts;
          break;

        case "s":
          colorEnum = CardColorEnum.Spades;
          break;

        case "c":
          colorEnum = CardColorEnum.Clubs;
          break;

        default:
          break;
      }

      switch (value) {
        case "A":
          comCards.add(new Card(colorEnum, 1, owner));
          comCards.add(new Card(colorEnum, 14, owner));
          break;
        case "J":
          comCards.add(new Card(colorEnum, 11, owner));
          break;
        case "Q":
          comCards.add(new Card(colorEnum, 12, owner));
          break;
        case "K":
          comCards.add(new Card(colorEnum, 13, owner));
          break;
        case "T":
          comCards.add(new Card(colorEnum, 10, owner));
          break;
        default:
          comCards.add(new Card(colorEnum, Integer.parseInt(value), owner));
          break;
      }
    }
    return comCards;
  }
}
