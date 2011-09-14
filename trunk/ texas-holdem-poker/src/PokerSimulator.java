import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enums.PlayerActions;
import enums.PlayerType;


public class PokerSimulator {
	//TODO: re raise
	private static Deck deck;
	private static List<Player> players;
	private static List<Player> playersInRound;
	private static Table table;
	private static int pot;
	private static int currentBet;
	private static ActionSelector actionSelector;
	private static int smallBlindPosition;
	private static int bigBlindPosition;
	
	public int getCurrentBet() {
		return currentBet;
	}

	public static void main(String[] args) {
		
		setUpGame();
		int roundsPlayed=0;
		
		while (roundsPlayed<Settings.numberOfRounds) {
			
            setUpRound();
            dealStartingHandToPlayers();

            //preflop
            preFlop();
            printGame(true);
            
            if (onePLayerLeft())  {
            	distributePotToWinners();
            	roundsPlayed++;
            	tearDown();
            	continue;
            }
        	//flop
            dealFlop();
            updatePowerRatingsforPlayers();
            playersMakeActions();
            printGame(false);
            
            if (onePLayerLeft()) {
            	distributePotToWinners();
            	roundsPlayed++;
            	tearDown();
            	continue;
            }
            	
            //turn
            table.dealCard(deck.dealCard());
            updatePowerRatingsforPlayers();
            playersMakeActions();
            printGame(false);

            if (onePLayerLeft()) {
            	distributePotToWinners();
            	roundsPlayed++;
            	tearDown();
            	continue;
            }
            	
            //river
            table.dealCard(deck.dealCard());
            updatePowerRatingsforPlayers();
            playersMakeActions();
            printGame(false);

            distributePotToWinners();

            tearDown();
            roundsPlayed++;
		}
		
		for (Player player : players) {
			System.out.println(player.getId() + "ended up with " + player.getMoney());
		}
		
	}

    public static void printGame(boolean preFlop){
        for(Player pl : players){
            System.out.print(pl+"($"+pl.getMoney()+"): [ ");
            for(Card card : pl.getCards()){
                System.out.print(card+" ");
            }
            System.out.print("] "+pl.getAction()+"      ");
        }
        System.out.print("\n\nTable: [ ");
        for(Card card : table.getCards()){
            System.out.print(card+" ");
        }
        System.out.print("]     Pot: $"+pot+"\n\n");
        if(!preFlop){
            for(Player pl : players){
                System.out.print(pl+"(power):[ ");
                int[] rating = getPowerRating(pl);
                for(int i : rating){
                    System.out.print(i+" ");
                }
                System.out.print("]     ");
            }
            System.out.print("\n");
        }
    }

    public static void preFlop() {
        for(Player pl : players){
            pl.setAction(actionSelector.preFlopFlipOfCoin(pl));
            if (playersInRound.size() == 1) return;
            
            if(pl.hasFolded())
                playersInRound.remove(pl);
            else
                pot += pl.call(currentBet-pl.getBet());
        }
    }
    
    
    private static boolean onePLayerLeft() {
    	if (playersInRound.size() == 1) return true;
    	return false;
    }
    

	private static void distributePotToWinners() {
		int temp =0;
		if (calculateWinner(playersInRound).size() > 1) {
			System.out.println("We have a draw: ");
			temp = pot/playersInRound.size();
			for (Player player : calculateWinner(playersInRound)) {
				System.out.println(player.getId());
				player.addMoney(temp);
			}
		} else {
			Player winner = calculateWinner(playersInRound).get(0);
			System.out.println("winner is " + calculateWinner(playersInRound).get(0).getId());
			winner.addMoney(pot);
		}
	}

	private static void playersMakeActions() {
		boolean continueBetting = true;
		
		while(continueBetting) {
		
		for (Player player : players) {
			if (playersInRound.size() == 1) return;
			
			if (player.getAction() == PlayerActions.FOLD) {
				playersInRound.remove(player);
				continue;
			}
			
			player.setAction(actionSelector.decideAction(player));
			if (player.getAction() == PlayerActions.RAISE) {
				if (player.getRaises() >= 3) {
					player.setAction(PlayerActions.CALL);
				} else {
					int amountRaised = player.raise(Settings.bigBlind + (currentBet - player.getBet()));
					System.out.println("amount raised: " + amountRaised);
					pot+= amountRaised;
					currentBet+= amountRaised;
					player.AddRaises(1);
				}
			}
				if (player.getAction() == PlayerActions.CALL) {
					 System.out.println("current bet " + currentBet);
					 System.out.println("current player bet" + player.getBet());
					int amountCalled = player.call(currentBet-player.getBet());
					 pot += amountCalled;
					 System.out.println("amount called: " + amountCalled);
					if (amountCalled == 0) player.setAction(PlayerActions.CHECK);
				}
				
			System.out.println(player.getId() + " has " + player.getAction());
		
		}
			continueBetting = false;
			if (playersInRound.size() == 1) return;
			for (Player player : playersInRound) {
				if (player.getBet() < currentBet) continueBetting = true;
			}
		}
		resetRaisesForPlayers();
	}

	private static void resetRaisesForPlayers() {
		for (Player player : players) {
			player.setRaises(0);
		}
	}
	
	private static void updatePowerRatingsforPlayers() {
		for (Player player : players) {
			player.updatePowerRating(getPowerRating(player));
		}
	}
	
	public static void setUpGame() {
		actionSelector = new ActionSelector();
		players = new ArrayList<Player>();
		deck = new Deck();
		table = new Table();
		setUpPlayers(Settings.numOfPlayers);
	}
	
	public static void setUpRound() {
		movePlayerOrder();
		
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
			
			players.add(new Player("p"+i, generateType()));
		}
	}
	//TODO: Ikke random type spiller, b�r predefineres s� de kan sammenlignes.
	private static PlayerType generateType () {
		Random r = new Random();
		r.nextInt(3);
		switch (r.nextInt(3)) {
		case 0:
			return PlayerType.PASSIVE;
		case 1:
			return PlayerType.AGGRESSIVE;
		case 2:
			return PlayerType.NORMAL;
		default:
			return PlayerType.NORMAL;
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
	
	public static void printCardsForPlayers() {
		for (Player player : players) {
			System.out.println(player.getId());
			player.printCards();
		}
	}
	
	public static void dealFlop() {
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
	}
	
	public void RaisePot(int amount) {
		pot = pot + amount;
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
		 List<Player> winners = new ArrayList<Player>();
		 int[] rating;
		 int n;
		 int [] currentBestRating = new int[]{0};
		 for (Player player : players) {
			rating = player.getPowerRating();
			n = calculateBestRating(rating, currentBestRating);
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

	
	 /**
	  * 
	  * @param rating
	  * @param rating2
	  * @return integer: 1 if rating 1 is best, 0 if they are equal, and -1 if rating2 is best.
	  */
	 private static int calculateBestRating(int[] rating, int[] rating2) {
		 int toTraverse;
		 if (rating.length > rating2.length) toTraverse = rating2.length;
		 if (rating.length < rating2.length) toTraverse = rating.length;
		 else toTraverse = rating.length;
		 
		 
		 for (int i = 0; i < toTraverse; i++) {
			if (rating[i] > rating2[i]) return 1;
			if (rating2[i] > rating[i]) return -1;
			if (rating[i] == rating2[i] && toTraverse-i == 1) return 0;
		 } 
		 return 0;
	 }
	 
	 
}
