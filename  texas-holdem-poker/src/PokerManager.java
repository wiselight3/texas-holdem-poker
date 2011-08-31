import java.util.ArrayList;

public class PokerManager {
	
	private static Deck deck;
	private static Table table;
	

	public static ArrayList<Player> players;
	private static Player p1,p2,p3,p4;
	private static int smallBlind = 25;
	public static int bigBlind =50;
	private static int roundsPlayed=0;
	
	
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
			
			
			
			//before the flop we have no power ratings so use random to decide to bet or not?
			for (Player player : players) {
				int n = (int) Math.random();
				if (n>0.25 && n<0.5) {
					table.RaisePot(player.call(bigBlind)); 
				} else if (n>0.5) {
					table.RaisePot(player.raise(bigBlind));
				} else {
					player.fold();
				}
				
			//}
			//use this after flop using power ratings
			//for (Player player : players) {
				//player.makeDecision();
			//}
			
			dealTableCards();
			table.printCards();
			System.out.println(table.getPot());
			
			roundsPlayed++;
			
		}
		
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
