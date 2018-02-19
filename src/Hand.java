/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author Maurice Bernard
 */
public class Hand {
    private ArrayList<Card> hand = new ArrayList<>();
    private int handValue = 0;
    
    public Hand(ArrayList<Card> hand)
    {
        this.hand = hand;
    }

    /**
     * @return the hand
     */
    public ArrayList<Card> getCards() {
        return hand;
    }

    /**
     * @return the handValue
     */
    public int getHandValue() {
        return handValue;
    }

    /**
     * @param handValue the handValue to set
     */
    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }
}
