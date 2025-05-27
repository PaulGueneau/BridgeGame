package src;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card playCard(Card.Suit ledSuit) {
        // For now, just play the first card that follows suit if possible
        // TO DO: more sophisticated card selection
        for (Card card : cards) {
            if (ledSuit == null || card.getSuit() == ledSuit) {
                cards.remove(card);
                return card;
            }
        }
        // If no card follows suit, play the first card (you are not forced to use a trump card in bridge)
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
