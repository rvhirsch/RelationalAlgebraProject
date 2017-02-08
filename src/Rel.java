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
     * Get average per column
     */
    public Rel average(String colName) throws IllegalTypeException {
        if (this.relation.size() == 0) {
            return new Rel("Avg(" + colName + ")");
        }

        int index = this.relation.get(0).getColNames().indexOf(colName);
        Rel avgRel;
        Tup sumTup;
        Attr sumAttr;

        int type = this.relation.get(0).getAtPos(index).getType();

        if (type >= 2) {
            throw new IllegalTypeException("IllegalTypeException: Cannot compute average of this type");
        }
        else {
            avgRel = this.sum(colName);         // sum relation
            sumTup = avgRel.relation.get(0);    // tuple contained in sum relation
            sumAttr = sumTup.getAtPos(0);      // attr contained in tuple
            int sumInt;
            double sumDub;
            double avg;

            if (type == 0) {
                sumInt = (int) sumAttr.getValue().getKey();
                avg = (double) sumInt / this.relation.size();

                sumTup.setAtPos(0, new Attr(avg, "Average"));
                avgRel.relation.set(0, sumTup);     // update relation
                avgRel.renameTable("Avg(" + colName + ")");
            } else if (type == 1) {
                sumDub = (double) sumAttr.getValue().getKey();
                avg = sumDub / this.relation.size();

                sumTup.setAtPos(0, new Attr(avg, "Average"));   // avg = sum / size
                avgRel.relation.set(0, sumTup);     // update relation
                avgRel.renameTable("Avg(" + colName + ")");
            }

            return avgRel;
        }
    }

    /**
     * Count rows per column
     */
    public int count() {
        return this.relation.size();
    }

    /**
     * Cartesian/cross product
     */
    public Rel crossProd(Rel rel) {
        System.out.println("doing cross product now");
        Rel crossRel = new Rel("CrossProduct");
        Tup tup1, tup2, newTup;

        for (int i=0; i<this.relation.size(); i++) {
            tup1 = this.relation.get(i);
            for (int j=0; j<this.relation.size(); j++) {
                tup2 = rel.relation.get(j);

                newTup = makeFullTup(tup1, tup2);
                try {
                    crossRel.insert(newTup);
                } catch (IllegalInsertException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Problem in Tuple: " + newTup.toString() + "\n");
                }
            }
        }
        return crossRel;
    }

    /**
     * Returns tup 1 + tup 2
     */
    private Tup makeFullTup(Tup first, Tup second) {
        Tup tup = new Tup();
        Attr attr;

        for (int i=0; i<first.getLength(); i++) {
            attr = first.getAtPos(i);
            tup.addAttr(attr);
        }

        for (int i=0; i<second.getLength(); i++) {
            attr = second.getAtPos(i);
            tup.addAttr(attr);
        }

        return tup;
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
    public void fullNatJoin(Rel rel) {
        // TODO
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
            return;
        }
        else if (!this.relation.get(0).getColNames().equals(tup.getColNames())) {
            throw new IllegalInsertException("IllegalInsertException: Non-matching Column Types");
        }

        for (int i=0; i<tup.getLength(); i++) {
            if (this.relation.get(0).getValAtPos(i).getKey() == null) {
                continue;
            }
            else if (this.relation.get(0).getTypeAtPos(i) != tup.getTypeAtPos(i)) {
                throw new IllegalInsertException("IllegalInsertException: Non-matching Column Types");
            }
        }

        relation.add(tup);
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
    public void leftNatJoin(Rel rel) {
        // TODO
    }

    /**
     * Get maximum per group
     */
    public Rel max(String colName) throws IllegalTypeException {
        Attr maxAttr;
        Tup maxTup;
        Rel maxRel;

        if (this.relation.size() == 0) {    // if empty table
            return new Rel("Max(" + colName + ")");
        }

        int index = this.relation.get(0).getColNames().indexOf(colName);

        if (this.relation.get(0).getAtPos(index).getType() >= 2) {
            throw new IllegalTypeException("IllegalTypeException: Cannot find max of this type");
        }

        // else - type is double or int
        double max = Double.MIN_VALUE;
        double val;
        for (int i=0; i<this.relation.size(); i++) {
            val = (double) this.relation.get(i).getValAtPos(index).getKey();
            if (val > max) {
                max = val;
            }
        }

        maxAttr = new Attr(max, "Max");
        maxTup = new Tup();
        maxTup.addAttr(maxAttr);
        maxRel = new Rel("Max(" + colName + ")");
        try {
            maxRel.insert(maxTup);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        return maxRel;
    }

    /**
     * Get minimum per group
     */
    public Rel min(String colName) throws IllegalTypeException {
        Attr minAttr;
        Tup minTup;
        Rel minRel;

        if (this.relation.size() == 0) {    // if empty table
            return new Rel("Min(" + colName + ")");
        }

        int index = this.relation.get(0).getColNames().indexOf(colName);

        if (this.relation.get(0).getAtPos(index).getType() >= 2) {
            throw new IllegalTypeException("IllegalTypeException: Cannot find max of this type");
        }

        // else - type is double or int
        double min = Double.MAX_VALUE;
        double val;
        for (int i=0; i<this.relation.size(); i++) {
            val = (double) this.relation.get(i).getValAtPos(index).getKey();
            if (val < min) {
                min = val;
            }
        }

        minAttr = new Attr(min, "Min");
        minTup = new Tup();
        minTup.addAttr(minAttr);
        minRel = new Rel("Min(" + colName + ")");
        try {
            minRel.insert(minTup);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        return minRel;

    }

    /**
     * Natural join
     */
    public void natJoin(Rel rel) {
        // TODO
    }

    /**
     * Prints table in new window
     * Code mostly taken from: http://www.java2s.com/Tutorial/Java/0240__Swing/PrintaJTableout.htm
     */
    public void printTable() {
        if (this.relation.size() == 0) {    // if empty relation
            return;
        }

        String[][] objs = new String[this.relation.size()][this.relation.get(0).getLength()];
        for (int i=0; i<objs.length; i++) {
            for (int j=0; j<objs[0].length; j++) {
                if (this.relation.get(i).getValAtPos(j).getKey() == null) {
                    objs[i][j] = "NULL";
                }
                else {
                    objs[i][j] = this.relation.get(i).getValAtPos(j).getKey().toString();
                }
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
    public void remove(int rowToRemove) {
        // TODO - this might be put off for later bc it's technically not necessary for rel alg
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
    public void rightNatJoin(Rel rel) {
        // TODO
    }

    /**
     * Selects certain rows
     */
    public void select(String specs) {
        // TODO
        String[] specsArray = specs.split(" ");
    }

    /**
     * Semijoin (???????)
     * This is used so rarely it's not really necessary
     */
//    public void semiJoin(Rel rel) {
//        // TODO
//    }

    /**
     * Get sum of group
     */
    public Rel sum(String colName) throws IllegalTypeException {
            Attr attr = new Attr(0, "Sum(" + colName + ")");
            Tup tup;
            Rel rel;

            if (this.relation.size() == 0) {    // if empty table
                return new Rel("Sum(" + colName + ")");
            }

            int index = this.relation.get(0).getColNames().indexOf(colName);

            int type = this.relation.get(0).getAtPos(index).getType();
            int totalInt;
            double total;

            if (type >= 2) {
                throw new IllegalTypeException("IllegalTypeException: Cannot compute sum of this type");
            }

            // else - type is double or int
            else if (type == 0) {
                totalInt = 0;
                for (int i=0; i<this.relation.size(); i++) {
                    totalInt += (Integer) this.relation.get(i).getValAtPos(index).getKey();
                }
                attr = new Attr(totalInt, "Sum(" + colName + ")");
            }
            else if (type == 1) {
                total = 0.0;
                for (int i=0; i<this.relation.size(); i++) {
                    total += (Double) this.relation.get(i).getValAtPos(index).getKey();
                }
                attr = new Attr(total, "Sum(" + colName + ")");
            }

            tup = new Tup();
            tup.addAttr(attr);
            rel = new Rel("Sum(" + colName + ")");
            try {
                rel.insert(tup);
            } catch (IllegalInsertException e) {
                System.out.println(e.getMessage());
            }

            return rel;
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
    public void update(String locSpecs) {

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

        Rel relation3 = new Rel("Rel3");
        try {
            relation3.insert(tuple5);
            relation3.insert(tuple6);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        Attr a25 = new Attr(null, "null col");
        Attr a26 = new Attr(464, "int stuff");
        Attr a27 = new Attr("meh", "blugh");
        Attr a28 = new Attr(44.2, "some shit");

        Tup tuple7 = new Tup();
        tuple7.addAttr(a25);
        tuple7.addAttr(a26);
        tuple7.addAttr(a27);
        tuple7.addAttr(a28);

        Attr a29 = new Attr(7, "null col");
        Attr a30 = new Attr(5748, "int stuff");
        Attr a31 = new Attr("grump", "blugh");
        Attr a32 = new Attr(546.13, "some shit");

        Tup tuple8 = new Tup();
        tuple8.addAttr(a29);
        tuple8.addAttr(a30);
        tuple8.addAttr(a31);
        tuple8.addAttr(a32);

        Rel relation4 = new Rel("Rel4");
        try {
            relation4.insert(tuple7);
            relation4.insert(tuple8);
        } catch (IllegalInsertException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("RELATION 1:");
        System.out.println(relation.toString());

        System.out.println("RELATION 2:");
        System.out.println(relation2.toString());

        System.out.println("RELATION 3:");
        System.out.println(relation3.toString());

        System.out.println("RELATION 4:");
        System.out.println(relation4.toString());

//        relation.printTable();

        /*
        testing union
         */
//        try {
//            relation = relation.union(relation2);
//        } catch (ColumnNameException e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println(relation.toString());
//        relation.printTable();

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

        /*
        testing average
         */
//        try {
//            relation = relation.average("doubles");
//        } catch (IllegalTypeException e) {
//            System.out.println(e.getMessage());
//        }
//
//        relation.printTable();
//        System.out.println(relation.toString());

        /*
        testing sum
         */
//        try {
//            relation = relation.sum("doubles");
//        } catch (IllegalTypeException e) {
//            System.out.println(e.getMessage());
//        }
//
//        relation.printTable();
//        System.out.println(relation.toString());

        /*
        testing cross product
         */

        relation = relation.crossProd(relation4);
        relation.printTable();
        System.out.println("\nNEW REL: \n" + relation.toString());
    }
}
