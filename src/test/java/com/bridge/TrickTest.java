package com.bridge;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TrickTest {
    private Trick trick;

    @Before
    public void setUp() {
        trick = new Trick();
    }

    @Test
    public void testNoTrumpTrick() {
        // Set up the trick
        trick.setLeader(Player.Position.NORTH);
        
        // North leads with Ace of Hearts
        Card aceHearts = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        trick.addCard(aceHearts);
        
        // East plays King of Hearts
        Card kingHearts = new Card(Card.Suit.HEARTS, Card.Rank.KING);
        trick.addCard(kingHearts);
        
        // South plays Queen of Hearts
        Card queenHearts = new Card(Card.Suit.HEARTS, Card.Rank.QUEEN);
        trick.addCard(queenHearts);
        
        // West plays Jack of Hearts
        Card jackHearts = new Card(Card.Suit.HEARTS, Card.Rank.JACK);
        trick.addCard(jackHearts);
        
        // Verify the trick is complete
        assertTrue("Trick should be complete", trick.isComplete());
        
        // Verify the led suit is Hearts
        assertEquals("Led suit should be Hearts", Card.Suit.HEARTS, trick.getLedSuit());
        
        // Verify North (leader) wins with Ace of Hearts
        assertEquals("North should win with Ace of Hearts", 
                    Player.Position.NORTH, trick.getWinner());
    }

    @Test
    public void testTrumpTrick() {
        trick.setLeader(Player.Position.NORTH);
        trick.setTrumpSuit(Card.Suit.DIAMONDS);

        // North leads with King of Spades
        Card kingSpades = new Card(Card.Suit.SPADES, Card.Rank.KING);
        trick.addCard(kingSpades);

        // East plays Ace of Spades
        Card aceSpades = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        trick.addCard(aceSpades);

        // South plays Ten of Diamonds (trump)
        Card trumpCard = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);
        trick.addCard(trumpCard);

        // West plays Queen of Diamonds (trump)
        Card queenHearts = new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN);
        trick.addCard(queenHearts);

        // Verify the trick is complete
        assertTrue("Trick should be complete", trick.isComplete());

        // Verify the led suit is Spades
        assertEquals("Led suit should be Spades", Card.Suit.SPADES, trick.getLedSuit());
  
        assertEquals("West should win with Queen of Diamonds", 
                    Player.Position.WEST, trick.getWinner());

    }


}