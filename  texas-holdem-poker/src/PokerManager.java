import java.util.ArrayList;
import java.util.List;


public class PokerManager {
	
	private static Deck deck;
	private static Table table;
	private static ArrayList<Player> players;
	private static Player p1,p2,p3,p4;
	
	public static void main(String[] args) {
		deck = new Deck();
		deck.buildDeck();
		deck.shuffleDeck();
		table = new Table("round1");
		players = new ArrayList<Player>();
		
		initializePlayers();
		
		
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		
		dealStartingHandForPlayers();
		
		
		dealTableCards();
		List<Card> playerCards = p1.getCards();
		
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
	}

	private static void dealTableCards() {
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
		table.dealCard(deck.dealCard());
	}
	
	
}
