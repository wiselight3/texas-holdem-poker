import java.util.ArrayList;
import java.util.List;

import enums.PhaseType;
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
	private PhaseType phaseType;
    public final PlayerType playerType;

    private List<Card> cards;
    private int money;
    private int id;
    private int bet;

    private int [] powerRating;

    public PlayerActions getAction() {
		return action;
	}

    public void setAction(PlayerActions action) {
		this.action = action;
	}

	public int[] getPowerRating() {
		return powerRating;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public int getBet() {
		return bet;
	}

	public Player (int id, PlayerType type, PhaseType phaseType){
		this.phaseType = phaseType;
        this.id = id;
        playerType = type;
        cards = new ArrayList<Card>();
        money = Settings.startingCash;
	}

	public int getId() {
		return id;
	}

    public void dealCard(Card card) {
    	cards.add(card);
    }

	public List<Card> getCards() {
	    return cards;
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
    	if (action == PlayerActions.FOLD)
            return true;
    	
    	return false;
    }
    
    public void updatePowerRating(int [] rating) {
	    this.powerRating = rating;
    }

   public void removeCards() {
	   cards.clear();
   }
   
   public void resetAfterRound() {
	   bet =0;
	   action = null;
   }
	
   public String toString (){
       return "P"+id;
   }

   public PhaseType getPhaseType() {
	   return this.phaseType;
   }
}
