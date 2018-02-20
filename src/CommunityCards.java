/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 * @author Maurice Bernard
 */
public class CommunityCards {
  private ArrayList<Card> flop = new ArrayList<>();   //ersten cCards
  private ArrayList<Card> turn = new ArrayList<>();   //zweiten cCards
  private ArrayList<Card> river = new ArrayList<>();  //dritten cCards

  /**
   * @return the flop
   */
  public ArrayList<Card> getFlop() {
    return flop;
  }

  /**
   * @param flop the flop to set
   */
  public void setFlop(ArrayList<Card> flop) {
    this.flop = flop;
  }

  /**
   * @return the river
   */
  public ArrayList<Card> getRiver() {
    return river;
  }

  /**
   * @param river the river to set
   */
  public void setRiver(ArrayList<Card> river) {
    this.river = river;
  }

  /**
   * @return the turn
   */
  public ArrayList<Card> getTurn() {
    return turn;
  }

  /**
   * @param turn the turn to set
   */
  public void setTurn(ArrayList<Card> turn) {
    this.turn = turn;
  }

  public ArrayList<Card> getAllCards() {
    ArrayList<Card> returnList = new ArrayList<>();
    returnList.addAll(flop);
    returnList.addAll(turn);
    returnList.addAll(river);
    return returnList;
  }
}
