import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by rvhirsch on 1/20/17.
 */
public class Tup {
    ArrayList<Attr> list;

    public Tup() {
        this.list = new ArrayList<Attr>();
    }

    private void addAttr(Attr attr) {
        this.list.add(attr);
    }

    private int getLength() {
        return this.list.size();
    }

    private Attr getAtPos(int pos) {
        return this.list.get(pos);
    }

    private Pair getValAtPos(int pos) {
        return this.list.get(pos).getValue();
    }

    private int getTypeAtPos(int pos) {
        return this.list.get(pos).getType();
    }

    private Attr setAtPos(int pos, Attr attr) {
        return this.list.set(pos, attr);
    }

    public String toString() {
        return this.list.toString();
    }

    public static void main(String[] args) {
        Attr a1 = new Attr(5);
        Attr a2 = new Attr(6.2);
        Attr a3 = new Attr("thing");

        System.out.println("INITIAL TEST");
        System.out.println("A1: " + a1.toString());
        System.out.println("A2: " + a2.toString());
        System.out.println("A3: " + a3.toString());

        //create list of these tuples
        Tup tuple = new Tup();
        tuple.addAttr(a1);
        tuple.addAttr(a2);
        tuple.addAttr(a3);

        System.out.println("\nRow: " + tuple.toString());
        System.out.println("Length: " + tuple.getLength());
        System.out.println("Pos 0: " + tuple.getAtPos(0));
        System.out.println("Val @ Pos 0: " + tuple.getValAtPos(0));
        System.out.println("Type @ Pos 0: " + tuple.getTypeAtPos(0));

        Attr a4 = new Attr("new stuff");
        tuple.setAtPos(2, a4);
        System.out.println("\nRow After Update: " + tuple.toString());
    }
}
