/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Maurice Bernard
 */
public class Table {
    private int pot = 0;
    private Player self = new Player();
    private Player opponent = new Player();
    private CommunityCards cCards = new CommunityCards();
    private int smallBlind = 0;
    private int bigBlind  = 0;
    private int amountToCall = 0;

    /**
     * @return the pot
     */
    public int getPot() {
        return pot;
    }

    /**
     * @param pot the pot to set
     */
    public void setPot(int pot) {
        this.pot = pot;
    }

    /**
     * @return the self
     */
    public Player getSelf() {
        return self;
    }

    /**
     * @param self the self to set
     */
    public void setSelf(Player self) {
        this.self = self;
    }

    /**
     * @return the opponent
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * @param opponent the opponent to set
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * @return the cCards
     */
    public CommunityCards getcCards() {
        return cCards;
    }

    /**
     * @param cCards the cCards to set
     */
    public void setcCards(CommunityCards cCards) {
        this.cCards = cCards;
    }

    /**
     * @return the smallBlind
     */
    public int getSmallBlind() {
        return smallBlind;
    }

    /**
     * @param smallBlind the smallBlind to set
     */
    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    /**
     * @return the bigBlind
     */
    public int getBigBlind() {
        return bigBlind;
    }

    /**
     * @param bigBlind the bigBlind to set
     */
    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    /**
     * @return the amountToCall
     */
    public int getAmountToCall() {
        return amountToCall;
    }

    /**
     * @param amountToCall the amountToCall to set
     */
    public void setAmountToCall(int amountToCall) {
        this.amountToCall = amountToCall;
    }
}
