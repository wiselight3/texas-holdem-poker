import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 17.09.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class EquivalenceClassTable {

    private double[][][][] probs = new double[14][14][2][10];

    public void saveProb(List<Card> holeCards, int players, double prob){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal();
        int card2 = holeCards.get(1).getValue().ordinal();
        int suited = isSuited(holeCards);
        probs[card1][card2][suited][players] = prob;
    }

    public double getProb(List<Card> holeCards, int players){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal();
        int card2 = holeCards.get(1).getValue().ordinal();
        int suited = isSuited(holeCards);
        return probs[card1][card2][suited][players];
    }

    private int isSuited(List<Card> holeCards) {
        if(holeCards.get(0).getSuit()==holeCards.get(1).getSuit())
            return 1;
        else
            return 0;
    }
    
    public void saveProbEquivalenceClassToFile() throws IOException {
    	File f = new File("probs.txt");
    	FileWriter fwriter = new FileWriter(f);
    	BufferedWriter writer = new BufferedWriter(fwriter);
    	writer.write("Cards " + " Prob" + "\n");
    	for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				for (int j2 = 0; j2 < 2; j2++) {
					for (int k = 0; k < 10; k++) {
						writer.write(i + " " + j + " "  + probs[i][j][j2][k] + "\n" );
					}
				}
			}
		}
    	writer.close();
    }

}
