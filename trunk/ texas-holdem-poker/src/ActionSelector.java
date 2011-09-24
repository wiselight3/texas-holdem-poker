
import enums.PlayerActions;
import java.util.*;


public class ActionSelector {

	
	
	public PlayerActions decideAction(Player player) {
			switch (player.playerType) {
			case NORMAL:
				return decideActionForNormalPlayer(player);
			case BLUFFER:
				return decideActionForBluffer(player);
			case CONSERVATIVE:
				return decideActionForConservativePlayer(player);
			default:
			return PlayerActions.CALL;
			}
	}

    public PlayerActions preFlopFlipOfCoin(Player player)  {
        List<Card> holdecards = player.getCards();
        if(holdecards.get(0).getSuit()==holdecards.get(1).getSuit() || holdecards.get(0).getValue()==holdecards.get(1).getValue())
            return PlayerActions.CALL;
        if((int)(Math.random()*2) == 0)
            return PlayerActions.CALL;
        else
            return PlayerActions.FOLD;
    }

	
	private PlayerActions decideActionForConservativePlayer(Player player) {
		int [] powerRating = player.getPowerRating();
		if (powerRating[0] >=5) return PlayerActions.RAISE;
		else if (powerRating[0] >=3 && powerRating[0] <5) return PlayerActions.CALL;
		else return PlayerActions.FOLD;	
	}


	private PlayerActions decideActionForBluffer(Player player) {
		int [] powerRating = player.getPowerRating();
		if (powerRating[0] >=2) return PlayerActions.RAISE;
		else if (powerRating[0] ==1) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
	
	private PlayerActions decideActionForNormalPlayer(Player player) {
		if (player.getPowerRating()[0] >= 3) return PlayerActions.RAISE;
		else if (player.getPowerRating()[0]>=1 && player.getPowerRating()[0] <3) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
}
	
