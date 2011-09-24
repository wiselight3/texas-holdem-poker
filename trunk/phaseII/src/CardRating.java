





import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 26.08.11
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class CardRating {

    public Comparator<Card> suitComparator = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getSuit().compareTo(o2.getSuit());
        }
    };

    public Comparator<Card> valueComparator = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    public Comparator<ArrayList<Card>> lengthComparator = new Comparator<ArrayList<Card>>() {
        public int compare(ArrayList<Card> o1, ArrayList<Card> o2) {
            if(o1.size()<o2.size())
                return 1;
            else if(o1.size()>o2.size())
                return -1;
            else if(o1.get(0).getValue().ordinal()<o2.get(0).getValue().ordinal())
                return 1;
            else if(o1.get(0).getValue().ordinal()>o2.get(0).getValue().ordinal())
                return -1;
            else
                return 0;
        }
    };

    //Sort by suit
    private ArrayList<Card> sortBySuit(ArrayList<Card> cards){
        Collections.sort(cards, suitComparator);
        return cards;
    }

    //Sort by value
    private ArrayList<Card> sortByValue(ArrayList<Card> cards){
        Collections.sort(cards, valueComparator);
        return cards;
    }

    //Sort by suit and value
    private ArrayList<Card> sortBySuitAndValue(ArrayList<Card> cards){
        Collections.sort(cards, suitComparator);
        ArrayList<Card> sorted = new ArrayList<Card>();
        ArrayList<Card> suitCol = new ArrayList<Card>();
        for(Card card : cards){
            if(suitCol.isEmpty())
                suitCol.add(card);
            else{
                if(suitCol.get(suitCol.size()-1).getSuit().equals(card.getSuit()))
                    suitCol.add(card);
                else{
                    sorted.addAll(sortByValue(suitCol));
                    suitCol.clear();
                    suitCol.add(card);
                }
            }
        }
        sorted.addAll(sortByValue(suitCol));
        return sorted;
    }

    //Return sets of cards ordered by value
    private ArrayList genValueGroups (ArrayList<Card> cards){
        cards = sortByValue(cards);
        ArrayList<ArrayList<Card>> valGroups = new ArrayList<ArrayList<Card>>();

        ArrayList<Card> tempValGroup = new ArrayList<Card>();
        valGroups.add(tempValGroup);
        int i=0;
        for(Card card : cards){
            if(valGroups.get(i).isEmpty())
                tempValGroup.add(card);
            else{
                if(valGroups.get(i).get(0).getValue().equals(card.getValue()))
                    tempValGroup.add(card);
                else{
                    tempValGroup = new ArrayList<Card>();
                    valGroups.add(tempValGroup);
                    i++;
                    tempValGroup.add(card);
                }
            }
        }
        return orderByLength(valGroups);
    }

    //Return sets of cards ordered by suit
    private ArrayList genSuitGroups (ArrayList<Card> cards){
        cards = sortBySuit(cards);
        ArrayList<ArrayList<Card>> suitGroups = new ArrayList<ArrayList<Card>>();

        ArrayList<Card> tempSuitGroup = new ArrayList<Card>();
        suitGroups.add(tempSuitGroup);
        int i=0;
        for(Card card : cards){
            if(suitGroups.get(i).isEmpty())
                tempSuitGroup.add(card);
            else{
                if(suitGroups.get(i).get(0).getSuit().equals(card.getSuit()))
                    tempSuitGroup.add(card);
                else{
                    tempSuitGroup = new ArrayList<Card>();
                    suitGroups.add(tempSuitGroup);
                    i++;
                    tempSuitGroup.add(card);
                }
            }
        }
        return orderByLength(suitGroups);
    }

    //Order sets by length
    private ArrayList orderByLength(ArrayList vGroups){
        Collections.sort(vGroups, lengthComparator);
        return vGroups;
    }

    //Returns Card if it exists in list
    private Card findCardOfValue(Card.Value value, ArrayList<Card> cards){
        for(Card card : cards){
            if(card.getValue().equals(value))
                return card;
        }
        return null;
    }


    public int[] calcCardsPower (ArrayList<Card> cards){
        ArrayList<ArrayList<Card>> vGroups = genValueGroups(cards);
        ArrayList<Card> flush = findFlush(cards);
        ArrayList<Card> strInFlush = null;
        if(flush != null)
            strInFlush = findStraight(flush);
        if(flush!=null && strInFlush!=null)
            return calcStraightFlushPower(strInFlush);
        else if(vGroups.get(0).size() == 4)
            return calcFourOfAKindPower(vGroups);
        else if(vGroups.get(0).size() == 3 && vGroups.size()>1 && vGroups.get(1).size()>=2)
            return calcFullHousePower(vGroups);
        else if(flush != null)
            return calcSimpleFlushPower(flush);
        else{
            ArrayList<Card> straight = findStraight(cards);
            if(straight != null)
                return calcStraightPower(straight);
            else if(vGroups.get(0).size() == 3)
                return calcThreeOfAKindPower(vGroups);
            else if(vGroups.get(0).size() == 2){
                if(vGroups.size()>1 && vGroups.get(1).size()==2)
                    return calcTwoPairsPower(vGroups);
                else
                    return calcPairPower(vGroups);
            }else
                 return calcHighCardPower(vGroups);
        }
    }

    private ArrayList<Card> findFlush (ArrayList<Card> cards){
        ArrayList<ArrayList<Card>> sGroups = genSuitGroups(cards);
        if(sGroups.get(0).size()>=5)
            return sGroups.get(0);
        return null;
    }

    private Card ace;
    private ArrayList<Card> findStraight (ArrayList<Card> inCards){
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(inCards);
        ace = findCardOfValue(Card.Value.ACE, cards);
        ArrayList<Card> sCards = sortByValue(cards);
        Collections.reverse(sCards);

        ArrayList<Card> straight = new ArrayList<Card>();
        straight.add(sCards.get(0));
        return scanForStraight(sCards, straight);
    }

    private ArrayList<Card> scanForStraight (ArrayList<Card> cards, ArrayList<Card> straight){
        if(straight.size() == 5){
            return straight;
        }else if(ace != null && straight.get(0).getValue().equals(Card.Value.DEUCE) && straight.size()==4){
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(ace);
            temp.addAll(straight);
            return temp;
        }
        else if(cards.isEmpty())
            return null;

        Card card = cards.get(0);
        cards.remove(0);
        if(card.getValue().ordinal() == straight.get(0).getValue().ordinal()-1){
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(card);
            temp.addAll(straight);
            return scanForStraight(cards, temp);
        }else if(card.getValue().ordinal() == straight.get(0).getValue().ordinal()){
            return scanForStraight(cards, straight);
        }else{
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(card);
            return scanForStraight(cards, temp);
        }
    }

    private ArrayList<Card> findMaxValueKickers (ArrayList<ArrayList<Card>> vGroups, int startGroup){
        ArrayList<Card> possibleKickers = new ArrayList<Card>();
        for(int i=startGroup; i<vGroups.size(); i++){
            ArrayList<Card> vGroup = vGroups.get(i);
            possibleKickers.addAll(vGroup);
        }
        Collections.sort(possibleKickers, valueComparator);
        Collections.reverse(possibleKickers);
        return possibleKickers;
    }

    private int[] calcStraightFlushPower (ArrayList<Card> cards){
        int[] power = {9, cards.get(4).getValue().ordinal()+2};
        return power;
    }

    private int[] calcFourOfAKindPower (ArrayList<ArrayList<Card>> vGroups){
        ArrayList<Card> kickers = findMaxValueKickers(vGroups, 1);
        int[] power = {8, vGroups.get(0).get(0).getValue().ordinal()+2, kickers.get(0).getValue().ordinal()+2};
        return power;
    }

    private int[] calcFullHousePower (ArrayList<ArrayList<Card>> vGroups){
        int[] power = {7, vGroups.get(0).get(0).getValue().ordinal()+2, vGroups.get(1).get(0).getValue().ordinal()+2};
        return power;
    }

    private int[] calcSimpleFlushPower (ArrayList<Card> cards){
        int[] power = new int[6];
        power[0] = 6;
        Collections.sort(cards, valueComparator);
        Collections.reverse(cards);
        for(int i=1; i<=5; i++){
            power[i] = cards.get(i-1).getValue().ordinal()+2;
        }
        return power;
    }

    private int[] calcStraightPower (ArrayList<Card> cards){
        int[] power = {5, cards.get(4).getValue().ordinal()+2};
        return power;
    }

    private int[] calcThreeOfAKindPower (ArrayList<ArrayList<Card>> vGroups){
        ArrayList<Card> kickers = findMaxValueKickers(vGroups, 1);
        int[] power = {4, vGroups.get(0).get(0).getValue().ordinal()+2, kickers.get(0).getValue().ordinal()+2, kickers.get(1).getValue().ordinal()+2};
        return power;
    }

    private int[] calcTwoPairsPower (ArrayList<ArrayList<Card>> vGroups){
        ArrayList<Card> kickers = findMaxValueKickers(vGroups, 2);
        int[] power = {3, vGroups.get(0).get(0).getValue().ordinal()+2, vGroups.get(1).get(0).getValue().ordinal()+2, kickers.get(0).getValue().ordinal()+2};
        return power;
    }

    private int[] calcPairPower (ArrayList<ArrayList<Card>> vGroups){
        ArrayList<Card> kickers = findMaxValueKickers(vGroups, 1);
        int[] power = {2, vGroups.get(0).get(0).getValue().ordinal()+2, kickers.get(0).getValue().ordinal()+2, kickers.get(1).getValue().ordinal()+2, kickers.get(2).getValue().ordinal()+2};
        return power;
    }

    private int[] calcHighCardPower (ArrayList<ArrayList<Card>> vGroups){
        ArrayList<Card> kickers = findMaxValueKickers(vGroups, 0);
        int[] power = new int[6];
        power[0] = 1;
        for(int i=1; i<=5; i++){
            power[i] = kickers.get(i-1).getValue().ordinal()+2;
        }
        return power;
    }

    
    public double handStrength (ArrayList<Card> holeCards, List<Card> communityCards, int numbPlayers){
        Deck deck = new Deck();
        ArrayList<Card> plHand = new ArrayList<Card>();
        ArrayList<Card> opHand = new ArrayList<Card>();
        for(Card c : holeCards){
            deck.getSpecificCard(c);
        }
        for(Card c : communityCards){
            deck.getSpecificCard(c);
            plHand.add(c);
            opHand.add(c);
        }
        ArrayList<Card> remainingCards = deck.getCards();

        int[] plRank = calcCardsPower(plHand);

        int wins, losses, draws;
        for(int i=0; i<remainingCards.size(); i++){
            for(int j=i+1; j<remainingCards.size(); j++){
                opHand.add(remainingCards.get(i));
                opHand.add(remainingCards.get(j));

                int[] opRank = calcCardsPower(opHand);
                //if()
            }
        }
        return 0.0;
    }

    /**
	  *
	  * @param rating
	  * @param rating2
	  * @return integer: 1 if rating 1 is best, 0 if they are equal, and -1 if rating2 is best.
	  */
	 public static int calculateBestRating(int[] rating, int[] rating2) {
		 int toTraverse;
		 if (rating.length > rating2.length) toTraverse = rating2.length;
		 if (rating.length < rating2.length) toTraverse = rating.length;
		 else toTraverse = rating.length;


		 for (int i = 0; i < toTraverse; i++) {
			if (rating[i] > rating2[i]) return 1;
			if (rating2[i] > rating[i]) return -1;
			if (rating[i] == rating2[i] && toTraverse-i == 1) return 0;
		 }
		 return 0;
     }
    
    
    //Testing of sorting methods
    public static void main(String args[]){
        Deck deck = new Deck();
        CardRating cardRating = new CardRating();

        ArrayList<Card> testHole = new ArrayList<Card>();
        testHole.add(new Card(Card.Value.JACK, Card.Suit.CLUBS));
        testHole.add(new Card(Card.Value.JACK, Card.Suit.DIAMONDS));

        ArrayList<Card> testCommunity = new ArrayList<Card>();
        testCommunity.add(new Card(Card.Value.JACK, Card.Suit.HEARTS));
        testCommunity.add(new Card(Card.Value.JACK, Card.Suit.SPADES));
        testCommunity.add(new Card(Card.Value.QUEEN, Card.Suit.SPADES));

        cardRating.handStrength(testHole, testCommunity, 2);
    }
}
