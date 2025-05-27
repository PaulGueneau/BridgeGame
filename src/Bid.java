package src;

public class Bid {
    public enum Level {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);
        private final int value;
        
        Level(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    private Level level;
    private Card.Suit suit;
    private boolean isPass;
    private boolean isDouble;
    private boolean isRedouble;
    
    public Bid() {
        this.isPass = true;
        this.isDouble = false;
        this.isRedouble = false;
    }
    
    public Bid(Level level, Card.Suit suit) {
        this.level = level;
        this.suit = suit;
        this.isPass = false;
        this.isDouble = false;
        this.isRedouble = false;
    }
    
    public static Bid pass() {
        return new Bid();
    }
    
    public static Bid doubleBid() {
        Bid bid = new Bid();
        bid.isPass = false;
        bid.isDouble = true;
        return bid;
    }
    
    public static Bid redouble() {
        Bid bid = new Bid();
        bid.isPass = false;
        bid.isRedouble = true;
        return bid;
    }
    
    public boolean isPass() {
        return isPass;
    }
    
    public boolean isDouble() {
        return isDouble;
    }
    
    public boolean isRedouble() {
        return isRedouble;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public Card.Suit getSuit() {
        return suit;
    }
    
    public boolean isHigherThan(Bid other) {
        if (this.isPass || this.isDouble || this.isRedouble) {
            return false;
        }
        if (other.isPass || other.isDouble || other.isRedouble) {
            return true;
        }
        
        if (this.level.getValue() > other.level.getValue()) {
            return true;
        }
        if (this.level.getValue() < other.level.getValue()) {
            return false;
        }
        
        // If levels are equal, compare suits
        return this.suit.ordinal() > other.suit.ordinal();
    }
    
    @Override
    public String toString() {
        if (isPass) return "PASS";
        if (isDouble) return "X";
        if (isRedouble) return "XX";
        return level.getValue() + " " + suit;
    }
}
