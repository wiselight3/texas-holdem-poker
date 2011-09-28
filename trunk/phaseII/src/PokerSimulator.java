import enums.PhaseType;
import enums.PlayerActions;
import enums.PlayerType;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class PokerSimulator {
	private static Deck deck;
	private static List<Player> players;
	private static List<Player> playersInRound;
	private static Table table;
	private static int pot;
	private static int currentBet;
    private static EquivalenceClassTable equivalenceClassTable;
    private static CardRating cardRating;
	private static ActionSelector actionSelector;
    private static OpponentModeler opponentModeler;
	private static int smallBlindPosition;
	private static int bigBlindPosition;
    private static int roundsPlayed;

    //Simulates a round of betting
    public static void startBetting(boolean preFlop) {
        boolean bettingNotDone = true;
        int raiseCount = 0;
        while(bettingNotDone){
            Iterator<Player> iterator = playersInRound.iterator();
            while (iterator.hasNext()){
                Player player = iterator.next();
                if(playersInRound.size()==1)
                    return;

                player.setAction(actionSelector.decideAction(player, table, playersInRound, equivalenceClassTable, preFlop, opponentModeler, roundsPlayed));
                if(player.getAction()==PlayerActions.RAISE){
                    if(raiseCount >= Settings.maxNumRaises){
                        if(currentBet==player.getBet())
                            player.setAction(PlayerActions.CHECK);
                        else{
                            player.setAction(PlayerActions.CALL);
                            pot += player.call(currentBet-player.getBet());
                        }
                    }else{
                        raiseCount++;
                        int amount = player.raise(Settings.bigBlind + (currentBet-player.getBet()));
                        pot += amount;
                        currentBet += Settings.bigBlind;
                    }
                }else if(player.getAction()==PlayerActions.CALL){
                    if(currentBet==player.getBet()){
                        player.setAction(PlayerActions.CHECK);
                    }else{
                        pot += player.call(currentBet-player.getBet());
                    }
                }else if(player.getAction()==PlayerActions.FOLD){
                    if(currentBet==player.getBet()){
                        player.setAction(PlayerActions.CHECK);
                    }
                    else{
                        iterator.remove();
                    }
                }

                if(roundsPlayed<Settings.numRoundsToCollectData){
                    double strength = preFlop? equivalenceClassTable.calcPreflopProbabilityStrength(player.getCards(), playersInRound.size()) : cardRating.handStrength(player.getCards(), table.getCards(), playersInRound.size());
                    opponentModeler.savePlayerData(player.getId(), preFlop, player.getAction(), strength);
                }

                if(roundsPlayed<Settings.numberOfRoundsToPrint)
                    System.out.println(player+"($"+player.getMoney()+"): "+player.getAction().toString()+"      "+"Current bet: $"+currentBet+" Pot: $"+pot);

                if(player.hasFolded() || player.getAction()==PlayerActions.CALL){
                    if(isLastCall() && !preFlop)
                        return;
                }
            }

            for(Player player : playersInRound){
                if(currentBet != player.getBet()){
                    bettingNotDone = true;
                    break;
                }else
                    bettingNotDone = false;
            }
        }
    }

    public static void playRound() {
        setUpRound();

        //preflop
        dealStartingHandToPlayers();
        printGame(true);
        startBetting(true);

        if(onePLayerLeft())
            return;

        //flop
        dealFlop();
        updatePowerRatingsforPlayers();
        printGame(false);
        startBetting(false);

        if (onePLayerLeft())
            return;

        //turn
        table.dealCard(deck.dealCard());
        updatePowerRatingsforPlayers();
        printGame(false);
        startBetting(false);

        if (onePLayerLeft())
            return;

        //river
        table.dealCard(deck.dealCard());
        updatePowerRatingsforPlayers();
        printGame(false);
        startBetting(false);
    }

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

        //setUpPhase1Game();
        //setUpPhase1vsPhase2Game();
        setUpAllPhasesGame();

        opponentModeler = new OpponentModeler(players);

		while(roundsPlayed<Settings.numberOfRounds) {
            playRound();
            if(roundsPlayed<Settings.numRoundsToCollectData)
                opponentModeler.saveDataForShowdownPlayers(playersInRound);
            distributePotToWinners();
            tearDown();
            roundsPlayed++;
        }

        movePlayerOrder();
        movePlayerOrder();

		System.out.println("After "+roundsPlayed+" rounds played in "+(System.currentTimeMillis()-startTime)/1000+"s:");
		for (Player player : players) {
			System.out.println(player + " ended up with " + player.getMoney() + "$ by playing as "+player.getPhaseType()+" "+player.playerType);
		}

		int sum =0;
		for (Player player : players) {
			sum+= player.getMoney();
		}
		//System.out.println("This should be "+(Settings.startingCash*players.size())+" for "+players.size()+" players?: " + sum);
	}

    public static void printGame(boolean preFlop){
        if(roundsPlayed<Settings.numberOfRoundsToPrint){
            if(preFlop)
                System.out.println("******** Preflop ********");
            else if(table.getCards().size()==3)
                System.out.println("********   Flop  ********");
            else if(table.getCards().size()==4)
                System.out.println("********   Turn  ********");
            else if(table.getCards().size()==5)
                System.out.println("********  River  ********");

            for(Player pl : players){
                System.out.print(pl+"($"+pl.getMoney()+")");
                if(players.indexOf(pl)==players.size()-2)
                    System.out.print("{SB}");
                else if(players.indexOf(pl)==players.size()-1)
                    System.out.print("{BB}");
                System.out.print(": [ ");
                for(Card card : pl.getCards()){
                    System.out.print(card+" ");
                }
                System.out.print("] ");
                if(pl.getAction()!=null)
                    System.out.print(pl.getAction() + "      ");
                else
                    System.out.print("          ");
            }
            System.out.print("\n\nTable: [ ");
            for(Card card : table.getCards()){
                System.out.print(card+" ");
            }
            System.out.print("]     Pot: $"+pot+"\n");
            DecimalFormat threeDec = new DecimalFormat("#.###");
            if(!preFlop){
                System.out.print("\n");
                for(Player pl : players){
                    System.out.print(pl+"(power):[ ");
                    int[] rating = pl.getPowerRating();
                    for(int i : rating){
                        System.out.print(i+" ");
                    }

                    System.out.print("] (prob):[ "+threeDec.format(cardRating.handStrength(pl.getCards(), table.getCards(), playersInRound.size()))+" ]     ");
                }
                System.out.print("\n");
            }else{
                System.out.print("\n");
                for(Player pl : players){
                    System.out.print(pl+"(prob):[ "+threeDec.format(equivalenceClassTable.calcPreflopProbabilityStrength(pl.getCards(), playersInRound.size()))+" ]     ");
                }
                System.out.print("\n");
            }
            System.out.println("*************************");
        }else
            return;
    }

    private static boolean isLastCall() {
        for(Player player : playersInRound){
            if(player.getBet()!=currentBet)
                return false;
        }
        return true;
    }
    
    private static boolean onePLayerLeft() {
    	if (playersInRound.size() == 1) return true;
    	return false;
    }

	private static void distributePotToWinners() {
        if(roundsPlayed<Settings.numberOfRoundsToPrint)
            System.out.println("*************************");
        List<Player> winners = calculateWinner(playersInRound);
		if (winners.size() > 1) {
            if(roundsPlayed<Settings.numberOfRoundsToPrint)
			    System.out.println("We have a draw: ");
			int temp = pot/winners.size();
			for (Player player : calculateWinner(playersInRound)) {
                if(roundsPlayed<Settings.numberOfRoundsToPrint)
				    System.out.println(player+" wins: $"+temp);
				player.addMoney(temp);
			}
		} else {
			Player winner = winners.get(0);
            if(roundsPlayed<Settings.numberOfRoundsToPrint){
			    System.out.println("The winner is: ");
                System.out.println(calculateWinner(playersInRound).get(0));
            }
			winner.addMoney(pot);
		}
        if(roundsPlayed<Settings.numberOfRoundsToPrint)
            System.out.println("*************************");
	}
	
	private static void updatePowerRatingsforPlayers() {
		for (Player player : players) {
			player.updatePowerRating(getPowerRating(player));
		}
	}

    private static void setUpPhase1Game(){
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE1PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
        players.add(new Player(2, PlayerType.BLUFFER, PhaseType.PHASE1PLAYER));
        players.add(new Player(3, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
    }

    private static void setUpPhase2Game(){
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE2PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
        players.add(new Player(2, PlayerType.BLUFFER, PhaseType.PHASE2PLAYER));
        players.add(new Player(3, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
    }

    private static void setUpPhase3Game(){
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE3PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE3PLAYER));
    }

    private static void setUpPhase1vsPhase2Game() {
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE1PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
        players.add(new Player(2, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
        players.add(new Player(3, PlayerType.BLUFFER, PhaseType.PHASE2PLAYER));
        players.add(new Player(4, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
        players.add(new Player(5, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
    }
    
    private static void setUpPhase2vsPhase3Game() {
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE2PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
        players.add(new Player(2, PlayerType.CONSERVATIVE, PhaseType.PHASE2PLAYER));
        players.add(new Player(3, PlayerType.BLUFFER, PhaseType.PHASE3PLAYER));
        players.add(new Player(4, PlayerType.CONSERVATIVE, PhaseType.PHASE3PLAYER));
        players.add(new Player(5, PlayerType.CONSERVATIVE, PhaseType.PHASE3PLAYER));
    }

    private static void setUpCustomGame(){
        setUpGame();
        players.add(new Player(0, PlayerType.BLUFFER, PhaseType.PHASE1PLAYER));
        players.add(new Player(1, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
        players.add(new Player(2, PlayerType.BLUFFER, PhaseType.PHASE3PLAYER));
        players.add(new Player(3, PlayerType.CONSERVATIVE, PhaseType.PHASE3PLAYER));
    }

    private static void setUpAllPhasesGame() {
        setUpGame();
        int i=0;
        for(PhaseType phase : PhaseType.values()){
            for(PlayerType type : PlayerType.values()){
                players.add(new Player(i, type, phase));
                i++;
            }
        }
    }
	
	private static void setUpGame() {
        equivalenceClassTable = new EquivalenceClassTable();
        try {
            equivalenceClassTable.readProbEquivalenceClassFromFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        actionSelector = new ActionSelector();
        cardRating = new CardRating();
		players = new ArrayList<Player>();
		deck = new Deck();
		table = new Table();
	}
	
	private static void setUpRound() {
		playersInRound = new ArrayList<Player>();
		playersInRound.addAll(players);
		deck.buildDeck();
		deck.shuffleDeck();
		
		setBlinds();
		currentBet = Settings.bigBlind;
	}
	
	private static void movePlayerOrder() {
		Player temp = players.remove(players.size()-1);
		players.add(0, temp);
	}
	
	private static void setBlinds() {
		smallBlindPosition = players.size()-2;
		bigBlindPosition = players.size()-1;
		pot += players.get(smallBlindPosition).raise(Settings.smallBlind);
		pot += players.get(bigBlindPosition).raise(Settings.bigBlind);
	}
	
	private static void tearDown() {
		playersInRound.clear();
		for (Player player : players) {
			player.setBet(0);
		}
		currentBet =0;
		pot =0;
		collectCards();
		resetPlayers();
        movePlayerOrder();
	}
	
	private static void collectCards() {
		for (Player player : players) {
			player.removeCards();
		}
		table.removeCards();
	}
	
	private static void resetPlayers() {
		for (Player player : players) {
			player.resetAfterRound();
		}
	}
	
	private static void setUpPlayers(PhaseType phase, PlayerType type) {
		for (int i = 0; i < Settings.numOfPlayers; i++) {
			players.add(new Player(i, type, phase));
		}
	}

	public static void dealStartingHandToPlayers() {
		for (Player player : players) {
			player.dealCard(deck.dealCard());
		}
		for (Player player : players) {
			player.dealCard(deck.dealCard());
		}
	}
	
	public static void dealFlop() {
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
	}
	
	private static int [] getPowerRating(Player player) {
	    ArrayList<Card> hand = new ArrayList<Card>();
		CardRating rating = new CardRating();
		hand.add(player.getCards().get(0));
		hand.add(player.getCards().get(1));

		for (Card card : table.getCards()) {
		    hand.add(card);
		}
			
		return rating.calcCardsPower(hand);
	}
	 
	 
    public static List<Player> calculateWinner(List<Player> players) {
        if(players.size()==1)
            return players;
	    List<Player> winners = new ArrayList<Player>();
		int[] rating;
	    int n;
		int [] currentBestRating = new int[]{0};
		for (Player player : players) {
		    rating = player.getPowerRating();
			n = CardRating.calculateBestRating(rating, currentBestRating);
			if (n == 1)  {
				currentBestRating = rating;
				winners.clear();
				winners.add(player);
			}
			if (n== 0) {
				winners.add(player);
			}
	    }
        return winners;
	}
}