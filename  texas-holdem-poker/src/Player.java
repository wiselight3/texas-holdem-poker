import java.util.ArrayList;
import java.util.List;

import enums.PlayerActions;
import enums.PlayerType;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 26.08.11
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class Player {

	private PlayerActions action;
	public PlayerActions getAction() {
		return action;
	}
	public void setAction(PlayerActions action) {
		this.action = action;
	}
	public final PlayerType playerType;
	
	private List<Card> cards;
	private int money = 500;
    private String id;
    private int [] powerRating;
    
	public int[] getPowerRating() {
		return powerRating;
	}

	private int bet;
	
	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public int getBet() {
		return bet;
	}
	public Player (String id, PlayerType type){
        this.id = id;
        playerType = type;
        cards = new ArrayList<Card>();
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
	
    /**
     * Call or check. Depends on currentBet compared to players bet
     * @param amount
     * @return the amount used to call
     */
    public int call(int amount) {
    	money = money - amount;
    	bet+=amount;
    	return amount;
    }
    
    public int raise(int amount) {
    	money = money-amount;
    	bet +=amount;
        return amount;
    }
    
    public void addMoney(int amount) {
    	this.money+= amount;
    }
    public int getMoney() {
    	return money;
    }
    
    
    public boolean hasFolded() {
    	if (action == PlayerActions.FOLD) return true;
    	
    	return false;
    }
    
   public void updatePowerRating(int [] rating) {
	   this.powerRating = rating;
   }
   
   public void printPowerRating() {
	   System.out.print("[");
	   for (Integer inte : powerRating) {
		System.out.print(" " + inte);
	}
	   System.out.print("]");
   }
   
   
   public void removeCards() {
	   cards.clear();
   }
	
    
    

}
