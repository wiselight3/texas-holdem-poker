import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 17.09.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class EquivalenceClassTable {

    private static double[][][][] probs = new double[13][13][2][11];

    public static void saveProb(List<Card> holeCards, int players, double prob){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal();
        int card2 = holeCards.get(1).getValue().ordinal();
        int suited = isSuited(holeCards);
        probs[card1][card2][suited][players] = prob;
    }

    public static double getProb(List<Card> holeCards, int players){
        Collections.sort(holeCards);
        int card1 = holeCards.get(0).getValue().ordinal();
        int card2 = holeCards.get(1).getValue().ordinal();
        int suited = isSuited(holeCards);
        return probs[card1][card2][suited][players];
    }

    private static int isSuited(List<Card> holeCards) {
        if(holeCards.get(0).getSuit()==holeCards.get(1).getSuit())
            return 1;
        else
            return 0;
    }
    
    public static void saveProbEquivalenceClassToFile() throws IOException {
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

    public static void readProbEquivalenceClassFromFile() throws IOException{
        File f = new File("probs.txt");

    	FileReader fReader = new FileReader(f);
    	BufferedReader reader = new BufferedReader(fReader);
        reader.readLine();
    	for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				for (int j2 = 0; j2 < 2; j2++) {
					for (int k = 0; k < 10; k++) {
                        String[] s = reader.readLine().split(" ");
                        probs[Integer.parseInt(s[0])][Integer.parseInt(s[1])][Integer.parseInt(s[2])][Integer.parseInt(s[3])] = Double.parseDouble(s[4]);
					}
				}
			}
		}
    	reader.close();
    }

    public static void main (String[] args){
        EquivalenceClassTable ect = new EquivalenceClassTable();
        try {
            ect.readProbEquivalenceClassFromFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<Card> testCards = new ArrayList<Card>();
        testCards.add(new Card(Card.Value.JACK, Card.Suit.CLUBS));
        testCards.add(new Card(Card.Value.JACK, Card.Suit.DIAMONDS));
        System.out.println(ect.getProb(testCards, 2));
    }

}
