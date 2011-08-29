
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 25.08.11
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
public class Deck {

    private ArrayList<Card> cards = new ArrayList<Card>();

    public Deck (){
        buildDeck();
    }

    public void buildDeck(){
        cards.clear();
        for(Card.Suit suit : Card.Suit.values()){
            for(Card.Value value : Card.Value.values()){
                cards.add(new Card(value, suit));
            }
        }
    }

    public void shuffleDeck (){
        Card temp;
        int tempIndex;
        for (int i=0; i<cards.size(); i++){
            tempIndex = ((int) (i * Math.random()));
            if (tempIndex != i){
                temp = cards.get(i);
                cards.set(i, cards.get(tempIndex));
                cards.set(tempIndex, temp);
            }
        }
    }

    public Card dealCard (){
        if(!cards.isEmpty())
            return cards.remove(0);
        else
            System.out.println("No more cards in deck!");
        return null;
    }

    public int cardsLeft (){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    //Testing the main Deck functionality
    public static void main(String args[]){
        Deck deck = new Deck();
        System.out.println(deck.cardsLeft());
        for(int i=0; i<52; i++){
            System.out.println(deck.dealCard());
        }
        deck.buildDeck();
        deck.shuffleDeck();
        Collections.sort(deck.getCards());
        Collections.reverse(deck.getCards());
        for(int i=0; i<52; i++){
            System.out.println(deck.dealCard());
        }
    }
}
