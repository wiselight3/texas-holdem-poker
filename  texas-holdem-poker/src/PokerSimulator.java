import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PokerSimulator {
	
	
	
	private static PokerManager manager;
	private static Deck deck;
	private static List<Player> players;
	private static Table table;
	private int pot;
	private int currentBet;

	
	public static void main(String[] args) {

		setUpGame();
		
		
		setUpRound();
		dealStartingHandToPlayers();
		printCardsForPlayers();
		dealFlop();
		//table.printCards();
		table.dealCard(deck.dealCard());
		//table.printCards();
		table.dealCard(deck.dealCard());
		table.printCards();
		
		for (Player player : players) {
			player.updatePowerRating(getPowerRating(player));
			System.out.println(player.getId());
			player.printPowerRating();
			System.out.println();
		}
		System.out.println("winner is " + calculateWinner().getId());
		
		
		
	}
	
	public static void setUpGame() {
		players = new ArrayList<Player>();
		manager = new PokerManager();
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
			players.add(new Player("p"+i, Player.PlayerType.NORMAL));
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
	 
	 
	 public static Player calculateWinner() {
		 Player currentWinner = new Player(null,null);
		 int[] rating;
		 int n;
		 int [] currentBestRating = new int[]{0};
		 for (Player player : players) {
			rating = player.getPowerRating();
			n = calculateBestRating(rating, currentBestRating);
			if (n == 1)  {
				currentBestRating = rating;
				currentWinner = player;
			}
			//if (n== 0) uavgjort not yet implemented
			
	 }
		 return currentWinner;
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
