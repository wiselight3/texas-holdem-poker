import java.util.ArrayList;
import java.util.List;

import enums.PlayerActions;
import enums.PlayerType;


public class PokerSimulator {
	
	private static Deck deck;
	private static List<Player> players;
	private static Table table;
	private static int pot;
	private static int currentBet;

	
	public int getCurrentBet() {
		return currentBet;
	}

	public static void main(String[] args) {
		ActionSelector actionSelector = new ActionSelector();
		
		setUpGame();
		
		
		setUpRound();
		dealStartingHandToPlayers();
		printCardsForPlayers();
	
		dealFlop();
		
		updatePowerRatingsforPlayers();
		
		
		for (Player player : players) {
			player.setAction(actionSelector.decideAction(player));
			if (player.getAction() == PlayerActions.RAISE) pot+= player.raise(Settings.bigBlind + (currentBet - player.getBet()));
			if (player.getAction() == PlayerActions.CALL) pot += player.call(currentBet-player.getBet());
			
		}
		
		//turn
		table.dealCard(deck.dealCard());
		//table.printCards();
		//river
		table.dealCard(deck.dealCard());
		table.printCards();
		
		
	
		
		if (calculateWinner().size() > 1) {
			System.out.println("We have a draw: ");
			for (Player player : calculateWinner()) {
				System.out.println(player.getId());
			}
		} else {
			System.out.println("winner is " + calculateWinner().get(0).getId());
		}

		
	}

	private static void updatePowerRatingsforPlayers() {
		for (Player player : players) {
			player.updatePowerRating(getPowerRating(player));
			System.out.println(player.getId());
			player.printPowerRating();
			System.out.println();
		}
	}
	
	public static void setUpGame() {
		players = new ArrayList<Player>();
		deck = new Deck();
		table = new Table();
		setUpPlayers(Settings.numOfPlayers);
	}
	
	public static void setUpRound() {
		deck.buildDeck();
		deck.shuffleDeck();
	}
	
	private static void setUpPlayers(int numOfPlayers) {
		for (int i = 0; i < numOfPlayers; i++) {
			players.add(new Player("p"+i, PlayerType.NORMAL));
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
	 
	 
	 public static List<Player> calculateWinner() {
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
