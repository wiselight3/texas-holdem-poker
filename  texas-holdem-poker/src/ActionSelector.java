
import enums.PlayerActions;


public class ActionSelector {

	
	
	public PlayerActions decideAction(Player player) {
			switch (player.playerType) {
			case NORMAL:
				return decideActionForNormalPlayer(player);
			case AGGRESSIVE:
				return decideActionForAggressivePlayer(player);
			case PASSIVE:
				return decideActionForPassivePlayer(player);
			default:
			return PlayerActions.CALL;
			}
	}

	
	private PlayerActions decideActionForPassivePlayer(Player player) {
		int [] powerRating = player.getPowerRating();
		if (powerRating[0] >=6) return PlayerActions.RAISE;
		else if (powerRating[0] >=4 && powerRating[0] <6) return PlayerActions.CALL;
		else return PlayerActions.FOLD;	
	}


	private PlayerActions decideActionForAggressivePlayer(Player player) {
		int [] powerRating = player.getPowerRating();
		if (powerRating[0] >=2) return PlayerActions.RAISE;
		else if (powerRating[0] ==1) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
	
	private PlayerActions decideActionForNormalPlayer(Player player) {
		if (player.getPowerRating()[0] >= 4) return PlayerActions.RAISE;
		else if (player.getPowerRating()[0]>=2 && player.getPowerRating()[0] <4) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
}
	

