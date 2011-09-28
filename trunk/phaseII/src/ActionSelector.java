
import enums.PlayerActions;
import java.util.*;


public class ActionSelector {

	public PlayerActions decideAction(Player player, Table table, List<Player> players, EquivalenceClassTable ect, boolean preFlop, OpponentModeler opponentModeler, int roundsPlayed){
        switch (player.getPhaseType()) {
            case PHASE1PLAYER:
                return decideActionsForPhase1Players(player, preFlop);
            case PHASE2PLAYER:
                return decideActionsForPhase2Players(player, table, players, ect, preFlop);
            case PHASE3PLAYER:
            	if (roundsPlayed < Settings.numRoundsForPhase3ToWait) {
            		return decideActionsForPhase2Players(player, table, players, ect, preFlop);
            	}
                return decideActionsForPhase3Players(player, table, preFlop, ect, opponentModeler, players);
            default:
                return PlayerActions.CALL;
        }
    }

    public PlayerActions decideActionsForPhase2Players(Player player, Table table, List<Player> playersInRound, EquivalenceClassTable ect, boolean preFlop) {
        CardRating cardRating = new CardRating();
        double probForWinning;
        if(preFlop)
		    probForWinning = ect.calcPreflopProbabilityStrength(player.getCards(), playersInRound.size());
        else
            probForWinning = cardRating.handStrength(player.getCards(), table.getCards(), playersInRound.size());

		switch (player.playerType) {
            case BLUFFER:
                return decideActionForPhase2Bluffer(probForWinning);
            case CONSERVATIVE:
                return decideActionForPhase2ConservativePlayer(probForWinning);
            default:
                return PlayerActions.CALL;
		}
	}

    private PlayerActions decideActionForPhase2ConservativePlayer(double probForWinning) {
		if (probForWinning >= 0.5) return PlayerActions.RAISE;
		else if (probForWinning >= 0.3) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}

	private PlayerActions decideActionForPhase2Bluffer(double probForWinning) {
		if (probForWinning >= 0.3) return PlayerActions.RAISE;
		else if (probForWinning >= 0.2) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}

    private PlayerActions decideActionsForPhase3Players(Player player, Table table, boolean preFlop, EquivalenceClassTable ect, OpponentModeler opponentModeler, List<Player> playersInRound) {
    	CardRating cardRating = new CardRating();
        double probForWinning;
        if(preFlop)
		    probForWinning = ect.calcPreflopProbabilityStrength(player.getCards(), playersInRound.size());
        else
            probForWinning = cardRating.handStrength(player.getCards(), table.getCards(), playersInRound.size());

        double [] otherPlayerDatas = new double [playersInRound.size()];
        
        int counter =0;

        while(counter < playersInRound.size()) {
        	if (playersInRound.get(counter).getAction() == null) {
        		counter++;
        		continue;
        	}

        	if (playersInRound.get(counter).getId() == player.getId()) {
        		counter++;
        		continue;
            }else{
        	    otherPlayerDatas[counter] = opponentModeler.getPlayerData(playersInRound.get(counter).getId(), preFlop, playersInRound.get(counter).getAction());
        	    //System.out.println("" + playersInRound.get(counter) + opponentModeler.getPlayerData(playersInRound.get(counter).getId(), preFlop, playersInRound.get(counter).getAction()));
        	    counter++;
    		}
        }

    	switch (player.playerType) {
            case BLUFFER:
                return decideActionForPhase3Bluffer(probForWinning, otherPlayerDatas);
            case CONSERVATIVE:
                return decideActionForPhase3Conservative(probForWinning, otherPlayerDatas);
            default:
                return PlayerActions.CALL;
		}
    }

	private PlayerActions decideActionForPhase3Conservative(double probForWinning, double[] otherPlayerDatas) {
		double strongestHandStrength = 0;
		for (double d : otherPlayerDatas) {
			if (d>strongestHandStrength) strongestHandStrength = d;
		}

		if (probForWinning > strongestHandStrength) return PlayerActions.RAISE;
		else if (probForWinning >= strongestHandStrength-0.30) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}

	private PlayerActions decideActionForPhase3Bluffer(double probForWinning,double[] otherPlayerDatas) {
		double strongestHandStrength = 0;
		for (double d : otherPlayerDatas) {
			if (d>strongestHandStrength) strongestHandStrength = d;
		}
		
		if (probForWinning > strongestHandStrength-0.30) return PlayerActions.RAISE;
		else if (probForWinning > strongestHandStrength-0.50) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
		
	}

	public PlayerActions decideActionsForPhase1Players(Player player, boolean preFlop) {
        if(preFlop)
            return preFlopFlipOfCoin(player);
        else{
            switch (player.playerType) {
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
		if (powerRating[0] >=3) return PlayerActions.RAISE;
		else if (powerRating[0] ==2) return PlayerActions.CALL;
		else return PlayerActions.FOLD;
	}
}
	

