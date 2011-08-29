import java.util.ArrayList;
import java.util.List;


public class Table {
	
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	private String id;
	private int pot;
	
	
	public int getPot() {
		return pot;
	}

	public void RaisePot(int amount) {
		pot = pot + amount;
	}


	public Table (String id){
        this.id = id;
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
	
	
}
