/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 04.11.11
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class Pair {

    private int id;
    private String t, h, entailment;

    public Pair(int id, String t, String h, String entailment) {
        this.id = id;
        this.t = t;
        this.h = h;
        this.entailment = entailment;
    }

    public int getId() {
        return id;
    }

    public String getT() {
        return t;
    }

    public String getH() {
        return h;
    }

    public String getEntailment() {
        return entailment;
    }

    public String toString() {
        return "Pair{" +
                "id=" + id +
                ", t='" + t + '\'' +
                ", h='" + h + '\'' +
                ", entailment='" + entailment + '\'' +
                '}';
    }
}

