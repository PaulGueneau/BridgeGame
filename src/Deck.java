package src;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// This class represents a standard deck of playing cards.
// It contains methods to shuffle the deck and deal cards.
// The deck is initialized with 52 cards (no jokers in Bridge), each of the four suits (hearts, diamonds, clubs, spades) and 13 ranks (2-10, J, Q, K, A).

public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }


    public Card dealCard() throws EmptyDeckException {
        if (cards.isEmpty()) {
            throw new EmptyDeckException("The deck is empty. No cards can be drawn.");
        }
        return cards.remove(cards.size() - 1);
    }

    public int getRemainingCards() {
        return cards.size();
    }
}
