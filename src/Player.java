package src;
import java.util.Scanner;

public class Player {

    public enum Position {
        NORTH, SOUTH, WEST, EAST
    };

    private Position position;
    private String name;
    private int score;
    private Hand hand;
    private Bid bid;
    private Trick trick;
    private Scanner scanner;

    public Player(String name, Position position) {
        this.name = name;
        this.position = position;
        this.score = 0;
        this.hand = new Hand();
        this.bid = new Bid();
        this.trick = new Trick();
        this.scanner = new Scanner(System.in);
    }

    public String getName() {
        return name;
    }
    
    public Position getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Hand getHand() {
        return hand;
    }

    public Bid getBid() {
        System.out.println("\n" + name + "'s turn to bid");
        System.out.println("Your hand: " + hand);
        System.out.println("Enter your bid (1-7 for level, then suit: C/D/H/S/NT, or P for pass, X for double, XX for redouble):");
        
        String input = scanner.nextLine().toUpperCase();
        
        if (input.equals("P")) {
            return Bid.pass();
        } else if (input.equals("X")) {
            return Bid.doubleBid();
        } else if (input.equals("XX")) {
            return Bid.redouble();
        }
        
        try {
            String[] parts = input.split(" ");
            int level = Integer.parseInt(parts[0]);
            String suitStr = parts[1];
            
            if (level < 1 || level > 7) {
                System.out.println("Invalid level. Please bid again.");
                return getBid();
            }
            
            Card.Suit suit;
            switch (suitStr) {
                case "C": suit = Card.Suit.CLUBS; break;
                case "D": suit = Card.Suit.DIAMONDS; break;
                case "H": suit = Card.Suit.HEARTS; break;
                case "S": suit = Card.Suit.SPADES; break;
                case "NT": suit = Card.Suit.NT; break;
                default:
                    System.out.println("Invalid suit. Please bid again.");
                    return getBid();
            }
            
            return new Bid(Bid.Level.values()[level - 1], suit);
        } catch (Exception e) {
            System.out.println("Invalid bid format. Please bid again.");
            return getBid();
        }
    }

    public Card playCard(Card.Suit ledSuit) {
        System.out.println("\n" + name + "'s turn to play");
        System.out.println("Your hand: " + hand);
        if (ledSuit != null) {
            System.out.println("Led suit: " + ledSuit);
        }
        
        System.out.println("Enter the card to play (e.g., 'AH' for Ace of Hearts):");
        String input = scanner.nextLine().toUpperCase();
        
        try {
            String rankStr = input.substring(0, input.length() - 1);
            String suitStr = input.substring(input.length() - 1);
            
            Card.Rank rank;
            switch (rankStr) {
                case "A": rank = Card.Rank.ACE; break;
                case "K": rank = Card.Rank.KING; break;
                case "Q": rank = Card.Rank.QUEEN; break;
                case "J": rank = Card.Rank.JACK; break;
                case "10": rank = Card.Rank.TEN; break;
                case "9": rank = Card.Rank.NINE; break;
                case "8": rank = Card.Rank.EIGHT; break;
                case "7": rank = Card.Rank.SEVEN; break;
                case "6": rank = Card.Rank.SIX; break;
                case "5": rank = Card.Rank.FIVE; break;
                case "4": rank = Card.Rank.FOUR; break;
                case "3": rank = Card.Rank.THREE; break;
                case "2": rank = Card.Rank.TWO; break;
                default:
                    System.out.println("Invalid rank. Please try again.");
                    return playCard(ledSuit);
            }
            
            Card.Suit suit;
            switch (suitStr) {
                case "C": suit = Card.Suit.CLUBS; break;
                case "D": suit = Card.Suit.DIAMONDS; break;
                case "H": suit = Card.Suit.HEARTS; break;
                case "S": suit = Card.Suit.SPADES; break;
                default:
                    System.out.println("Invalid suit. Please try again.");
                    return playCard(ledSuit);
            }
            
            Card card = new Card(suit, rank);
            // TODO: Validate that the card is in the player's hand and follows suit if possible
            return card;
        } catch (Exception e) {
            System.out.println("Invalid card format. Please try again.");
            return playCard(ledSuit);
        }
    }

    public Trick getTrick() {
        return trick;
    }

    public void addCardToHand(Card card) {
        hand.addCard(card);
    }

    public void showHand() {
        System.out.println(name + "'s hand: " + hand);
    }

}
