import java.util.Arrays;

/**
 * Created by rvhirsch on 4/7/17.
 */
public class Parser3 {
    // all possible commands - I hope...
    private final static String PI = "\\Pi_";
    private final static String SIGMA1 = "\\sigma";
    private final static String SIGMA2 = "\\sigma_";
    private final static String AGGR = "\\G_";                // TODO
    private final static String NATJOIN = "\\bowtie";
    private final static String CROSSJOIN = "\\times";
    private final static String UNION = "\\cup";
    private final static String INTERSECT = "\\cap";
    private final static String RENAME = "\\rho";             // TODO
    private final static String AND1 = "\\vee";
    private final static String AND2 = "\\land";
    private final static String OR1 = "\\wedge";
    private final static String OR2 = "\\lor";
    private final static String EXCEPT = "-";
    private final static String LOJ = "\\leftouterjoin";
    private final static String ROJ = "\\rightouterjoin";
    private final static String FOJ = "\\fullouterjoin";

    private final static String REGEX = "(?=[\\\\({])";

    private String latex;
    private String[] texArray;
    public String sql;

    public Parser3(String eqn) {
        this.latex = eqn;
        this.texArray = this.latexToArray();
        this.sql = "";
        this.sql += this.parseArrayToSQL();
    }

    private String[] latexToArray() {
        this.latex = replacePrev(this.latex);

        return this.latex.split(REGEX);      // splits on { ( but keeps delimiters
    }

    private String parseArrayToSQL() {
        String curr, cols, from, where, join, group;
        Parser3 psql;

        for (int i=0; i<this.texArray.length; i++) {
            curr = this.texArray[i];
//            if (curr.equals("_")) {
//                // TODO
//                group = getGroup(i);
//                // this.texArray[i] += group;
//                i++;
//
//                if (this.texArray[i].contains(AGGR)) {
//
//                }
//                else if (this.texArray[i].contains(PI)) {
//
//                }
//                else if (this.texArray[i].contains(SIGMA1)) {
//
//                }
//                else if (this.texArray[i].contains(SIGMA2)) {
//
//                }
//
//            }
//            else if (curr.contains(AGGR)) {
//                System.out.println("IN AGGR");
//
//                // TODO
//            }
            if (curr.equals(PI)) {
                i = piSelect(i);
            }
            else if (curr.equals(SIGMA1)) {
                i = sigma1Select(i);
            }
            else if (curr.equals(SIGMA2)) {
                i = sigma2Select(i);
            }
            else if (curr.equals(AGGR)) {

            }
            else if (curr.contains(NATJOIN)) {
//                join = curr.substring(NATJOIN.length());
                this.sql += curr.replace(NATJOIN, " INNER JOIN ");
//                this.sql += " INNER JOIN " + join;
            }
            else if (curr.contains(INTERSECT)) {
//                join = curr.substring(INTERSECT.length());
//                this.sql += " INTERSECT " + join;
                this.sql += curr.replace(INTERSECT, " INTERSECT ");
            }
            else if (curr.contains(UNION)) {
//                join = curr.substring(UNION.length());
//                this.sql += " UNION " + join;
                this.sql += curr.replace(UNION, " UNION ");
            }
            else if (curr.contains(CROSSJOIN)) {
//                join = curr.substring(CROSSJOIN.length());
//                this.sql += " CROSS JOIN " + join;
                this.sql += curr.replace(CROSSJOIN, " CROSS JOIN ");
            }
            else if (curr.contains(LOJ)) {
//                join = curr.substring(LOJ.length());
//                this.sql += " LEFT JOIN " + join;
                this.sql += curr.replace(LOJ, " LEFT JOIN ");
            }
            else if (curr.contains(ROJ)) {
//                join = curr.substring(ROJ.length());
//                this.sql += " RIGHT JOIN " + join;
                this.sql += curr.replace(ROJ, " RIGHT JOIN ");
            }
            else if (curr.contains(FOJ)) {
//                join = curr.substring(FOJ.length());
//                this.sql += " FULL JOIN " + join;
                this.sql += curr.replace(FOJ, " FULL JOIN ");
            }
            else if (curr.contains(EXCEPT)) {
//                join = curr.substring(EXCEPT.length());
//                this.sql += " EXCEPT " + join;
                this.sql += curr.replace(EXCEPT, " EXCEPT ");
            }
            else {
//                System.out.println("thing: " + curr);
                this.sql += curr;
            }
        }

        return this.sql.replace("  ", " ");
    }

    private int piSelect(int i) {
        String cols = getCols(i);
        cols = cols.substring(1, cols.length() - 1);

        i += cols.split(REGEX).length + 1;

        String from = getFrom(i);
        from = from.substring(1, from.length()-1);

        Parser3 psql = new Parser3(from);
        from = psql.sql;

        int split = from.split(REGEX).length;
//      i += from.split(REGEX).length;
        i += split + 1;

        if (split > 1) {
            this.sql += "SELECT " + cols + " FROM (" + from + ")";
        }
        else {
            this.sql += "SELECT " + cols + " FROM " + from;
        }

        return i;
    }

    private int sigma1Select(int i) {
        String from = getFrom(i+1);
        from = from.substring(1, from.length()-1);

        System.out.println("from: " + from);

        int split = from.split(REGEX).length;
        i += split + 1;

        if (split > 1) {
            this.sql += "SELECT * FROM (" + from + ")";
        }
        else {
            this.sql += "SELECT * FROM " + from;
        }

        return i;
    }

    private int sigma2Select(int i) {
//        System.out.println("in sig 2 select, i(1) = " + i + ": " + this.texArray[i]);

        String where = getCols(i);     // literally same code as getWhere() would have
        i += where.split(REGEX).length + 1;
//        System.out.println("i(2) = " + i + ": " + this.texArray[i] + "\n");

        where = where.replace("||", "OR").replace("&&", "AND").substring(1, where.length()-1);

        String from = getFrom(i);
        Parser3 psql = new Parser3(replaceWords(from));
        from = psql.sql;
        i += from.split(REGEX).length + 1;

        this.sql += "SELECT * FROM " + from + " WHERE " + where;

//        System.out.println("i(3) = " + i + ": " + this.texArray[i]);

        return i;
    }

    private String getGroup(int pos) {
        String grp = "";
        String phrase = this.texArray[pos+1];
        char[] arr = phrase.toCharArray();
        int i = 0;
        while (arr[i] != '}') {
            grp += arr[i];
        }
        return grp;
    }

    private String getFrom(int pos) {
        String from = "";
        String curr = "";

        for (int i=pos; i<this.texArray.length; i++) {
//            System.out.println("from curr: " + from);

            curr = this.texArray[i];
            if (curr.contains(")")) {
                from += curr;
//                System.out.println("from curr: " + from);
                return from;
            }
            else {
                from += curr;
            }
        }

        return from;
    }

    private String getCols(int pos) {
        String cols = "";
        String curr = "";

        for (int i=pos+1; i<this.texArray.length; i++) {
            curr = this.texArray[i];
//            System.out.println("cols curr: " + curr);
            if (curr.contains("}")) {
                cols += curr;
                return cols;
            }
            else {
                cols += curr;
            }
        }

        return cols;
    }

    private String replacePrev(String str) {
        return str.replace(":", " NATURAL JOIN ").replace(AND1, " && ").replace(AND2, " && ").replace(OR1, " || ").replace(OR2, " || ")
                .replace(NATJOIN, " INNER JOIN ").replace(CROSSJOIN, ", ").replace(EXCEPT, "-")
                .replace(LOJ, " LEFT JOIN ").replace(ROJ, " RIGHT JOIN ").replace(FOJ, " FULL JOIN ")
                .replace(UNION, " UNION ").replace(INTERSECT, " INTERSECT ");
    }

    private String replaceWords(String str) {
        return str.replace("PI_", "\\Pi_").replace("sigma(", "\\sigma(").replace("sigma_", "\\sigma_")
                .replace(UNION, "\\cup").replace(INTERSECT, "\\cap").replace(RENAME, "\\rho");
//                .replace(NATJOIN, "\\bowtie").replace(CROSSJOIN, "\\bigtimes")
                //.replace(AND1, "\\vee").replace(AND2, "\\land").replace(OR1, "\\wedge").replace(OR2, "\\lor").replace(EXCEPT, "\\-")
                //.replace(LOJ, "\\leftouterjoin").replace(ROJ, "\\rightouterjoin").replace(FOJ, "\\fullouterjoin");
    }

    public String runTest(String input, int num, Parser3 p) {
        System.out.println("\nLaTeX Input " + num + ": " + input);
        System.out.println("Array Output: " + Arrays.toString(p.texArray));
        System.out.println("SQL Output: " + p.sql);
        return p.sql;
    }

    public static void main(String[] args) {
        String sampleInput1 = "\\Pi_{name}(Person)";
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";
        String sampleInput4 = "_{age, name}\\G_{max(age), name} (Person \\bowtie Eats)";
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";
        String sampleInput6 = "\\sigma(Person)";

        // actual test stuff //
        String input = sampleInput2;

        Parser3 p = new Parser3(input);
        System.out.println("Latex: " + input);
        System.out.println("Array: " + Arrays.toString(p.latexToArray()));
        System.out.println("SQL: " + p.sql);
    }
}
