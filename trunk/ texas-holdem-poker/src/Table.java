import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	private int pot;
	private int currentBet;
	private List<Player> players;
	private Deck deck;
	
	public Deck getDeck() {
		return deck;
	}

	private String id;
	private int smallBlind;
	public  int bigBlind;
	
	public Table(String id, int smallBlind, int bigBlind) {
		this.id = id;
		players = new ArrayList<Player>();
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		deck = new Deck();
	}
	
	public int getPot() {
		return pot;
	}

	public void RaisePot(int amount) {
		pot = pot + amount;
	}

	public void dealCard(Card card) {
    	cards.add(card);
    }
	
    public void dealHand(ArrayList<Card> cards){
        this.cards = cards;
    }
    
    public List<Card> getCards() {
    	return this.cards;
    }
    
    
    public void printCards() {
    	System.out.println("Table Cards:");
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }
    
    public void addPlayerToTable(Player player) {
    	players.add(player);
    }
	
	
}
