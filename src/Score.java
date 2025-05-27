package src;

// This class is responsible for managing the scoring logic in a game of bridge.

// It keeps track of the points and tricks won by each team.
// In bridge, the scoring is based on the number of tricks won and the level of the bid.
// The scoring system is complex and varies based on the type of bid (game, slam, partial, grand slam).
// The scoring also depends on the trump suit, the cards played in the tricks and the vulnerability of the players. There are 
// also variations depending on the type of game being played (rubber, duplicate, etc.). Here we will only take into account the basic scoring. 

public class Score {
    final static int PARTIAL_BONUS = 50;
    final static int GAME_BONUS = 300;
    final static int GAME_BONUS_VULNERABLE = 500;
    final static int LITTLE_SLAM_BONUS = 500;
    final static int LITTLE_SLAM_BONUS_VULNERABLE = 750;
    final static int GRAND_SLAM_BONUS = 1000;
    final static int GRAND_SLAM_BONUS_VULNERABLE = 1500;
    final static int TRICK_POINTS_MINOR = 20;
    final static int TRICK_POINTS_MAJOR = 30;
    final static int TRICK_POINTS_NO_TRUMP = 40;
    
    private int points;
    private int tricks;
    private boolean isVulnerable;
    private Bid contract;
    private int tricksRequired;
    private int tricksWon;
    
    public Score() {
        this.points = 0;
        this.tricks = 0;
        this.isVulnerable = false;
        this.tricksWon = 0;
    }
    
    public void setContract(Bid contract) {
        this.contract = contract;
        this.tricksRequired = contract.getLevel().getValue() + 6; // Base tricks required
    }
    
    public void setVulnerable(boolean vulnerable) {
        this.isVulnerable = vulnerable;
    }
    
    public void addTrick() {
        this.tricksWon++;
    }
    
    public int calculateScore() { // replace with a compute object CalculateScore? 
        if (contract == null) {
            return 0;
        }
        
        int score = 0;
        int tricksOver = tricksWon - tricksRequired;
        
        // Contract is made, calculate score
        if (tricksOver >= 0) {
            int trickPoints = 0;
            if (contract.getSuit() == Card.Suit.NT) {
                trickPoints = TRICK_POINTS_NO_TRUMP;
            } else if (contract.getSuit() == Card.Suit.HEARTS || contract.getSuit() == Card.Suit.SPADES) {
                trickPoints = TRICK_POINTS_MAJOR;
            } else {
                trickPoints = TRICK_POINTS_MINOR;
            }
            
            score += contract.getLevel().getValue() * trickPoints;
            
            // Add bonuses
            if (contract.getLevel().getValue() >= 7) {
                // Grand slam bonus
                score += isVulnerable ? GRAND_SLAM_BONUS_VULNERABLE : GRAND_SLAM_BONUS;
            } else if (contract.getLevel().getValue() >= 6) {
                // Little slam bonus
                score += isVulnerable ? LITTLE_SLAM_BONUS_VULNERABLE : LITTLE_SLAM_BONUS;
            } else if (score >= 100) {
                // Game bonus 
                score += isVulnerable ? GAME_BONUS_VULNERABLE : GAME_BONUS;
            } else {
                // Partial
                score += PARTIAL_BONUS;
            }
            
            // Add overtrick points
            if (tricksOver > 0) {
                int overtrickPoints = trickPoints;
                if (contract.isDouble()) {
                    overtrickPoints = isVulnerable ? 200 : 100;
                } else if (contract.isRedouble()) {
                    overtrickPoints = isVulnerable ? 400 : 200;
                }
                score += tricksOver * overtrickPoints;
            }
        } else {
            // Failed the contract (messy logic, to be improved)
            int undertricks = -tricksOver;
            int penaltyPoints;
            if (contract.isRedouble()) {
                penaltyPoints = isVulnerable ? 
                    (undertricks == 1 ? 400 : undertricks == 2 ? 600 : 800 + (undertricks - 3) * 400) :
                    (undertricks == 1 ? 200 : undertricks == 2 ? 400 : 600 + (undertricks - 3) * 200);
            } else if (contract.isDouble()) {
                penaltyPoints = isVulnerable ?
                    (undertricks == 1 ? 200 : undertricks == 2 ? 500 : 800 + (undertricks - 3) * 300) :
                    (undertricks == 1 ? 100 : undertricks == 2 ? 300 : 500 + (undertricks - 3) * 300);
            } else {
                penaltyPoints = isVulnerable ?
                    (undertricks == 1 ? 100 : undertricks == 2 ? 200 : 300 + (undertricks - 3) * 300) :
                    (undertricks == 1 ? 50 : undertricks == 2 ? 100 : 150 + (undertricks - 3) * 100);
            }
            score = -penaltyPoints;
        }
        
        return score;
    }
    
    
    public void addPoints(int points) {
        this.points += points;
    }
}
