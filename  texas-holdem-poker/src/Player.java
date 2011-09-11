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
	
	public enum statusForPlayer {FOLD, RAISE, CALL, WAIT, CHECK}
	
	private statusForPlayer status;
	private final int maxRaises = 3;
	private int numberOfRaisesThisRound=0;
	private final PlayerType playerType;
	private int money = 500;
    private String id;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private boolean smallBlind;
	private boolean bigBlind;
	private int bet;
	
	
	public statusForPlayer getStatusForPlayer() {
		return status;
	}
	
	public String getId() {
		return id;
	}
	
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
    	System.out.println(id + " hand: " );
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }
    
    
	public void makeBettingDecision(int[] powerRatings) {
		switch (playerType) {
		case NORMAL:
			if (powerRatings[0] >= 4) {
				raise(PokerManager.bigBlind);
			} else if (powerRatings[0]>=2 && powerRatings[0] <4) {
				if (bet < PokerManager.getTable().bet) {
					call(PokerManager.bigBlind);
				} else {
					check();
				}
			} else {
				fold();
			}
			break;
		case AGGRESSIVE:
			if (powerRatings[0] >= 2) {
				raise(PokerManager.bigBlind);
			} else if (powerRatings[0] == 1) {
				call(PokerManager.bigBlind);
			} else {
				fold();
			}
			break;
		case PASSIVE:
			if (powerRatings[0] >=6) {
				raise(PokerManager.bigBlind);
			} else if(powerRatings[0] >= 4 && powerRatings[0] <6) {
				call(PokerManager.bigBlind);
			} else {
				fold();
			}
			break;
		}
	}
	
	public void fold() {
	this.status = statusForPlayer.FOLD;	
	}
	
	
	public void check() {
		status = statusForPlayer.CHECK;
	}
	
    public int call(int amount) {
    	this.status = statusForPlayer.CALL;
    	money = money - amount;
    	this.bet = PokerManager.getTable().bet;
    	PokerManager.getTable().RaisePot(amount);
    	return amount;
    }
    
    public int raise(int amount) {
    	this.status = statusForPlayer.RAISE;	
    	money = money-amount;
    	this.bet = PokerManager.getTable().bet + amount;
    	PokerManager.getTable().bet += amount;
    	PokerManager.getTable().RaisePot(amount);
    	numberOfRaisesThisRound++;
        return amount;
    }
    
    public void resetStatus () {
    	this.status = statusForPlayer.WAIT;
    }
    
    public boolean hasFolded() {
    	if (status == statusForPlayer.FOLD) return true;
    	
    	return false;
    }
    
    

}
