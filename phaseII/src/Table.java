import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private ArrayList<Card> cards;
	
	public Table() {
		cards = new ArrayList<Card>();
	}
	
	public void dealCard(Card card) {
    	cards.add(card);
    }
	
    public void dealFlop(ArrayList<Card> cards){
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
    
    public void removeCards() {
    	cards.clear();
    }
	
	
}
