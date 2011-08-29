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
	
	private int powerRating;
	

	private final int maxRaises = 3;
	private int numberOfRaisesThisRound=0;
	private final PlayerType playerType;
	private int money = 500; //assume everyone starts with the same amount the first round
    private String id;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private boolean smallBlind;
	private boolean bigBlind;
	
	
    public boolean isSmallBlind() {
		return smallBlind;
	}
	public void setSmallBlind(boolean smallBlind) {
		this.smallBlind = smallBlind;
	}
	public boolean isBigBlind() {
		return bigBlind;
	}
	public void setBigBlind(boolean bigBlind) {
		this.bigBlind = bigBlind;
	}
	
	public int getPowerRating() {
		return powerRating;
	}
	public void setPowerRating(int powerRating) {
		this.powerRating = powerRating;
	}

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
    	System.out.println(id + " hand:");
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }
    
    
	public void makeDecision() {
		if (playerType == playerType.NORMAL) {
			if(powerRating > 0.5) {
				raise(PokerManager.bigBlind);
			} else if(powerRating<0.5 && powerRating > 0.25) {
				call();
			} else {
				fold();
			}
		} else if (playerType == playerType.AGGRESSIVE) {
			if (powerRating > 0.25) {
				raise(PokerManager.bigBlind);
			} else if (powerRating < 0.25 && powerRating > 0.10) {
				call();
			} else {
				fold();
			}
			
		} else if (playerType == playerType.PASSIVE) {
			if (powerRating> 0.90) {
				raise(PokerManager.bigBlind);
			} else if(powerRating < 0.90 && powerRating > 0.75) {
				call();
			} else {
				fold();
			}
		}
		
	}
	
	public void fold() {
	}
    
    public int call(int amount) {
    	money = money - amount;
    	return money;
    }
    
    public int raise(int amount) {
    	money = money-amount;
    	return money;
    }
    

}
