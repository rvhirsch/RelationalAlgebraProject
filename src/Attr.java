import javafx.util.Pair;

/**
 * Attributes only
 */

public class Attr {
    private int intVal;
    private double dubVal;
    private String strVal;

    private String columnName;

    /*
      * type 0 = int
      * type 1 = double
      * type 2 = string
     */
    private int TYPE;

    public Attr(int value, String colName) {
        this.intVal = value;
        this.TYPE = 0;
        this.columnName = colName;
    }

    public Attr(double value, String colName) {
        this.dubVal = value;
        this.TYPE = 1;
        this.columnName = colName;
    }

    public Attr(String value, String colName) {
        this.strVal = value;
        this.TYPE = 2;
        this.columnName = colName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Attr)) return false;

        Attr attr = (Attr) other;

        return this.getValue().equals(attr.getValue()) && this.getType() == attr.getType();
    }

    public String getColumnName() {
        return this.columnName;
    }

    public int getType() {
        return this.TYPE;
    }

    public Pair getValue() {
        switch (this.TYPE) {
            case 0:
                return new Pair<Integer, Integer>(this.intVal, 0);
            case 1:
                return new Pair<Double, Integer>(this.dubVal, 1);
            case 2:
                return new Pair<String, Integer>(this.strVal, 2);
            default:
                return new Pair<Integer, Integer>(null, 3);
        }
    }

    public Attr setColumnName(String colName) {
        this.columnName = colName;

        return this;
    }

    public void setVal(int value) {
        if (this.TYPE == 0) {
            this.intVal = value;
        }
        else if (this.TYPE == 1) {
            this.dubVal = value;
        }
        else {
            System.out.println("UGH");
        }
    }

    public void setVal(double value) {
        this.dubVal = value;
    }

    public void setVal(String value) {
        this.strVal = value;
    }

    @Override
    public String toString() {
//        String str = "[Value: ";      // leaving this in for testing later maybe

        String str = "";

        switch (this.TYPE) {
            case 0:
                str += this.intVal;
                break;
            case 1:
                str += this.dubVal;
                break;
            case 2:
                str += this.strVal;
                break;
            default:
                str += " BROKEN ";
                break;
        }
//        str += ", ColName: " + this.columnName + ", Type: " + this.TYPE + "]";

        return str;
    }

    public static void main(String[] args) {
        Attr a1 = new Attr(5, "Integers");
        Attr a2 = new Attr(6.2, "Doubles");
        Attr a3 = new Attr("thing", "String");

        System.out.println("INITIAL TEST");
        System.out.println("A1: " + a1.toString());
        System.out.println("A2: " + a2.toString());
        System.out.println("A3: " + a3.toString());

        System.out.println("\nGET COLUMN NAMES");
        System.out.println("A1: " + a1.getColumnName());
        System.out.println("A2: " + a2.getColumnName());
        System.out.println("A3: " + a3.getColumnName());

        a1.setVal(18);
        a2.setVal(607.7);
        a3.setVal("other thing");

        a1.setColumnName("ints");
        a2.setColumnName("dubs");
        a3.setColumnName("strs");

        System.out.println("\nAFTER RESETS");
        System.out.println("A1: " + a1.toString());
        System.out.println("A2: " + a2.toString());
        System.out.println("A3: " + a3.toString());

        System.out.println("\nAFTER COLUMN RESET");
        System.out.println("A1: " + a1.getColumnName());
        System.out.println("A2: " + a2.getColumnName());
        System.out.println("A3: " + a3.getColumnName());

        System.out.println("\nGET TYPES");
        System.out.println("A1: " + a1.getType());
        System.out.println("A2: " + a2.getType());
        System.out.println("A3: " + a3.getType());

        System.out.println("\nGET VALUES");
        System.out.println("A1: " + a1.getValue().getKey());
        System.out.println("A2: " + a2.getValue().getKey());
        System.out.println("A3: " + a3.getValue().getKey());
    }
}
