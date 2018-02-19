/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Maurice Bernard
 */
public class Player {
    private int stack = 0;
    private Hand hand = null;
    private int bet = 0;
    
    public void removeFromStack(int amount)
    {
        stack =- amount;
    }
    
    public void addToStack(int amount)
    {
        stack += amount;
    }

    /**
     * @return the stack
     */
    public int getStack() {
        return stack;
    }

    /**
     * @param stack the stack to set
     */
    public void setStack(int stack) {
        this.stack = stack;
    }

    /**
     * @return the hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * @param hand the hand to set
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * @return the bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * @param bet the bet to set
     */
    public void setBet(int bet) {
        this.bet = bet;
    }
    
}
