/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maurice Bernard
 */
public class TacticalProcessorTest {
    
    public TacticalProcessorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class TacticalProcessor.
     */
//    @Test
//    public void testStart() {
//        System.out.println("start");
//        TacticalProcessor instance = null;
//        instance.start();
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of returnAction method, of class TacticalProcessor.
     */
//    @Test
//    public void testReturnAction() {
//        System.out.println("returnAction");
//        TacticalProcessor instance = null;
//        String expResult = "";
//        String result = instance.returnAction();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of setBet method, of class TacticalProcessor.
     */
    @Test
    public void testSetBet() {
        System.out.println("setBet");
        int bet = 0;
        TacticalProcessor instance = new TacticalProcessor(new Table());
        //instance.setBet(bet);
    }
    
    /**
     * Test of testEvaluateCards method, of class TacticalProcessor.
     */
    @Test
    public void testEvaluateCards()
    {        
        final int EXPRESULT = 22;
        
        InputParser.raises = 0;
        
        System.out.println("evaluateCards");
        ArrayList<Card> handCards = new ArrayList<>();
        handCards.add(new Card(CardColorEnum.Clubs, 11, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Hearts, 11, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Hearts, 9, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Diamonds, 2, CardOwnerEnum.Self));
        
        
        ArrayList<Card> flop = new ArrayList<>();
//        flop.add(new Card(CardColorEnum.Diamonds, 14, CardOwnerEnum.Community));
//        flop.add(new Card(CardColorEnum.Clubs, 6, CardOwnerEnum.Community));
//        flop.add(new Card(CardColorEnum.Clubs, 11, CardOwnerEnum.Community));
//        flop.add(new Card(CardColorEnum.Diamonds, 1, CardOwnerEnum.Community));
        
        ArrayList<Card> turn = new ArrayList<>();
//        turn.add(new Card(CardColorEnum.Clubs, 2, CardOwnerEnum.Community));
        
        ArrayList<Card> river = new ArrayList<>();
//        river.add(new Card(CardColorEnum.Diamonds, 9, CardOwnerEnum.Community));
//        river.add(new Card(CardColorEnum.Hearts, 14, CardOwnerEnum.Community));
        
        CommunityCards cCards = new CommunityCards();
        cCards.setFlop(flop);
        cCards.setTurn(turn);
        cCards.setRiver(river);
        
        Hand hand = new Hand(handCards);
        Table table = new Table();
        Player self = new Player();
        self.setHand(hand);
        table.setSelf(self);
        table.setcCards(cCards);
        long startTime = new Date().getTime();
        TacticalProcessor instance = new TacticalProcessor(table);    
        instance.start();
        System.out.println("Taken time: "+(new Date().getTime()-startTime));
        assertEquals(EXPRESULT, table.getSelf().getHand().getHandValue());
    }
    
    /**
     * Test of isPossibleFlush method, of class TacticalProcessor.
     */
    @Test
    public void isPossibleFlush()
    {
        final String EXPRESULT = "raise 100";
        System.out.println("evaluateCards");
        ArrayList<Card> handCards = new ArrayList<>();
        handCards.add(new Card(CardColorEnum.Diamonds, 9, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Hearts, 11, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Spades, 7, CardOwnerEnum.Self));
        handCards.add(new Card(CardColorEnum.Diamonds, 11, CardOwnerEnum.Self));
        
        CommunityCards cCards = new CommunityCards();
        
        Hand hand = new Hand(handCards);
        Table table = new Table();
        Player self = new Player();
        self.setHand(hand);
        self.setStack(10000);
        table.setSelf(self);
        table.setcCards(cCards);
        table.setBigBlind(100);
        TacticalProcessor instance = new TacticalProcessor(table);    
        //instance.start();
        //assertEquals(EXPRESULT, instance.returnAction());
    }
    
}
