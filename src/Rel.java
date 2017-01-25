import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rvhirsch on 1/18/17.
 */

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
    public void proj() {

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
        for (int i=0; i<relation.size(); i++) {
            relation.get(i).updateCatName(newName, col);
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
     * Prints relation
     */
    public String toString() {
        String str = "";
        str += Arrays.toString(relation.get(0).getColNames().toArray());    // column headers
        str += "\n";

        for (int i=0; i<relation.size(); i++) {
            str += relation.get(i).toString();      // db info
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

        Attr a4 = new Attr(12, "integers");
        Attr a5 = new Attr(17.5, "doubles");
        Attr a6 = new Attr("thing6", "strings");

        Tup tuple = new Tup();
        tuple.addAttr(a1);
        tuple.addAttr(a2);
        tuple.addAttr(a3);

        Tup tuple2 = new Tup();
        tuple2.addAttr(a4);
        tuple2.addAttr(a5);
        tuple2.addAttr(a6);

        Rel relation = new Rel("Rel1");
        relation.insert(tuple);
        relation.insert(tuple2);

        System.out.println(relation.toString());
    }
}
