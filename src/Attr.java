import javafx.util.Pair;

/**
 * Attributes only
 */

public class Attr {
    private int intVal;
    private double dubVal;
    private String strVal;

    private int TYPE;

    public Attr(int value) {
        this.intVal = value;
        this.TYPE = 0;
    }

    public Attr(double value) {
        this.dubVal = value;
        this.TYPE = 1;
    }

    public Attr(String value) {
        this.strVal = value;
        this.TYPE = 2;
    }

    public Pair getValue() {
        switch (this.TYPE) {
            case 0:
                return new Pair(this.intVal, 0);
            case 1:
                return new Pair(this.dubVal, 1);
            case 2:
                return new Pair(this.strVal, 2);
            default:
                return new Pair(null, 3);
        }
    }

    public int getType() {
        return this.TYPE;
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

    public String toString() {
        String str = "[";

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
        str += ", " + this.TYPE + "]";

        return str;
    }

    public static void main(String[] args) {
        Attr a1 = new Attr(5);
        Attr a2 = new Attr(6.2);
        Attr a3 = new Attr("thing");

        System.out.println("INITIAL TEST");
        System.out.println("A1: " + a1.toString());
        System.out.println("A2: " + a2.toString());
        System.out.println("A3: " + a3.toString());

        a1.setVal(18);
        a2.setVal(607.7);
        a3.setVal("other thing");

        System.out.println("\nAFTER RESET");
        System.out.println("A1: " + a1.toString());
        System.out.println("A2: " + a2.toString());
        System.out.println("A3: " + a3.toString());

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