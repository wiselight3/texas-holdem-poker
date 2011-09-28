import enums.PhaseType;
import enums.PlayerActions;
import enums.PlayerType;

import java.io.IOException;
import java.util.*;


public class PreflopRolloutSimulator {
	private static Deck deck;
	private static List<Player> players;
	private static List<Player> playersInRound;
	private static Table table;
	private static int pot;
	private static int currentBet;
	private static ActionSelector actionSelector;
	private static int smallBlindPosition;
	private static int bigBlindPosition;
    private static int wins, losses, draws;
    
	
	public int getCurrentBet() {
		return currentBet;
	}

    public static void startBetting() {
        boolean bettingNotDone = true;
        int raiseCount = 0;
        while(bettingNotDone){
            Iterator<Player> iterator = playersInRound.iterator();
            while (iterator.hasNext()){
                Player player = iterator.next();
                if(playersInRound.size()==1)
                    return;

                player.setAction(actionSelector.decideActionForPreFlopRollout(player));
                if(player.getAction()==PlayerActions.RAISE){
                    if(raiseCount >= 3){
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
//                System.out.println(player+"($"+player.getMoney()+"): "+player.getAction().toString()+"      "+"Current bet: $"+currentBet+" Pot: $"+pot);

                if(player.hasFolded() || player.getAction()==PlayerActions.CALL)
                    if(isLastCall())
                        return;
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
        dealStartingHandToRemainingPlayers();
//        printGame(true);
        startBetting();

        if(onePLayerLeft())
            return;

        //flop
        dealFlop();
        updatePowerRatingsforPlayers();
//        printGame(false);
        startBetting();

        if (onePLayerLeft())
            return;

        //turn
        table.dealCard(deck.dealCard());
        updatePowerRatingsforPlayers();
//        printGame(false);
        startBetting();

        if (onePLayerLeft())
            return;

        //river
        table.dealCard(deck.dealCard());
        updatePowerRatingsforPlayers();
//        printGame(false);
        startBetting();
    }

	public static void main(String[] args) {
		EquivalenceClassTable equivalenceClassTable = new EquivalenceClassTable();
        long startTime = System.currentTimeMillis();
        int counter=0;
        for(int p=2; p<=10; p++){
            ArrayList<Card> spades = new ArrayList<Card>();
            for(Card.Value val : Card.Value.values()){
                spades.add(new Card(val, Card.Suit.SPADES));
            }

            ArrayList<Card> clubs = new ArrayList<Card>();
            for(Card.Value val : Card.Value.values()){
                clubs.add(new Card(val, Card.Suit.CLUBS));
            }

            for(int i=0; i<13; i++){
                for(int j=i; j<13; j++){
                    int roundsPlayed=0;
                    while (roundsPlayed<Settings.numberOfRounds) {
                        setUpGame(p);

                        players.get(0).dealCard(deck.getSpecificCard(spades.get(i)));
                        players.get(0).dealCard(deck.getSpecificCard(clubs.get(j)));

                        playRound();
//                        distributePotToWinners();
                        updateStats(calculateWinner(playersInRound));
                        tearDown();
                        roundsPlayed++;
                    }
//                    System.out.println("After "+roundsPlayed+" rounds played in "+(System.currentTimeMillis()-startTime)/1000+"s:");
//                    for (Player player : players) {
//                        System.out.println(player + " ended up with " + player.getMoney() + "$ by playing as " +player.playerType);
//                    }
                    double prob = calculateHoldecardProb(p);
                    List<Card> holeCards = new ArrayList<Card>();
                    holeCards.add(spades.get(i));
                    holeCards.add(clubs.get(j));
                    equivalenceClassTable.saveProb(holeCards, p, prob);
                    System.out.println("P0: [ "+spades.get(i)+" "+clubs.get(j)+" ] Players: "+p+" Prob: "+prob);
                    resetStats();
                }
            }

            for(int i=0; i<13; i++){
                for(int j=i+1; j<13; j++){
                    if(i==j)
                        continue;
                    int roundsPlayed=0;
                    while (roundsPlayed<Settings.numberOfRounds) {
                        setUpGame(p);

                        players.get(0).dealCard(deck.getSpecificCard(spades.get(i)));
                        players.get(0).dealCard(deck.getSpecificCard(spades.get(j)));

                        playRound();
//                        distributePotToWinners();
                        updateStats(calculateWinner(playersInRound));
                        tearDown();
                        roundsPlayed++;
                    }
    //                System.out.println("After "+roundsPlayed+" rounds played in "+(System.currentTimeMillis()-startTime)/1000+"s:");
    //                for (Player player : players) {
    //                    System.out.println(player + " ended up with " + player.getMoney() + "$ by playing as " +player.playerType);
    //                }
                    double prob = calculateHoldecardProb(p);
                    List<Card> holeCards = new ArrayList<Card>();
                    holeCards.add(spades.get(i));
                    holeCards.add(spades.get(j));
                    equivalenceClassTable.saveProb(holeCards, p, prob);
                    System.out.println("P0: [ "+spades.get(i)+" "+spades.get(j)+" ] Players: "+p+" Prob: "+prob);
                    resetStats();
                }
            }
        }
        System.out.println("Time: "+(System.currentTimeMillis()-startTime)/1000+"s");

        try {
			equivalenceClassTable.saveProbEquivalenceClassToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private static void resetStats() {
        draws = 0;
        wins = 0;
        losses = 0;
    }

    private static double calculateHoldecardProb(int numberOfPlayers) {
        //( (Wins + Ties/2) / (Wins + Ties + Losses) )^p
        int teller = wins + (draws/2);
        int nevner = wins + draws + losses;
        return Math.pow((double)teller/(double) nevner, (double)numberOfPlayers-1);
    }

    private static void updateStats(List<Player> winners) {
        if(winners.size()>1 && winners.contains(players.get(0)))
            draws++;
        else if(winners.size()==1 && winners.contains(players.get(0)))
            wins++;
        else
            losses++;
    }

    public static void printGame(boolean preFlop){
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
        if(!preFlop){
            System.out.print("\n");
            for(Player pl : players){
                System.out.print(pl+"(power):[ ");
                int[] rating = pl.getPowerRating();
                for(int i : rating){
                    System.out.print(i+" ");
                }
                System.out.print("]     ");
            }
            System.out.print("\n");
        }
        System.out.println("*************************");
    }

    public static void preFlopBetting() {
        for(Player pl : players){
            if(players.indexOf(pl)==players.size()-1){
                pl.setAction(PlayerActions.CHECK);
                System.out.println(pl+"($"+pl.getMoney()+"): "+pl.getAction().toString());
            }else{
                pl.setAction(actionSelector.preFlopFlipOfCoin(pl));
                if(pl.hasFolded())
                    playersInRound.remove(pl);
                else
                    pot += pl.call(currentBet-pl.getBet());
                System.out.println(pl+"($"+pl.getMoney()+"): "+pl.getAction().toString());
            }
        }
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
        System.out.println("*************************");
        List<Player> winners = calculateWinner(playersInRound);
		if (winners.size() > 1) {
			System.out.println("We have a draw: ");
			int temp = pot/winners.size();
			for (Player player : calculateWinner(playersInRound)) {
				System.out.println(player.getId()+" wins: $"+temp);
				player.addMoney(temp);
			}
		} else {
			Player winner = winners.get(0);
			System.out.println("The winner is: ");
            System.out.println(calculateWinner(playersInRound).get(0).getId());
			winner.addMoney(pot);
		}
        System.out.println("*************************");
	}
	
	private static void updatePowerRatingsforPlayers() {
		for (Player player : players) {
			player.updatePowerRating(getPowerRating(player));
		}
	}
	
	public static void setUpGame(int numOfPlayers) {
		actionSelector = new ActionSelector();
		players = new ArrayList<Player>();
		deck = new Deck();
		table = new Table();
		setUpPlayers(numOfPlayers);
	}
	
	public static void setUpRound() {
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
	
	public static void tearDown() {
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
	
	private static void setUpPlayers(int numOfPlayers) {
		for (int i = 0; i < numOfPlayers; i++) {
			players.add(new Player(i, PlayerType.CONSERVATIVE, PhaseType.PHASE1PLAYER));
		}
	}

	//TODO: Ikke random type spiller, b�r predefineres s� de kan sammenlignes.
	private static PlayerType generateType () {
		Random r = new Random();
		r.nextInt(3);
		switch (r.nextInt(3)) {
		case 0:
			return PlayerType.CONSERVATIVE;
		case 1:
			return PlayerType.BLUFFER;
		//case 2:
		//	return PlayerType.NORMAL;
		default:
			return PlayerType.BLUFFER;
		}
	}
	
	public static void dealStartingHandToRemainingPlayers() {
		for (Player player : players) {
            if(players.indexOf(player)==0)
                continue;
            else
			    player.dealCard(deck.dealCard());
		}
		for (Player player : players) {
			if(players.indexOf(player)==0)
                continue;
            else
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