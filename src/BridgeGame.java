package src;

public class BridgeGame {
    private static final int MAX_PLAYERS = 4; // Number of players required in a bridge game
    private static final int CARDS_PER_PLAYER = 13;
    private Player[] players;
    private Card.Suit trumpSuit;
    private Player.Position currentPlayer;
    private Trick currentTrick;
    private int tricksPlayed;
    private Score northSouthScore;
    private Score eastWestScore;
    private Bid currentContract;
    private Player.Position declarer;

    public static void main(String[] args) {
        BridgeGame game = new BridgeGame();
        Player[] players = { 
            new Player("Player 1", Player.Position.NORTH),
            new Player("Player 2", Player.Position.SOUTH),
            new Player("Player 3", Player.Position.EAST),
            new Player("Player 4", Player.Position.WEST)
        };
        game.startGame(players);
        // Bidding phase
        game.bid(); 
        // Playing phase
        game.playHand();
        // Scoring phase
        game.score();
        // End of game (after 18 deals but it depends on the kind of bridge game you are playing)
        // game.displayResults();
    }
    
    public BridgeGame() {
        this.northSouthScore = new Score();
        this.eastWestScore = new Score();
    }
    
    private void startGame(Player[] players) {
        this.players = players;
        this.tricksPlayed = 0;
        Deck deck = new Deck();
        deck.shuffle();

        // Dealing phase, each player gets 13 cards 
        try {
            for (int i = 0; i < MAX_PLAYERS; i++) {
                for (int j = 0; j < CARDS_PER_PLAYER; j++) { 
                    players[i].addCardToHand(deck.dealCard());
                }
            }
        } catch (EmptyDeckException e) {
            System.err.println("Error dealing cards: " + e.getMessage());
            return;
        }

        for (Player player : players) {
            player.showHand();
        }

        currentPlayer = Player.Position.NORTH; // This should be determined by bidding
    }

    private void playHand() {
        while (tricksPlayed < CARDS_PER_PLAYER) {
            playTrick();
        }
    }

    private void playTrick() {
        currentTrick = new Trick();
        currentTrick.setTrumpSuit(trumpSuit);
        currentTrick.setLeader(currentPlayer);

        // Each player plays a card
        for (int i = 0; i < MAX_PLAYERS; i++) {
            Player player = getPlayerAtPosition(currentPlayer);
            Card card = player.playCard(currentTrick.getLedSuit());
            currentTrick.addCard(card);
            currentPlayer = currentTrick.getCurrentPlayer();
        }

        // Determine winner and update current player
        Player.Position winner = currentTrick.getWinner();
        if (winner != null) {
            currentPlayer = winner;
            Player winningPlayer = getPlayerAtPosition(winner);
            winningPlayer.setScore(winningPlayer.getScore() + 1);
        }

        tricksPlayed++;
    }

    private Player getPlayerAtPosition(Player.Position position) {
        for (Player player : players) {
            if (player.getPosition() == position) {
                return player;
            }
        }
        return null;
    }

    private void bid() {
        Bid currentBid = null;
        Player.Position currentBidder = Player.Position.NORTH;
        int consecutivePasses = 0; // 4 consecutive passes means bidding ends
        boolean biddingEnded = false;
        
        while (!biddingEnded) {
            Player player = getPlayerAtPosition(currentBidder);
            System.out.println("\n" + player.getName() + "'s turn to bid");
            System.out.println("Current bid: " + (currentBid != null ? currentBid : "No bid yet"));
        
            Bid newBid = player.getBid();
            
            if (newBid.isPass()) {
                consecutivePasses++;
                if (consecutivePasses >= 3 && currentBid != null) {
                    biddingEnded = true;
                }
            } else if (newBid.isDouble() || newBid.isRedouble()) {
                if (currentBid == null || currentBid.isPass()) {
                    System.out.println("Invalid double/redouble - no bid to double");
                    continue;
                }
                if (newBid.isRedouble() && !currentBid.isDouble()) {
                    System.out.println("Invalid redouble - no double to redouble");
                    continue;
                }
                consecutivePasses = 0;
                currentBid = newBid;
            } else {
                if (currentBid != null && !newBid.isHigherThan(currentBid)) {
                    System.out.println("Invalid bid - must be higher than current bid");
                    continue;
                }
                consecutivePasses = 0;
                currentBid = newBid;
            }
            
            currentBidder = getNextPlayer(currentBidder);
        }
        
        // Set the final contract
        if (currentBid != null && !currentBid.isPass()) {
            declarer = findDeclarer(currentBid);
            currentContract = currentBid;
            setTrumpSuit(currentBid.getSuit());
            currentPlayer = getNextPlayer(declarer);
            System.out.println("\nFinal contract: " + currentBid + " by " + getPlayerAtPosition(declarer).getName());
        } else {
            System.out.println("\nAll passed - redeal");
            startGame(players);
        }
    }
    
    private Player.Position findDeclarer(Bid finalBid) {
        // Start from the last bidder and work backwards to find who first bid the final suit
        Player.Position position = getPreviousPlayer(currentPlayer);
        while (true) {
            Player player = getPlayerAtPosition(position);
            Bid playerBid = player.getBid();
            if (!playerBid.isPass() && !playerBid.isDouble() && !playerBid.isRedouble() 
                && playerBid.getSuit() == finalBid.getSuit()) {
                return position;
            }
            position = getPreviousPlayer(position);
        }
    }
    
    private Player.Position getPreviousPlayer(Player.Position current) {
        switch (current) {
            case NORTH: return Player.Position.WEST;
            case EAST: return Player.Position.NORTH;
            case SOUTH: return Player.Position.EAST;
            case WEST: return Player.Position.SOUTH;
            default: return null;
        }
    }

    private Player.Position getNextPlayer(Player.Position current) {
        switch (current) {
            case NORTH: return Player.Position.EAST;
            case EAST: return Player.Position.SOUTH;
            case SOUTH: return Player.Position.WEST;
            case WEST: return Player.Position.NORTH;
            default: return null;
        }
    }

    public void setTrumpSuit(Card.Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }

    private void score() {
        // Determine which team is vulnerable (TO DO: this should be determined by game state)
        boolean nsVulnerable = false; 
        boolean ewVulnerable = false; 
        northSouthScore.setVulnerable(nsVulnerable);
        eastWestScore.setVulnerable(ewVulnerable);
        

        northSouthScore.setContract(currentContract);
        eastWestScore.setContract(currentContract);
        

        int nsTricks = 0;
        int ewTricks = 0;
        
        // Determine which team won each trick
        for (int i = 0; i < CARDS_PER_PLAYER; i++) {
            Player.Position winner = currentTrick.getWinner();
            if (winner == Player.Position.NORTH || winner == Player.Position.SOUTH) {
                nsTricks++;
                northSouthScore.addTrick();
            } else {
                ewTricks++;
                eastWestScore.addTrick();
            }
        }
        
        int nsScore = northSouthScore.calculateScore();
        int ewScore = eastWestScore.calculateScore();
        
        if (declarer == Player.Position.NORTH || declarer == Player.Position.SOUTH) {
            northSouthScore.addPoints(nsScore);
            eastWestScore.addPoints(-nsScore); 
        } else {
            eastWestScore.addPoints(ewScore);
            northSouthScore.addPoints(-ewScore);
        }

        System.out.println("\nScoring Results:");
        System.out.println("North-South: " + nsScore + " points");
        System.out.println("East-West: " + ewScore + " points");
        System.out.println("Contract: " + currentContract);
        System.out.println("Declarer: " + getPlayerAtPosition(declarer).getName());
        System.out.println("Tricks won - NS: " + nsTricks + ", EW: " + ewTricks);
    }
    
    private void addPoints(Score score, int points) {
        score.addPoints(points);
    }
}

// TO DO: 
// - add a class CalculateScore to calculate scores
// - add a class GameState to manage game state
