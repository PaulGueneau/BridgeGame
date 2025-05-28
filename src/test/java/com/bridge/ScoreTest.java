package com.bridge;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScoreTest {
    private Score score;
    private Bid partialBid;
    private Bid gameBid;
    private Bid littleSlamBid;
    private Bid grandSlamBid;

    @Before
    public void setUp() {
        score = new Score();

        partialBid = new Bid(Bid.Level.TWO, Card.Suit.CLUBS);  // 2♣
        gameBid = new Bid(Bid.Level.FOUR, Card.Suit.HEARTS);   // 4♥
        littleSlamBid = new Bid(Bid.Level.SIX, Card.Suit.SPADES); // 6♠
        grandSlamBid = new Bid(Bid.Level.SEVEN, Card.Suit.NT);    // 7NT
    }

    @Test
    public void testPartialBidScoring() {
        // Test a partial bid (2♣) with 8 tricks (made contract)
        score.setContract(partialBid);
        score.setVulnerable(false);
        
        // Set tricks won 
        for (int i = 0; i < 8; i++) {
            score.addTrick();
        }
        
        int expectedScore = (2 * Score.TRICK_POINTS_MINOR) + Score.PARTIAL_BONUS;
        assertEquals("Score for made partial bid should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testGameBidScoring() {
        // Test a game bid (4♥) with 10 tricks (made contract)
        score.setContract(gameBid);
        score.setVulnerable(false);
        
        // Set tricks won (4 + 6 = 10 tricks required, won 10)
        for (int i = 0; i < 10; i++) {
            score.addTrick();
        }
        
        int expectedScore = (4 * Score.TRICK_POINTS_MAJOR) + Score.GAME_BONUS;
        assertEquals("Score for made game bid should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testVulnerableGameBidScoring() {
        // Test a vulnerable game bid (4♥) with 10 tricks
        score.setContract(gameBid);
        score.setVulnerable(true);
        
        // Set tricks won (4 + 6 = 10 tricks required, won 10)
        for (int i = 0; i < 10; i++) {
            score.addTrick();
        }
        
        int expectedScore = (4 * Score.TRICK_POINTS_MAJOR) + Score.GAME_BONUS_VULNERABLE;
        assertEquals("Score for made vulnerable game bid should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testLittleSlamScoring() {
        // Test a little slam bid (6♠) with 12 tricks
        score.setContract(littleSlamBid);
        score.setVulnerable(false);
        
        // Set tricks won (6 + 6 = 12 tricks required, won 12)
        for (int i = 0; i < 12; i++) {
            score.addTrick();
        }
        
        int expectedScore = (6 * Score.TRICK_POINTS_MAJOR) + Score.LITTLE_SLAM_BONUS;
        assertEquals("Score for made little slam should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testGrandSlamScoring() {
        // Test a grand slam bid (7NT) with 13 tricks
        score.setContract(grandSlamBid);
        score.setVulnerable(false);
        
        // Set tricks won (7 + 6 = 13 tricks required, won 13)
        for (int i = 0; i < 13; i++) {
            score.addTrick();
        }
        
        int expectedScore = (7 * Score.TRICK_POINTS_NO_TRUMP) + Score.GRAND_SLAM_BONUS;
        assertEquals("Score for made grand slam should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testFailedContractScoring() {
        // Test a failed contract (2♣) with only 6 tricks
        score.setContract(partialBid);
        score.setVulnerable(false);
        
        // Set tricks won (2 + 6 = 8 tricks required, won 6)
        for (int i = 0; i < 6; i++) {
            score.addTrick();
        }
        
        // Should be penalized for 2 undertricks
        int expectedScore = -100; // 50 points per undertrick when not vulnerable
        assertEquals("Score for failed contract should be correct", 
                    expectedScore, score.calculateScore());
    }

    @Test
    public void testVulnerableFailedContractScoring() {
        // Test a failed vulnerable contract (2♣) with only 6 tricks
        score.setContract(partialBid);
        score.setVulnerable(true);
        
        // Set tricks won (2 + 6 = 8 tricks required, won 6)
        for (int i = 0; i < 6; i++) {
            score.addTrick();
        }
        
        // Should be penalized for 2 undertricks (100 points per undertrick when vulnerable)
        int expectedScore = -200;
        assertEquals("Score for failed vulnerable contract should be correct", 
                    expectedScore, score.calculateScore());
    }
}
