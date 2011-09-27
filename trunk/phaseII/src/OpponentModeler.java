import enums.PlayerActions;

import java.util.List;

public class OpponentModeler {

    private class playerModel {
        public double totalHandStrength=0;
        public int numberOfHands=0;
    }

    private playerModel[][][] playerModels;
    private playerModel[][][] tempModels;

    public OpponentModeler(List<Player> players) {
        playerModels = new playerModel[players.size()][2][PlayerActions.values().length];
        tempModels = new playerModel[players.size()][2][PlayerActions.values().length];
        for(int i=0; i<players.size(); i++){
            for(int j=0; j<2; j++){
                for(int k=0; k<PlayerActions.values().length; k++){
                    playerModels[i][j][k] = new playerModel();
                    tempModels[i][j][k] = new playerModel();
                }
            }
        }
    }

    public void savePlayerData(int player, boolean preFlop, PlayerActions action, double handStrength){
        tempModels[player][isPreFlop(preFlop)][action.ordinal()].totalHandStrength += handStrength;
        tempModels[player][isPreFlop(preFlop)][action.ordinal()].numberOfHands++;
        /*System.out.println("P"+player+" preFlop: "+preFlop+" action: "+action+" strength: "+handStrength+
                " totStrength: "+tempModels[player][isPreFlop(preFlop)][action.ordinal()].totalHandStrength+
                " numHands: "+tempModels[player][isPreFlop(preFlop)][action.ordinal()].numberOfHands); */
    }

    public void saveDataForShowdownPlayers(List<Player> playersInShowdown){
        for (Player player : playersInShowdown) {
            for (int i=0; i<2; i++){
                for(PlayerActions action : PlayerActions.values()){
                    playerModels[player.getId()][i][action.ordinal()].totalHandStrength += tempModels[player.getId()][i][action.ordinal()].totalHandStrength;
                    playerModels[player.getId()][i][action.ordinal()].numberOfHands += tempModels[player.getId()][i][action.ordinal()].numberOfHands;
                }
            }
        }
    }

    public void printPlayerModels(){
        for(int i=0; i<6; i++){
            for(int j=0; j<2; j++){
                for(int k=0; k<PlayerActions.values().length; k++){
                    System.out.println(
                            "P"+i+"preflop: "+j+
                            "totStrength: "+playerModels[i][j][k].totalHandStrength+
                            "numHands: "+playerModels[i][j][k].numberOfHands
                    );
                }
            }
        }
    }

    public double getPlayerData(int player, boolean preFlop, PlayerActions action){
        return playerModels[player][isPreFlop(preFlop)][action.ordinal()].totalHandStrength/playerModels[player][isPreFlop(preFlop)][action.ordinal()].numberOfHands;
    }

    private int isPreFlop(boolean preFlop){
        return preFlop ? 1:0;
    }
}
