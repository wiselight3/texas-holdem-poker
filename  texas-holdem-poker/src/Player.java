import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 26.08.11
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    private String id;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Player (String id){
        this.id = id;
    }

    public void dealHand(ArrayList<Card> cards){
        this.cards = cards;
    }



}
