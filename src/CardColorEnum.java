/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maurice Bernard
 */
public enum CardColorEnum {
    None(-1),       //If Error occurs
    Hearts(0),     //Herz
    Spades(1),     //Pik
    Clubs(2),      //Kreuz
    Diamonds(3);   //Karo    
    
    private int code;
    private static final Map<Integer, CardColorEnum> lookup = new HashMap<Integer, CardColorEnum>();

    static
    {
        for (CardColorEnum c : EnumSet.allOf(CardColorEnum.class))
        {
            lookup.put(c.getCode(), c);
        }
    }

    private CardColorEnum(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public static CardColorEnum get(int code)
    {
        return lookup.get(code);
    }
}
