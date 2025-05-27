package src;
import java.util.ArrayList;
import java.util.List;

// This class is responsible for managing the trick in a game of bridge.
// It keeps track of the cards played in the current trick and determines the winner of the trick.
// In bridge winner of tricks depends on the suit of the cards played and the rank of the cards. The card with the highest rank in the suit that was led wins the trick.
// If a trump suit is played, the highest card in the trump suit wins the trick.
// If no trump suit is played, the highest card in the suit that was led wins the trick. 
// The rank of the cards (for both trump and no trump) is as follows: 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A

public class Trick {
    private List<Card> cards;
    private Card.Suit trumpSuit;
    private Card.Suit ledSuit;
    private Player.Position leader;
    private Player.Position currentPlayer;
    
    public Trick() {
        this.cards = new ArrayList<>();
        this.trumpSuit = null;
        this.ledSuit = null;
        this.leader = null;
        this.currentPlayer = null;
    }
    
    public void setTrumpSuit(Card.Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }
    
    public void setLeader(Player.Position leader) {
        this.leader = leader;
        this.currentPlayer = leader;
    }
    
    public void addCard(Card card) {
        if (cards.isEmpty()) {
            ledSuit = card.getSuit();
        }
        cards.add(card);
        // Move to next player in clockwise order
        currentPlayer = getNextPlayer(currentPlayer);
    }
    
    public Player.Position getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isComplete() {
        return cards.size() == 4;
    }
    
    public Player.Position getWinner() {
        if (cards.size() != 4) {
            return null;
        }
        
        Card winningCard = cards.get(0);
        int winningIndex = 0;
        
        for (int i = 1; i < cards.size(); i++) {
            Card currentCard = cards.get(i);
            if (isCardHigher(currentCard, winningCard)) {
                winningCard = currentCard;
                winningIndex = i;
            }
        }
        
        // Calculate winner's position based on leader and winning card index
        Player.Position winner = leader;
        for (int i = 0; i < winningIndex; i++) {
            winner = getNextPlayer(winner);
        }
        
        return winner;
    }
    
    private boolean isCardHigher(Card card1, Card card2) {
        if (trumpSuit != null && card1.getSuit() == trumpSuit && card2.getSuit() != trumpSuit) {
            return true;
        }
    
        if (trumpSuit != null && card2.getSuit() == trumpSuit && card1.getSuit() != trumpSuit) {
            return false;
        }
        if (card1.getSuit() == card2.getSuit()) {
            return card1.getRank().ordinal() > card2.getRank().ordinal();
        }
        if (card1.getSuit() == ledSuit && card2.getSuit() != ledSuit) {
            return true;
        }
        if (card2.getSuit() == ledSuit && card1.getSuit() != ledSuit) {
            return false;
        }
        return false;
    }
    
    private Player.Position getNextPlayer(Player.Position current) {
        switch (current) {
            case NORTH:
                return Player.Position.EAST;
            case EAST:
                return Player.Position.SOUTH;
            case SOUTH:
                return Player.Position.WEST;
            case WEST:
                return Player.Position.NORTH;
            default:
                return null;
        }
    }
    
    public void clear() {
        cards.clear();
        ledSuit = null;
        leader = null;
        currentPlayer = null;
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public Card.Suit getLedSuit() {
        return ledSuit;
    }
}
