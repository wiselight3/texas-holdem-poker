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
	private final PlayerType playerType;
	
	private List<Card> cards;
	private int money = 500;
    private String id;
    private int [] powerRating;
	public int[] getPowerRating() {
		return powerRating;
	}

	private int bet;
	
	
	public Player (String id, PlayerType type){
        this.id = id;
        playerType = type;
        cards = new ArrayList<Card>();
	}
	
	public statusForPlayer getStatusForPlayer() {
		return status;
	}
	public String getId() {
		return id;
	}
 
    public void dealCard(Card card) {
    	cards.add(card);
    }
    public void dealHand(ArrayList<Card> cards){
        this.cards = cards;
    }    
  

	 public List<Card> getCards() {
	    return cards;
	}
	 
    public void printCards() {
    	for (Card card : cards) {
			System.out.println(card.toString());
		}
    }

    
    /*
	public void makeBettingDecision(int[] powerRatings) {
		switch (playerType) {
		case NORMAL:
			if (powerRatings[0] >= 4) {
				raise(Settings.bigBlind);
			} else if (powerRatings[0]>=2 && powerRatings[0] <4) {
				if (bet < PokerSimulator.round.getCurrentBet()) {
					call(PokerSimulator.round.getCurrentBet()-bet);
				} else {
					check();
				}
			} else {
				fold();
			}
			break;
		case AGGRESSIVE:
			if (powerRatings[0] >= 2) {
				raise(Settings.bigBlind);
			} else if (powerRatings[0] == 1) {
				call(Settings.bigBlind);
			} else {
				fold();
			}
			break;
		case PASSIVE:
			if (powerRatings[0] >=6) {
				raise(Settings.bigBlind);
			} else if(powerRatings[0] >= 4 && powerRatings[0] <6) {
				call(Settings.bigBlind);
			} else {
				fold();
			}
			break;
		}
	}
	*/
	public void fold() {
	this.status = statusForPlayer.FOLD;	
	}
	
	
	public void check() {
		status = statusForPlayer.CHECK;
	}
	
    public int call(int amount) {
    	this.status = statusForPlayer.CALL;
    	money = money - amount;
    	//this.bet = PokerManager.getTable().bet;
    	//PokerManager.getTable().RaisePot(amount);
    	return amount;
    }
    
    public int raise(int amount) {
    	this.status = statusForPlayer.RAISE;	
    	money = money-amount;
    	//this.bet = PokerManager.getTable().bet + amount;
    	//PokerManager.getTable().bet += amount;
    	//PokerManager.getTable().RaisePot(amount);
    	//numberOfRaisesThisRound++;
        return amount;
    }
    
    public void resetStatus () {
    	this.status = statusForPlayer.WAIT;
    }
    
    public boolean hasFolded() {
    	if (status == statusForPlayer.FOLD) return true;
    	
    	return false;
    }
    
   public void updatePowerRating(int [] rating) {
	   this.powerRating = rating;
   }
   
   public void printPowerRating() {
	   for (Integer inte : powerRating) {
		System.out.print(inte);
	}
   }
	
    
    

}
