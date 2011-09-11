import java.util.ArrayList;
import java.util.List;


public class Hand {
	
	private List<Card> cards;
	
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	public void dealCard(Card card) {
    	cards.add(card);
    }
	 public List<Card> getCards() {
	    return cards;
	}
	 
    public void printCards() {
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }
	
	
}
