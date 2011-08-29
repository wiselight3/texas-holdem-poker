

import com.sun.org.apache.bcel.internal.classfile.InnerClass;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 26.08.11
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class CardRating {

    private Comparator<Card> suitComparator = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getSuit().compareTo(o2.getSuit());
        }
    };

    private Comparator<Card> valueComparator = new Comparator<Card>() {
        public int compare(Card o1, Card o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    private Comparator<ArrayList<Card>> lengthComparator = new Comparator<ArrayList<Card>>() {
        public int compare(ArrayList<Card> o1, ArrayList<Card> o2) {
            if(o1.size()<o2.size())
                return 1;
            else if(o2.size()>o1.size())
                return -1;
            else
                return 0;
        }
    };

    //Sort by suit
    public ArrayList<Card> sortBySuit(ArrayList<Card> cards){
        Collections.sort(cards, suitComparator);
        return cards;
    }

    //Sort by value
    public ArrayList<Card> sortByValue(ArrayList<Card> cards){
        Collections.sort(cards, valueComparator);
        return cards;
    }

    //Sort by suit and value
    public ArrayList<Card> sortBySuitAndValue(ArrayList<Card> cards){
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
    public ArrayList genValueGroups (ArrayList<Card> cards){
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
    public ArrayList genSuitGroups (ArrayList<Card> cards){
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
    public ArrayList orderByLength(ArrayList vGroups){
        Collections.sort(vGroups, lengthComparator);
        return vGroups;
    }

    //Returns Card if it exists in list
    public Card findCardOfValue(Card.Value value, ArrayList<Card> cards){
        for(Card card : cards){
            if(card.getValue().equals(value))
                return card;
        }
        return null;
    }


    public int[] calcCardsPower (ArrayList<Card> cards){
        ArrayList vGroups = genValueGroups(cards);


        return null;
    }

    public ArrayList<Card> findFlush (ArrayList<Card> cards){
        ArrayList<ArrayList<Card>> sGroups = genSuitGroups(cards);
        if(sGroups.get(0).size()>=5)
            return sGroups.get(0);
        return null;
    }

    private Card ace;
    public ArrayList<Card> findStraight (ArrayList<Card> cards){
        ace = findCardOfValue(Card.Value.ACE, cards);
        ArrayList<Card> sCards = sortByValue(cards);
        Collections.reverse(sCards);

        ArrayList<Card> straight = new ArrayList<Card>();
        straight.add(sCards.get(0));
        return scanForStraight(sCards, straight);
    }

    public ArrayList<Card> scanForStraight (ArrayList<Card> cards, ArrayList<Card> straight){
        if(straight.size() == 5){
            return straight;
        }else if(ace != null && straight.get(0).getValue().equals(Card.Value.DEUCE) && straight.size()==4){
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(ace);
            temp.addAll(straight);
            return temp;
        }
        else if(cards == null)
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

    public int[] calcStraightFlushPower (ArrayList<Card> cards){
        return null;
    }

    //Testing of sorting methods
    public static void main(String args[]){
        Deck deck = new Deck();
        CardRating cardRating = new CardRating();
        /*
        deck.shuffleDeck();
        ArrayList<Card> valueSort = cardRating.sortByValue(deck.getCards());
        System.out.println("SortedByValue:");
        for(Card card : valueSort){
            System.out.println(card);
        }

        deck.buildDeck();
        deck.shuffleDeck();
        ArrayList<Card> suitSort = cardRating.sortBySuit(deck.getCards());
        System.out.println("SortedBySuit:");
        for(Card card : suitSort){
            System.out.println(card);
        }

        deck.buildDeck();
        deck.shuffleDeck();
        ArrayList<Card> suitAndValueSort = cardRating.sortBySuitAndValue(deck.getCards());
        System.out.println("SortedBySuitAndValue:");
        for(Card card : suitAndValueSort){
            System.out.println(card);
        }
        */

        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Value.ACE, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Value.DEUCE, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Value.THREE, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Value.KING, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Value.JACK, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Value.TEN, Card.Suit.HEARTS));
        testHand.add(new Card(Card.Value.QUEEN, Card.Suit.SPADES));

        /*
        for(Card card : cardRating.sortBySuit(testHand)){
            System.out.println(card);
        }
        System.out.println();
        for(Card card : cardRating.sortByValue(testHand)){
            System.out.println(card);
        }
        System.out.println();
        for(Card card : cardRating.sortBySuitAndValue(testHand)){
            System.out.println(card);
        }
        System.out.println();
        ArrayList<ArrayList<Card>> vGroups = cardRating.genValueGroups(testHand);
        for(ArrayList<Card> vGroup : vGroups){
            for(Card card : vGroup){
                System.out.println(card);
            }
        }
        System.out.println();
        ArrayList<ArrayList<Card>> sGroups = cardRating.genSuitGroups(testHand);
        for(ArrayList<Card> sGroup : sGroups){
            for(Card card : sGroup){
                System.out.println(card);
            }
        }
        System.out.println();
        ArrayList<Card> flush = cardRating.findFlush(testHand);
        for(Card card : flush){
            System.out.println(card);
        }  */
        System.out.println();
        ArrayList<Card> straight = cardRating.findStraight(testHand);
        if(straight != null){
            for(Card card : straight){
                System.out.println(card);
            }
        }
    }
}
