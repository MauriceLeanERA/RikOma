/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Maurice Bernard
 */
public class TacticalProcessor {
    private final Table table;
    private int bet;
    private HashMap<Integer, Card[]> cardsMap = new HashMap<>();
    private ArrayList<Card> straightStarter = new ArrayList<>();
    private boolean possibleFlush = false;
    private boolean possibleStreet = false;
    private boolean stop = false;
    private HandTimer timer;
    LoggerOutput logger = new LoggerOutput("tactic");
    
    //TODO: Unendliches raisen verhindern!!!
    //TODO: bei kleinen blinds oder bets zum mitgehen, einfach mitgehen und nicht folden
    public TacticalProcessor(Table table)
    {        
        this.table = table;       
    }
    
    public void start()
    {        
        timer = new HandTimer(this); 
        Thread t = new Thread(timer);
        t.start();
        if(!alreadyRaised())
        {
            prepareMap();
            evaluateCards();
        }
        if(!stop)
        {
            this.bet = evaluateBet();
        }
    }
    
    private void prepareMap()
    {
        for(int i = 1; i <= 14; i++)
        {
            cardsMap.put(i, new Card[]{null, null, null, null});
        }
        sortCards();
    }
    
    /**
     * Hier werden alle Suchen nach möglichen Händen durchlaufen
     */
    private void evaluateCards()
    {
        //Blatt                 MaxWert     Berechnung
        //----------------------------------------------
        //Paar:                 28          (Value*2)
        //2 Paar:               54          (Value1*2+Value2*2)
        //Three-Of-A-Kind:      97          (55+Value*3)
        //Straße:               158         (98+C1+C2+C3+C4+C5)                                           
        //Flush                 159         fix
        //Full House            228         (160+HighValue*3+LowValue*2)
        //Four of a Kind        285         (229+Value*4)
        //Straight Flush        346         (286+C1+C2+C3+C4+C5)
        //Royal Flush           347         fix
        
        //TODO: Eventuell auch bei möglicher Straße mitgehen
        
        //Test
        
        findPossibleFlush();
        
        if(!stop && getTable().getcCards().getFlop().isEmpty())
        {
            if(getTable().getSelf().getStack()*.015 >= getTable().getAmountToCall())
            {
                getTable().getSelf().getHand().setHandValue(-1);
            }
        }
        
        //Testweise rausgenommen, einfach bei preflop immer mitgehen solange der Einsatz unter 15% des Stacks liegt 
//        if(!stop && getTable().getcCards().getFlop().isEmpty())  
//        {
//            findPairsOnHand();
//            return;
//        }
        
        if(!stop && checkForRoyalFlush())
        {
            getTable().getSelf().getHand().setHandValue(347);
            return;
        }
        if(!stop && findStraight())
        {
            if(findFlush(straightStarter))
            {
                return;
            }           
        }
        if(!stop && findFourOfAKind())            
        {
            return;
        }        
        if(!stop && findFullHouse())
        {
            return;
        }
        if(!stop && findFlush())
        {
            return;
        }
        if(!stop && findStraight())
        {
            return;
        }
        if(!stop && findThreeOfAKind())
        {
            return;
        }
        if(!stop && findTwoPairs())
        {
            return;
        }
        if(!stop)
        {
            findPair();
        }
    }
    
    private boolean alreadyRaised()
    {
        if(InputParser.raises >= 1)
        {
            table.getSelf().getHand().setHandValue(6);
            return true;
        }
        return false;
    }
    
    /**
     * Sucht nach Paaren
     * @return 
     */
    private boolean findPair()
    {
        ArrayList<Integer> values = new ArrayList<>();
        for(Card[] cards : cardsMap.values())
        {
            int occurences = 0;
            int tmpVal = 0;
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null)
                {
                   tmpVal = cards[color].getVALUE();
                   occurences++;
                }
            }
            
            if(occurences == 2)
            {
                values.add(tmpVal*2);
            }
        }
        int value = 0;
        for(int val : values)
        {
            if(val > value)
            {
                value = val;
            }
        }
        
        if(value > 0)
        {
            getTable().getSelf().getHand().setHandValue(value);
            return true;
        }
        return false;
    }
    
    /**
     * Findet die zwei höchsten Pärchen.
     * @return 
     */
    private boolean findTwoPairs()
    {
        ArrayList<Card[]> pairs = new ArrayList<Card[]>();
        for(Card[] cards : cardsMap.values())
        {
            int occurences = 0;
            Card tmpCard1 = null;
            Card tmpCard2 = null;
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null)
                {
                    if(occurences == 0)
                    {
                        tmpCard1 = cards[color];
                        occurences++;
                    }
                    else
                    {
                        tmpCard2 = cards[color];
                        occurences++;
                    }
                }
            }
            if(occurences == 2)
            {
                pairs.add(new Card[]{tmpCard1, tmpCard2});
            }
        }
        //TODO: Das hier merken udn vielleicht in andere Methoden übertragen. es müssen eigentlich nur die errechneten Werte gespeichert
        //und dann der höchste ausgelesen werden. Das spart das zwischenspeichern der Karten
        ArrayList<Integer> values = new ArrayList<>();
        if(pairs.size() >= 2)
        {            
            for(int i = 0; i <= pairs.size()-2; i++)
            {
                int comCards = 0;
                int handCards = 0;
                if(pairs.get(i)[0].getOWNER() == CardOwnerEnum.Community)
                {
                    comCards++;
                }
                else {
                    handCards++;
                }
                if(pairs.get(i)[1].getOWNER() == CardOwnerEnum.Community)
                {
                    comCards++;
                }
                else {
                    handCards++;
                }
                for(int j = i+1; j <= pairs.size()-1; j++)
                {
                    int tmpComCards = 0;
                    int tmpHandCards = 0;
                    if(pairs.get(j)[0].getOWNER() == CardOwnerEnum.Community)
                    {
                        tmpComCards++;
                    }
                    else {
                        tmpHandCards++;
                    }
                    if(pairs.get(j)[1].getOWNER() == CardOwnerEnum.Community)
                    {
                        tmpComCards++;
                    }
                    else {
                        tmpHandCards++;
                    }
                    
                    if(tmpComCards+comCards <= 3 && tmpHandCards+handCards <=2)
                    {
                        values.add(pairs.get(j)[0].getVALUE()*2+pairs.get(i)[0].getVALUE()*2);
                    }
                }
            }
        }
        int value = 0;
        for(int val : values)
        {
            if(val > value)
            {
                value = val;
            }
        }
        if(value > 0)
        {
            getTable().getSelf().getHand().setHandValue(value);
            return true;
        }
        return false;
    }
    /**
     * Sucht nach einem Drilling.
     * @return 
     */
    private boolean findThreeOfAKind()
    {
        int val = 0;
        for(Card[] cards : cardsMap.values())
        {
            int cardsOnCom = 0;
            int cardsOnHand = 0;
            int tmpVal = 0;
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null)
                {
                    if(cards[color].getOWNER() == CardOwnerEnum.Community)
                    {
                        cardsOnCom++;
                    }
                    else if(cards[color].getOWNER() == CardOwnerEnum.Self)
                    {
                        cardsOnHand++;
                    }
                    tmpVal = cards[color].getVALUE();
                }
            }
            if(cardsOnCom+cardsOnHand >= 3 && cardsOnHand <= 2 && cardsOnCom >= 1 && cardsOnCom <= 3 && tmpVal > val)
            {
                val = tmpVal;
            }
        }
        
        if(val > 0)
        {
            getTable().getSelf().getHand().setHandValue(55+val*3);
            return true;
        }
        return false;
    }
    
    /**
     * Sucht nach einem FullHouse und speichert den Value.
     * @return 
     */
    private boolean findFullHouse()
    {
        ArrayList<Card[]> threeOfAKind = new ArrayList<>();
        ArrayList<Card[]> pairs = new ArrayList<>();
        for(Card[] cards : cardsMap.values())
        {
            int occurences = 0;
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null)
                {
                    occurences++;                
                }
            }
            if(occurences == 3)
            {
                threeOfAKind.add(cards);
            }
            else if(occurences == 2)
            {
                pairs.add(cards);
            }
        }
        
        int pairVal = 0;
        int threeVal = 0;
        for(Card[] pair : pairs)
        {
            int maxHandCards = 2;
            int maxComCards = 3;
            int tmpValPair = 0;
            int tmpValThree = 0;
            int tmpMaxHandCards = 0;
            int tmpMaxComCards = 0;
            for(Card pairCard : pair)
            {
                if(pairCard != null)
                {
                    if(pairCard.getOWNER() == CardOwnerEnum.Community)
                    {
                        maxComCards--;
                    }
                    else
                    {
                        maxHandCards--;
                    }
                    tmpValPair = pairCard.getVALUE();
                }
            }
            tmpMaxComCards = maxComCards;
            tmpMaxHandCards = maxHandCards;
            for(Card[] three : threeOfAKind)
            {
                for(Card card : three)
                {
                    if(card != null)
                    {
                        if(card.getOWNER() == CardOwnerEnum.Community)
                        {
                            maxComCards--;
                        }
                        else
                        {
                            maxHandCards--;
                        }
                        tmpValThree = card.getVALUE();
                    }
                    
                    if(maxHandCards < 0 || maxComCards < 0)
                    {
                        break;
                    }
                }
                if(maxComCards == 0 && maxHandCards == 0)
                {
                    if(tmpValPair > pairVal)
                    {
                        pairVal = tmpValPair;
                    }
                    if(tmpValThree > threeVal)
                    {
                        threeVal = tmpValThree;
                    }
                }
                maxComCards = tmpMaxComCards;
                maxHandCards = tmpMaxHandCards;
            }
        }
        if(pairVal > 0 && threeVal > 0)
        {
            getTable().getSelf().getHand().setHandValue(160+threeVal*3+pairVal*2);
            return true;
        }
        return false;
    }
    
    /**
     * Sucht nach 4 gleichen.
     * @return 
     */
    private boolean findFourOfAKind()
    {
        int val = 0;
        for(Card[] cards : cardsMap.values())
        {
            int newVal = 229;
            int cardsOnHand = 0;
            int cardsOnCom = 0;
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null )
                {
                    if(cards[color].getOWNER() == CardOwnerEnum.Community)
                    {
                        cardsOnCom++;
                    }
                    else
                    {
                        cardsOnHand++;
                    }
                }
            }
            
            if(cardsOnHand+cardsOnCom == 4 && cardsOnHand >= 1 && cardsOnHand <=2 && cardsOnCom >= 2 && cardsOnCom <= 3)
            {
                newVal += cards[0].getVALUE()*4;
                if(newVal > val)
                {
                    val = newVal;
                }
            }
        }
        if(val > 229)
        {
            getTable().getSelf().getHand().setHandValue(val);
            return true;
        }
        return false;
    }
    
    /**
     * Sucht nach einem Flush in dem angegebenen Array von Straßen-Startkarten (ermittelt durch findStraight()).
     * @param straight
     * @return 
     */
    private boolean findFlush(ArrayList<Card> straight)
    {                
        for(Card card : straight)
        {            
            int color = 0;
            for(color = 0; color <= 3; color++)
            {
                int handVal = 286;
                int cardsOnHand = 0;
                int cardsOnCom = 0;
                boolean found = true;
                for(int i = 0; i <= 4; i++) //Iteration über Kartenwerte, startVal+1...4 für alle Werte einer Straße
                {
                    if(cardsMap.get(card.getVALUE()+i)[color] == null)
                    {
                        found = false;                        
                        break;
                    }
                    handVal += cardsMap.get(card.getVALUE()+i)[color].getVALUE();
                    if(cardsMap.get(card.getVALUE()+i)[color].getOWNER() == CardOwnerEnum.Community)
                    {
                        cardsOnCom++;
                    }
                    else
                    {
                        cardsOnHand++;
                    }
                }
                if(found == true && cardsOnHand == 2 && cardsOnCom == 3)
                {
                    getTable().getSelf().getHand().setHandValue(handVal);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Sucht nach einem Flush.
     * @return 
     */
    private boolean findFlush()
    {
        //herz = 0; pik = 1; kreuz = 2; karo = 3
        ArrayList<int[]> flushList = new ArrayList<>();
        for(int i = 0; i <= 3; i++)
        {
            int[] c = {0, 0};   //index 0 = handCards; index 1 = comCards
            flushList.add(c);
        }
        
        for(Card[] cards : cardsMap.values())
        {
            for(Card card : cards)
            {
                if(card != null && card.getVALUE() != 1)
                {
                    if(card.getOWNER() == CardOwnerEnum.Community)
                    {
                        flushList.get(card.getCOLOR().getCode())[1]++;
                    }
                    else if(card.getOWNER() == CardOwnerEnum.Self)
                    {
                        flushList.get(card.getCOLOR().getCode())[0]++;
                    }
                }
            }
        }
        
        for(int[] colors : flushList)
        {
            if(colors[0]+colors[1] >= 5 && colors[0] <= 2 && colors[1] <= 3)
            {
                getTable().getSelf().getHand().setHandValue(159);
                return true;
            }
        }
        return false;       
    }
    
    /**
     * Füllt cardsMap mit allen CommunityCards und eigenen Karten.
     */
    private void sortCards()
    {
        ArrayList<Card> allCards = getTable().getcCards().getAllCards();
        allCards.addAll(getTable().getSelf().getHand().getCards());
        for(Card card : allCards)
        {
            Card[] cards = cardsMap.get(card.getVALUE());            
//            logger.write("Size of array: "+cards.length);                    
//            logger.write("Color-code: "+card.getCOLOR().getCode());
            cards[card.getCOLOR().getCode()] = card;
            cardsMap.put(card.getVALUE(), cards);
            //cardsMap.get(card.getVALUE())[card.getCOLOR().getCode()] = card;
        }
    }
    
    /**
     * Sucht nach einer Straße, oder einem Straight Flush
     * @return 
     */
    //TODO: Hier ein bisschen überarbeiten, da immer nur nach einer Karte mit entsprechendem Wert gesucht wird, aber es möglicherweise mehrere geben kann, diese aber nicht
    //bei der überprüfung auf anzahl der community und hand cards einfließen ...Vielleicht behoben
    private boolean findStraight()
    {       
        straightStarter = new ArrayList<>();
        for(int val = 1; val <= 10; val++)
        {
            boolean cardIsStarter = true;
            if(getCardOfValue(val) != null)
            {
                for(int nextCardVal = val+1; nextCardVal <= val+4; nextCardVal++)
                {
                    if(getCardOfValue(nextCardVal) == null)
                    {
                        cardIsStarter = false;
                        break;
                    }
                }
            }
            else
            {
                continue;
            }

            if(cardIsStarter)
            {
                straightStarter.add(getCardOfValue(val));
            }                            
        }      
        
        ArrayList<Card> recheckedStraightStarter = new ArrayList<>();
        for(int starterIndex = straightStarter.size()-1; starterIndex >= 0; starterIndex--)
        {
            int cardsOnHand = 0;
            int cardsOnCom = 0;
            int val = 98;
            for(int i = 0; i <= 4; i++)
            {
                for(Card card : getCardsOfValue(straightStarter.get(starterIndex).getVALUE()+i))
                {
                    if(card.getOWNER() == CardOwnerEnum.Community)
                    {
                        cardsOnCom++;
                    }
                    else
                    {
                        cardsOnHand++;
                    }
                }                
                val += straightStarter.get(starterIndex).getVALUE()+i;
            }
            if(cardsOnHand >= 2 && cardsOnCom >= 3)
            {
                this.getTable().getSelf().getHand().setHandValue(val);
                recheckedStraightStarter.add(straightStarter.get(starterIndex));
                straightStarter = recheckedStraightStarter;
                return true;
            }         
            
        }
        straightStarter.clear();
        return false;
    }
    
    
    
    /**
     * Gibt eine Karte aus der cardsMap mit entsprechendem Wert, falls vorhanden.
     * Wenn mehrere vorhanden sind, wird die letzte (siehe ColorEnum) ausgegeben.
     * @param val
     * @return 
     */
    private Card getCardOfValue(int val)
    {
        Card card = null;        
        for(int i = 0; i <= 3; i++)
        {
            if(cardsMap.get(val)[i] != null)
            {
                card = cardsMap.get(val)[i];
            }
        }
        return card;
    }
    
    /**
     * Liefert eine unsortierte ArrayList<Card> mit allen vorhandenen Karten des angegeben Wertes zurück.
     * @param val
     * @return 
     */
    private ArrayList<Card> getCardsOfValue(int val)
    {
        ArrayList<Card> cardList = new ArrayList<>();
        for(int i = 0; i <= 3; i++)
        {
            if(cardsMap.get(val)[i] != null)
            {
                cardList.add(cardsMap.get(val)[i]);
            }
        }
        return cardList;
    }
    
    /**
     * Sucht nach nem RoyalFlush, beinhaltet im prinzip auch schon suche nach ner Straße oder Flush.
     * @return 
     */
    private boolean checkForRoyalFlush()
    {
        //herz = 0; pik = 1; kreuz = 2; karo = 3
        boolean found = false;    
        for(int i = 0; i < 4; i++)
        {
            if(cardsMap.get(10)[i] != null && cardsMap.get(11)[i] != null && cardsMap.get(12)[i] != null && cardsMap.get(13)[i] != null && cardsMap.get(14)[i] != null)
            {
                Card[] royalFlush = {cardsMap.get(10)[i], cardsMap.get(11)[i], cardsMap.get(12)[i], cardsMap.get(13)[i], cardsMap.get(14)[i]};
                int comCards = 0;
                int playerCards = 0;
                for(Card card : royalFlush)
                {
                    switch(card.getOWNER())
                    {
                        case Community:
                            comCards++;
                            break;
                        case Self:
                            playerCards++;
                            break;
                    }
                }
                
                if(comCards == 3 && playerCards == 2)
                {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
    
    /**
     * herz = 0; pik = 1; kreuz = 2; karo = 3
     * @param arr
     * @param card 
     */
    private Card[] addToArray(Card[] arr, Card card)
    {
        arr[card.getCOLOR().getCode()] = card;        
        return arr;
    }
    
    /**
     * Paare in der eigenen Hand finden und die Stärke daraus berechnen
     */
    private void findPairsOnHand()
    {       
        int valueFound = 0;
        for(int val = 1; val <= 14; val++)
        {
            int occurences = 0;
            for(int color = 0; color <= 3; color++)
            {
                if(cardsMap.get(val)[color] != null)
                {
                    occurences++;
                    if(occurences == 2)
                    {
                        valueFound = val;
                    }
                }
            }
        }
        getTable().getSelf().getHand().setHandValue(valueFound*2);
    }
    
    private void findPossibleFlush()
    {
        //herz = 0; pik = 1; kreuz = 2; karo = 3
        int[] colors = {0, 0, 0, 0};
        for(Card[] cards : cardsMap.values())
        {
            for(int color = 0; color <= 3; color++)
            {
                if(cards[color] != null && cards[color].getVALUE() != 1)
                {
                    colors[color]++;
                }
            }
        }
        
        if(getTable().getcCards().getFlop().isEmpty())
        {
            for(int c : colors)
            {
                if(c >= 2)
                {
                    possibleFlush = true;
                }
            }
        }
        else if(!table.getcCards().getFlop().isEmpty() && getTable().getcCards().getTurn().isEmpty())
        {
            for(int c : colors)
            {
                if(c >= 4)
                {
                    possibleFlush = true;
                }
            }
        }            
    }
    
    /**
     * Die Bet anhand der Stärke der eigenen Hand ermitten
     * @return 
     */
    private int evaluateBet()
    {
        //Blatt                 MaxWert     Berechnung
        //----------------------------------------------
        //Paar:                 28          (Value*2)
        //2 Paar:               54          (Value1*2+Value2*2)
        //Three-Of-A-Kind:      97          (55+Value*3)
        //Straße:               158         (98+C1+C2+C3+C4+C5)                                           
        //Flush                 159         fix
        //Full House            228         (160+HighValue*3+LowValue*2)
        //Four of a Kind        285         (229+Value*4)
        //Straight Flush        346         (286+C1+C2+C3+C4+C5)
        //Royal Flush           347         fix
        
        int bet = 0;
        int multiplier = 1;
        int handVal = getTable().getSelf().getHand().getHandValue();
        if(handVal >= 6 && handVal <= 28)
        {
            if(table.getBigBlind()*10 <= table.getSelf().getStack())
            {
               multiplier = 3; 
            }
            bet = getTable().getBigBlind()*multiplier;
        }
        else if(handVal >= 19 && handVal <= 97)
        {
            bet = getTable().getBigBlind()*2;
        }
        else if(handVal >= 98 && handVal <= 159)  //bei Straße und Flush immer mitgehen/erhöhen
        {
            bet = getTable().getAmountToCall();
            if(bet < getTable().getBigBlind() *3)
            {
                bet = getTable().getBigBlind()*3;
            }
        }  
        else if(handVal >= 160 && handVal <= 285) //bei Full House und Vierlingen immer mitgehen/erhöhen
        {
            bet = getTable().getAmountToCall();
            if(bet < getTable().getBigBlind() *handVal/40)
            {
                bet = getTable().getBigBlind()*handVal/40;
            }
        }
        else if(handVal >= 286)
        {
            bet = (int)(getTable().getSelf().getStack()*0.7);
        }
        else if(handVal == -1)  //BetToCall beträgt weniger oder genau 15% des eigenen Stacks, also mitgehen
        {
            bet = getTable().getAmountToCall();
        }
        
        if(isPossibleFlush() && bet == 0)
        {            
            bet = getTable().getBigBlind();
            logger.write("Possible Flush detected! Setting bet to: "+bet);
        }        
        
        return recalculateBet(bet);
    }
    
    /**
     * Bet neu berechnen, kann ja sein das nicht mehr genug Chips auf dem eigenen Stack sind.
     * @param bet 
     */
    private int recalculateBet(int bet)
    {
        if(getTable().getSelf().getStack() < bet)
        {
            bet = getTable().getSelf().getStack();
        }
        return bet;
    }
    
    /**
     * Action-String ausgeben, je nachdem was die Bet und AmountToCall ist
     * @return 
     */
    private String prepareActionString()
    {
        String action = "check 0";
        if(bet > getTable().getAmountToCall())
        {
            action = "raise "+bet;
            InputParser.raises++;
        }
        else if(bet < getTable().getAmountToCall() && getTable().getAmountToCall() > 0)
        {
            action = "fold 0";
        }
        else if(bet == getTable().getAmountToCall())
        {
            action = "call 0";
        }
        return action;
    }
    
    public String returnAction()
    {
        //TODO: schauen warum Timer null sein kann
        if(timer != null)
        {            
            timer.stop();
        }
        return prepareActionString();
    }   

    /**
     * @param bet the bet to set
     */
    public void setBet(int bet) {
        this.bet = bet;
        stop = true;
    }

    /**
     * @return the table
     */
    public Table getTable() {
        return table;
    }

    /**
     * @return the possibleFlush
     */
    public boolean isPossibleFlush() {
        return possibleFlush;
    }
    
}
