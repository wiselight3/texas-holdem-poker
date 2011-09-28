import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 17.09.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class EquivalenceClassTable {

    private double[][][][] probs = new double[13][13][2][11];

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

    /**
     * For a given number of active players, this method calculates how often the probability for the given hole cards
     * is bigger, lower or equal to any other possible probability. Giving a percentage estimating how often the given
     * hole cards will win.
     *
     * @param holeCards
     * @param players
     * @return
     */
    public double calcPreflopProbabilityStrength(List<Card> holeCards, int players){
        double myProb = getProb(holeCards, players);
        int bigger=0, lower=0, tie=0;
        for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				for (int j2 = 0; j2 < 2; j2++) {
					double temp = probs[i][j][j2][players];
                    if(temp==0)
                        continue;
                    else{
                        if(myProb<temp)
                            lower++;
                        else if(myProb>temp)
                            bigger++;
                        else
                            tie++;
                    }
				}
			}
		}
        int teller = bigger + (tie/2);
        int nevner = bigger + tie + lower;
        return (double)teller/(double)nevner;
    }

    //Writes the roll out data to a file
    public void saveProbEquivalenceClassToFile() throws IOException {
    	File f = new File("probs.txt");
    	
    	FileWriter fwriter = new FileWriter(f);
    	BufferedWriter writer = new BufferedWriter(fwriter);
    	writer.write("Cards " + "Suited " + "Num pla. " + "Prob" +"\n");
    	for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				for (int j2 = 0; j2 < 2; j2++) {
					for (int k = 0; k < 11; k++) {
						writer.write(i + " " + j + " " + j2 + " " + k + " " + probs[i][j][j2][k] + "\n");
					}
				}
			}
		}
    	writer.close();
    }

    //Reads the roll out data from file
    public void readProbEquivalenceClassFromFile() throws IOException{
        File f = new File("probs.txt");

    	FileReader fReader = new FileReader(f);
    	BufferedReader reader = new BufferedReader(fReader);
        reader.readLine();
    	for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				for (int j2 = 0; j2 < 2; j2++) {
					for (int k = 0; k < 11; k++) {
                        String[] s = reader.readLine().split(" ");
                        probs[Integer.parseInt(s[0])][Integer.parseInt(s[1])][Integer.parseInt(s[2])][Integer.parseInt(s[3])] = Double.parseDouble(s[4]);
					}
				}
			}
		}
    	reader.close();
    }
}
