import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rvhirsch on 1/20/17.
 */
public class Tup {
    ArrayList<Attr> list;
    ArrayList<String> cats;

    public Tup() {
        this.list = new ArrayList<Attr>();
        this.cats = new ArrayList<String>();
    }

    public void addAttr(Attr attr) {
        this.list.add(attr);
        this.cats.add(attr.getColumnName());
    }

    public int getLength() {
        return this.list.size();
    }

    public Attr getAtPos(int pos) {
        return this.list.get(pos);
    }

    public Pair getValAtPos(int pos) {
        return this.list.get(pos).getValue();
    }

    public int getTypeAtPos(int pos) {
        return this.list.get(pos).getType();
    }

    public Attr setAtPos(int pos, Attr attr) {
        return this.list.set(pos, attr);
    }

    public void updateCatName(String newName, int col) {
        this.cats.set(col, newName);
        this.list.set(col, this.list.get(col).setColumnName(newName));
    }

    public ArrayList<String> getColNames() {
        return this.cats;
    }

    public String toString() {
        return this.list.toString();
    }

    public static void main(String[] args) {
        Attr a1 = new Attr(5, "INTS");
        Attr a2 = new Attr(6.2, "DOUBLES");
        Attr a3 = new Attr("thing", "STRINGS");

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

        Attr a4 = new Attr("new stuff", "STRINGS");
        tuple.setAtPos(2, a4);
        System.out.println("\nRow After Update: " + tuple.toString());

        System.out.println(Arrays.toString(tuple.getColNames().toArray()));

        tuple.updateCatName("STRS", 2);
        System.out.println("AFTER UPDATE");
        System.out.println(Arrays.toString(tuple.getColNames().toArray()));
    }
}
