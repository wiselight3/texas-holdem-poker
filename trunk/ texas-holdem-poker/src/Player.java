import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 26.08.11
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class Player {

	public enum PlayerType {PASSIVE, AGGRESSIVE, NORMAL }
	
	private final PlayerType playerType;
	private int money = 500; //assume everyone starts with the same amount the first round
    private String id;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Player (String id, PlayerType type){
        this.id = id;
        playerType = type;
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
