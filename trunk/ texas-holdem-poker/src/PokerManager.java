import java.util.ArrayList;
import java.util.List;

public class PokerManager {
	
	private static Deck deck;
	private static Table table;
	

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
			table = new Table("round"); //Make a round class and keep table consistent?
			
			initializeBlinds();
			dealStartingHandForPlayers();
			
			printCardsForPlayers();
			
			for (Player player : players) {
				player.makeDecision();
			}
			
			dealTableCards();
			
			/*
			ArrayList<Card> playerHand = new ArrayList<Card>();
			playerHand.add(p1.getCards().get(0));
			playerHand.add(p1.getCards().get(1));
			playerHand.add(table.getCards().get(0));
			playerHand.add(table.getCards().get(1));
			playerHand.add(table.getCards().get(2));
			*/
			//int [] powerRatingArray=
			//cardRating.calcCardsPower(playerHand);
			
			//p1.setPowerRating(powerRating)
			
			table.printCards();
			System.out.println(table.getPot());
			
			roundsPlayed++;
			
		}
		
	}
		
	

	private static void initializeBlinds() {
		p1.setSmallBlind(true);
		p2.setBigBlind(true);
		table.RaisePot(p1.raise(smallBlind)); 
		table.RaisePot(p2.raise(bigBlind));
		
		p1.addPotMoneyFromPlayer(smallBlind);
		p2.addPotMoneyFromPlayer(bigBlind);
			
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

	private static void dealTableCards() {
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
	}
	
	public static Table getTable() {
		return table;
	}
	
}
