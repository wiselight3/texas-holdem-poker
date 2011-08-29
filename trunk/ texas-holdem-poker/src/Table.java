import java.util.ArrayList;
import java.util.List;


public class Table {
	
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	private String id;
	
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
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }
	
	
}
