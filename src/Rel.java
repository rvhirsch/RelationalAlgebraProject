import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Rel {
    ArrayList<Tup> relation;
    String name;

    public Rel(String relName) {
        this.relation = new ArrayList<Tup>();
        this.name = relName;
    }

    /**
     * Get average per group
     */
    public void average() {

    }

    /**
     * Insert tuple into table
     */
    public void insert(Tup tup) {
        relation.add(tup);
    }

    /**
     * Count rows per group
     */
    public void count() {

    }

    /**
     * Cartesian/cross product
     */
    public void crossProd() {

    }

    /**
     * Full outer natural join
     */
    public void fullNatJoin() {

    }

    /**
     * Returns common columns from 2 sets
     */
    public void intersection() {

    }

    /**
     * Left outer natural join
     */
    public void leftNatJoin() {

    }

    /**
     * Get maximum per group
     */
    public void max() {

    }

    /**
     * Get minimum per group
     */
    public void min() {

    }

    /**
     * Natural join
     */
    public void natJoin() {

    }

    /**
     * Selects certain columns
     */
    public Rel proj(ArrayList<String> colNames) {
        ArrayList<Integer> cols = new ArrayList<Integer>();

        for (int i=0; i<this.relation.get(0).getColNames().size(); i++) {
            if (colNames.contains(this.relation.get(0).getColNames().get(i))) {
                cols.add(i);
            }
        }

        Rel newRel = new Rel("Relation Projection");
        Tup newTup = new Tup();

        for (int i=0; i<this.relation.size(); i++) {
            for (int j=0; j<cols.size(); j++) {
                newTup.addAttr(this.relation.get(i).getAtPos(j));
            }
            newRel.insert(newTup);
            newTup = new Tup();     // clear tuple values
        }

//        Rel newRel = new Rel("new relation");
//        Tup newTup = new Tup();
//
//        String col;
//        for (int i=0; i<this.relation.size(); i++) {
//            col = this.relation.get(0).getColNames().get(i);
//            for (int j=0; j<this.relation.get(0).getLength(); j++) {
//                if (colNames.contains(col)) {
//                    newTup.addAttr(this.relation.get(i).getAtPos(j));
//                }
//            }
//            newTup = new Tup();
//            if (newTup.getLength() > 0) {
//                newRel.insert(newTup);
//            }
//        }
//
////        System.out.println(newRel.toString());
//
        return newRel;
    }

    /**
     * Remove tuple from table
     */
    public void remove() {

    }

    /**
     * Renames column
     */
    public void rename(String newName, int col) {
        for (int i=0; i<this.relation.size(); i++) {
            this.relation.get(i).updateCatName(newName, col);
        }
    }

    /**
     * Right outer natural join
     */
    public void rightNatJoin() {

    }

    /**
     * Selects certain rows
     */
    public void select() {

    }

    /**
     * Semijoin (???????)
     */
    public void semiJoin() {

    }

    /**
     * Get sum of group
     */
    public void sum() {

    }

    /**
     * Prints table in new window
     * Code mostly taken from: http://www.java2s.com/Tutorial/Java/0240__Swing/PrintaJTableout.htm
     */
    public void printTable() {
        Object[][] objs = new Object[this.relation.size()][this.relation.get(0).getLength()];
        for (int i=0; i<objs.length; i++) {
            for (int j=0; j<objs[0].length; j++) {
                objs[i][j] = this.relation.get(i).getValAtPos(j);
            }
        }

        JTable table = new JTable(objs, this.relation.get(0).getColNames().toArray());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel(new GridLayout(2, 0));
        jPanel.setOpaque(true);

        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        jPanel.add(new JScrollPane(table));

        frame.add(jPanel);
        frame.pack();
        frame.setVisible(true);

//        try {
//            table.print();
//        } catch(PrinterException e) {
//            System.out.println(e.getMessage());
//        }
    }

    /**
     * Prints relation
     */
    public String toString() {
        String str = "";

        str += Arrays.toString(this.relation.get(0).getColNames().toArray());    // column headers
        str += "\n";

        for (int i=0; i<this.relation.size(); i++) {
            str += this.relation.get(i).toString();      // db info
            str += "\n";
        }

        return str;
    }

    /**
     * Puts 2 tables together vertically
     * Must have same col names
     */
    public void union() {

    }

    /**
     * Updates variable value
     */
    public void update() {

    }

    public static void main(String[] args) {
        Attr a1 = new Attr(5, "integers");
        Attr a2 = new Attr(6.2, "doubles");
        Attr a3 = new Attr("thing3", "strings");
        Attr a7 = new Attr(5, "other ints");

        Attr a4 = new Attr(12, "integers");
        Attr a5 = new Attr(17.5, "doubles");
        Attr a6 = new Attr("thing6", "strings");
        Attr a8 = new Attr(18, "other ints");

        Tup tuple = new Tup();
        tuple.addAttr(a1);
        tuple.addAttr(a2);
        tuple.addAttr(a3);
        tuple.addAttr(a7);

        Tup tuple2 = new Tup();
        tuple2.addAttr(a4);
        tuple2.addAttr(a5);
        tuple2.addAttr(a6);
        tuple2.addAttr(a8);

        Rel relation = new Rel("Rel1");
        relation.insert(tuple);
        relation.insert(tuple2);

        System.out.println(relation.toString());
//        relation.printTable();

        ArrayList<String> projOn = new ArrayList<String>();
        projOn.add("integers");
        projOn.add("strings");
        projOn.add("doubles");
        relation = relation.proj(projOn);

//        System.out.println(relation.toString());
        relation.printTable();

    }
}
