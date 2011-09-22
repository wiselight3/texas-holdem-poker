import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 17.09.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class EquivalenceClassTable {

    private double[][][][] probs = new double[14][14][2][10];

    public void saveProb(ArrayList<Card> holeCards, int players, double prob){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal()-2;
        int card2 = holeCards.get(1).getValue().ordinal()-2;
        int suited = isSuited(holeCards);
        probs[card1][card2][suited][players] = prob;
    }

    public double getProb(ArrayList<Card> holeCards, int players){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal()-2;
        int card2 = holeCards.get(1).getValue().ordinal()-2;
        int suited = isSuited(holeCards);
        return probs[card1][card2][suited][players];
    }

    private int isSuited(ArrayList<Card> holeCards) {
        if(holeCards.get(0).getSuit()==holeCards.get(1).getSuit())
            return 1;
        else
            return 0;
    }

}
