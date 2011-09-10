import java.util.ArrayList;
import java.util.List;

public class PokerManager {
	
	private static Deck deck;
	private static Table table;
	
	static int numOfFolds;
	public static ArrayList<Player> players;
	private static Player p1,p2,p3,p4;
	private static int smallBlind = 25;
	public static int bigBlind =50;
	private static int roundsPlayed=0;
	private static CardRating cardRating = new CardRating();
	
	
	public static void main(String[] args) {
		players = new ArrayList<Player>();
		initializePlayers();
		deck = new Deck();
		
		
		while(roundsPlayed < 1) {
			System.out.println("Round " + roundsPlayed);
			deck.buildDeck();
			deck.shuffleDeck();
			table = new Table("round");
			
			initializeBlinds();
			dealStartingHandForPlayers();
			printCardsForPlayers();
			table.bet = bigBlind;
			
			dealFlop();
			table.printCards();
			
			
			while (numOfFolds <3) {
				//while more than 1 player left and raises <3
				for (Player player : players) {
					int [] powerRatings = getPowerRatingArrayForPLayer(player);
					if (!player.hasFolded()) {
						player.makeBettingDecision(powerRatings);
					}
					System.out.println(player.getStatusForPlayer());
					System.out.println("current bet: " + table.bet);
				}
				
			}
			

			System.out.println(table.getPot());
			table.printCards();
			
			
			// river card
			table.dealCard(deck.dealCard());
			table.printCards();
			
			// turn card
			table.dealCard(deck.dealCard());
			table.printCards();
			
			
			roundsPlayed++;
		}
	}
		
	
	private static int [] getPowerRatingArrayForPLayer(Player player) {
		ArrayList<Card> playerHand = new ArrayList<Card>();
		playerHand.add(player.getCards().get(0));
		playerHand.add(player.getCards().get(1));
		
		
		for (Card card : table.getCards()) {
			playerHand.add(card);
		}
		return cardRating.calcCardsPower(playerHand);
	}
	

	private static void initializeBlinds() {
		p1.setSmallBlind(true);
		p2.setBigBlind(true);
		table.RaisePot(smallBlind); 
		table.RaisePot(bigBlind);	
	}

	private static void printCardsForPlayers() {
		for (Player player : players) {
			player.printCards();
		}
	}

	private static void dealStartingHandForPlayers() {
		for (Player player : players) {
			Card c = deck.dealCard();
			player.dealCard(c);
		}
		
		for (Player player : players) {
			Card c = deck.dealCard();
			player.dealCard(c);
		}
	}

	
	private static void initializePlayers() {
		p1 = new Player("p1", Player.PlayerType.AGGRESSIVE);
		p2 = new Player("p2", Player.PlayerType.NORMAL);
		p3 = new Player("p3", Player.PlayerType.PASSIVE);
		p4 = new Player("p4", Player.PlayerType.NORMAL);

		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		
	}

	private static void dealFlop() {
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
	}
	
	public static Table getTable() {
		return table;
	}
	
}
