
import enums.PlayerActions;
import java.util.*;


public class ActionSelector {

	public PlayerActions decideAction(Player player, Table table, List<Player> players, EquivalenceClassTable ect, boolean preFlop){
        switch (player.getPhaseType()) {
            case PHASE1PLAYER:
                return decideActionsForPhase1Players(player, preFlop);
            case PHASE2PLAYER:
                return decideActionsForPhase2Players(player, table, players, ect, preFlop);
            case PHASE3PLAYER:
                return decideActionsForPhase3Players(player, table, preFlop);
            default:
                return PlayerActions.CALL;
        }
    }

    public PlayerActions decideActionsForPhase2Players(Player player, Table table, List<Player> players, EquivalenceClassTable ect, boolean preFlop) {
        CardRating cardRating = new CardRating();
        double probForWinning;
        if(preFlop)
		    probForWinning = ect.getProb(player.getCards(), players.size());
        else
            probForWinning = cardRating.handStrength(player.getCards(), table.getCards(), players.size());

		switch (player.playerType) {
            case BLUFFER:
                return decideActionForPhase2Bluffer(probForWinning);
            case CONSERVATIVE:
                return decideActionForPhase2ConservativePlayer(probForWinning);
            case NORMAL:
                return decideActionForPhase2NormalPlayer(probForWinning);
            default:
                return PlayerActions.CALL;
		}
	}

    private PlayerActions decideActionForPhase2ConservativePlayer(double probForWinning) {
		if (probForWinning > 0.7) return PlayerActions.RAISE;
		else if (probForWinning > 0.4) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}

	private PlayerActions decideActionForPhase2Bluffer(double probForWinning) {
		if (probForWinning > 0.3) return PlayerActions.RAISE;
		else if (probForWinning > 0.1) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}

    private PlayerActions decideActionForPhase2NormalPlayer(double probForWinning) {
        if (probForWinning > 0.4) return PlayerActions.RAISE;
		else if (probForWinning > 0.2) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
    }

    //ToDo: innhold til fase 3
    private PlayerActions decideActionsForPhase3Players(Player player, Table table, boolean preFlop) {
        return null;
    }

	public PlayerActions decideActionsForPhase1Players(Player player, boolean preFlop) {
        if(preFlop)
            return preFlopFlipOfCoin(player);
        else{
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
	}

    public PlayerActions preFlopFlipOfCoin(Player player)  {
        List<Card> holeCards = player.getCards();
        if(holeCards.get(0).getSuit()==holeCards.get(1).getSuit() || holeCards.get(0).getValue()==holeCards.get(1).getValue())
            return PlayerActions.CALL;
        if((int)(Math.random()*2) == 0)
            return PlayerActions.CALL;
        else
            return PlayerActions.FOLD;
    }
    
    public PlayerActions decideActionForPreFlopRollout(Player player){
        return PlayerActions.CALL;
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
        int [] powerRating = player.getPowerRating();
		if (powerRating[0] >= 4) return PlayerActions.RAISE;
		else if (powerRating[0]>=2 && powerRating[0] <4) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
}
	

