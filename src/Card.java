/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Maurice Bernard
 */
public class Card {
    private final CardColorEnum COLOR;
    private final int VALUE;
    private final CardOwnerEnum OWNER;
    
    public Card(CardColorEnum color, int value, CardOwnerEnum owner)
    {
        this.COLOR = color;
        this.VALUE = value;
        this.OWNER = owner;
    }

    /**
     * @return the COLOR
     */
    public CardColorEnum getCOLOR() {
        return COLOR;
    }

    /**
     * @return the VALUE
     */
    public int getVALUE() {
        return VALUE;
    }

    /**
     * @return the owner
     */
    public CardOwnerEnum getOWNER() {
        return OWNER;
    }
}
