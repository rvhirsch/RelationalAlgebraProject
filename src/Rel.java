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
     * Count rows per group
     */
    public void count() {

    }

    /**
     * Cartesian/cross product
     */
    public void crossProd() {

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Rel)) return false;

        Rel rel = (Rel) other;

        for (int i=0; i<rel.relation.size(); i++) {
            if (!rel.relation.get(i).equals(((Rel) other).relation.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Full outer natural join
     */
    public void fullNatJoin() {

    }

    /**
     * Returns relation name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Insert tuple into table
     */
    public void insert(Tup tup) throws IllegalInsertException {
        if (this.relation.size() == 0) {    // if first element in table
            relation.add(tup);
        }
        else if (!this.relation.get(0).getColNames().equals(tup.getColNames())) {
            throw new IllegalInsertException("IllegalInsertException: Non-matching Column Types");
        }
    }

    /**
     * Returns common columns from 2 sets
     */
    public Rel intersection(Rel other) throws IllegalInsertException {
        ArrayList<String> thisList = this.relation.get(0).getColNames();
        ArrayList<String> otherList = other.relation.get(0).getColNames();

        ArrayList<String> colNames = new ArrayList<String>();

        for (int i=0; i<thisList.size(); i++) {
            if (otherList.contains(thisList.get(i))) {
                colNames.add(thisList.get(i));
            }
        }

        Rel rel = new Rel("Intersection Relation");
        if (colNames.size() == 0) {  // if no intersection
            return rel;
        }

        // else - intersection exists

        Rel thisRel = this.proj(colNames);
        Rel otherRel = this.proj(colNames);

        System.out.println("\nTHIS REL:");
        System.out.println(thisRel.toString());

        System.out.println("OTHER REL:");
        System.out.println(otherRel.toString());

        System.out.println(thisRel.relation.get(0).equals(otherRel.relation.get(0)));

        Tup t;
        int index;
        for (int i=0; i<otherRel.relation.size(); i++) {
            t = otherRel.relation.get(i);
            index = thisRel.relation.indexOf(t);
            if (index > -1) {
                try {
                    rel.insert(t);
                } catch (IllegalInsertException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return rel;
    }

    /**
     * Left outer natural join
     */
    public void leftNatJoin() {

    }

    /**
     * Get maximum per group
     */
    public void max(String colName) {

    }

    /**
     * Get minimum per group
     */
    public void min(String colName) {

    }

    /**
     * Natural join
     */
    public void natJoin() {

    }

    /**
     * Prints table in new window
     * Code mostly taken from: http://www.java2s.com/Tutorial/Java/0240__Swing/PrintaJTableout.htm
     */
    public void printTable() {
        if (this.relation.size() == 0) {    // if empty relation
            return;
        }

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
     * Selects certain columns
     */
    public Rel proj(ArrayList<String> colNames) {
        ArrayList<Integer> cols = new ArrayList<Integer>();

        for (int i=0; i<this.relation.get(0).getColNames().size(); i++) {
            if (colNames.contains(this.relation.get(0).getColNames().get(i))) {
                cols.add(i);
            }
        }

        Rel newRel = new Rel("Projection Relation");
        Tup newTup = new Tup();

        for (int i=0; i<this.relation.size(); i++) {
            for (int j=0; j<cols.size(); j++) {
                newTup.addAttr(this.relation.get(i).getAtPos(j));
            }
            try {
                newRel.insert(newTup);
            } catch (IllegalInsertException e) {
                System.out.println(e.getMessage());
            }
            newTup = new Tup();     // clear tuple values
        }

        for (int i=0; i<this.relation.size(); i++) {
            this.relation.get(i).cats = colNames;
        }

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
     * Returns relation name
     */
    public void renameTable(String newName) {
        this.name = newName;
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
     * Prints relation
     */
    public String toString() {
        if (this.relation.size() == 0) {
            return "[]";
        }

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
    public Rel union(Rel other) throws ColumnNameException {
        // check if relations have same column names
        if (this.relation.get(0).getColNames().equals(other.relation.get(0).getColNames())) {
            Rel newRel = new Rel("Union Relation");

            for (int i=0; i<this.relation.size(); i++) {
               try {
                    newRel.insert(this.relation.get(i));
                } catch (IllegalInsertException e) {
                    System.out.println(e.getMessage());
                }
            }

            for (int i=0; i<other.relation.size(); i++) {
                try {
                    newRel.insert(other.relation.get(i));
                } catch (IllegalInsertException e) {
                    System.out.println(e.getMessage());
                }
            }

            return newRel;
        }
        else {
            throw new ColumnNameException("ColumnNameException: These relations have different column names");

//            return null;
        }
    }

    /**
     * Updates variable value
     */
    public void update() {

    }

    public static void main(String[] args) {
        /*
        SET UP RELATION 1
         */
        Attr a1 = new Attr(5, "integers");
        Attr a2 = new Attr(6.2, "doubles");
        Attr a3 = new Attr("thing3", "strings");
        Attr a7 = new Attr(5, "other ints");

        Tup tuple = new Tup();
        tuple.addAttr(a1);
        tuple.addAttr(a2);
        tuple.addAttr(a3);
        tuple.addAttr(a7);

        Attr a4 = new Attr(12, "integers");
        Attr a5 = new Attr(17.5, "doubles");
        Attr a6 = new Attr("thing6", "strings");
        Attr a8 = new Attr(18, "other ints");

        Tup tuple2 = new Tup();
        tuple2.addAttr(a4);
        tuple2.addAttr(a5);
        tuple2.addAttr(a6);
        tuple2.addAttr(a8);

        Rel relation = new Rel("Rel1");
        try {
            relation.insert(tuple);
            relation.insert(tuple2);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        /*
        SET UP RELATION 2 - same column headers as Rel1
         */
        Attr a9 = new Attr(19, "integers");
        Attr a10 = new Attr(60.25, "doubles");
        Attr a11 = new Attr("yet another", "strings");
        Attr a12 = new Attr(57, "other ints");

        Tup tuple3 = new Tup();
        tuple3.addAttr(a9);
        tuple3.addAttr(a10);
        tuple3.addAttr(a11);
        tuple3.addAttr(a12);

        Attr a13 = new Attr(19, "integers");
        Attr a14 = new Attr(60.25, "doubles");
        Attr a15 = new Attr("ugh stuff", "strings");
        Attr a16 = new Attr(81, "other ints");

        Tup tuple4 = new Tup();
        tuple4.addAttr(a13);
        tuple4.addAttr(a14);
        tuple4.addAttr(a15);
        tuple4.addAttr(a16);

        Rel relation2 = new Rel("Rel2");
        try {
            relation2.insert(tuple3);
            relation2.insert(tuple4);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

         /*
        SET UP RELATION 3 - some column headers same as Rel1
         */
        Attr a17 = new Attr(19, "integers");
        Attr a18 = new Attr(60.25, "doubles");
        Attr a19 = new Attr(71, "yikes");
        Attr a20 = new Attr("lol test", "such test");

        Tup tuple5 = new Tup();
        tuple5.addAttr(a17);
        tuple5.addAttr(a18);
        tuple5.addAttr(a19);
        tuple5.addAttr(a20);

        Attr a21 = new Attr(128, "integers");
        Attr a22 = new Attr(175.29, "doubles");
        Attr a23 = new Attr(7777, "yikes");
        Attr a24 = new Attr("mrgh", "such test");

        Tup tuple6 = new Tup();
        tuple6.addAttr(a21);
        tuple6.addAttr(a22);
        tuple6.addAttr(a23);
        tuple6.addAttr(a24);

        Rel relation3 = new Rel("Rel2");
        try {
            relation3.insert(tuple5);
            relation3.insert(tuple6);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("RELATION 1:");
        System.out.println(relation.toString());

        System.out.println("RELATION 2:");
        System.out.println(relation2.toString());

        System.out.println("RELATION 3:");
        System.out.println(relation3.toString());

//        relation.printTable();

        /*
        testing union
         */
        try {
            relation = relation.union(relation2);
        } catch (ColumnNameException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(relation.toString());
        relation.printTable();

        /*
        testing projection
         */
//        ArrayList<String> projOn = new ArrayList<String>();
//        projOn.add("integers");
//        projOn.add("strings");
//        projOn.add("doubles");
//        relation = relation.proj(projOn);

//        System.out.println(relation.toString());
//        relation.printTable();

        /*
        testing intersection
         */
//        try {
//            relation2 = relation2.intersection(relation3);
//        } catch (IllegalInsertException e) {
//            System.out.println(e.getMessage());
//        }
//        System.out.println();
//        System.out.println(relation2.toString());
//        relation2.printTable();
    }
}
