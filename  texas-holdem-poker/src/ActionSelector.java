import enums.PlayerActions;


public class ActionSelector {

	
	
	public PlayerActions decideAction(Player player) {
			switch (player.playerType) {
			case NORMAL:
				return decideActionForNormalPlayer(player);
				break;
			case AGGRESSIVE:
				decideActionForAggressivePlayer(player);
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
	

