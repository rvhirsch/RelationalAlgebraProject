import java.util.Arrays;

/**
 * Created by rvhirsch on 4/7/17.
 */
public class Parser3 {
    // all possible commands - I hope...
    private final static String PI = "\\Pi_";
    private final static String SIGMA1 = "\\sigma";
    private final static String SIGMA2 = "\\sigma_";
    private final static String NATJOIN = "\\bowtie";
    private final static String CROSSJOIN = "\\bigtimes";
    private final static String UNION = "\\cup";
    private final static String INTERSECT = "\\cap";
    private final static String RENAME = "\\rho";             // TODO
    private final static String AND1 = "\\vee";               // TODO
    private final static String AND2 = "\\land";              // TODO
    private final static String OR1 = "\\wedge";              // TODO
    private final static String OR2 = "\\lor";                // TODO
    private final static String EXCEPT = "-";
    private final static String LOJ = "\\leftouterjoin";
    private final static String ROJ = "\\rightouterjoin";
    private final static String FOJ = "\\fullouterjoin";

    private String latex;
    private String[] texArray;
    private String sql;

    public Parser3(String eqn) {
        this.latex = eqn;
        this.texArray = this.latexToArray();
        this.sql = "";
        this.sql += this.parseArrayToSQL();
    }

    private String[] latexToArray() {
        return this.latex.split("(?=[\\({])");      // splits on \ { ( but keeps delimiters
    }

    private String parseArrayToSQL() {
        String curr, cols, from, where, join;
        Parser3 psql;

//        System.out.println("CURR EQN: " + Arrays.toString(texArray));

        for (int i=0; i<this.texArray.length; i++) {
            curr = this.texArray[i];
            if (curr.equals(PI)) {
                cols = getCols(i);
                i += cols.split("(?=[\\({])").length + 1;
                cols = cols.substring(1, cols.length()-1);

                from = getFrom(i);

                from = from.substring(1, from.length()-1);

                psql = new Parser3(from);
                from = psql.sql;

                int split = from.split("(?=[\\({])").length;
//                i += from.split("(?=[\\({])").length;
                i += split;

                if (split > 1) {
                    this.sql += "SELECT " + cols + " FROM (" + from + ")";
                }
                else {
                    this.sql += "SELECT " + cols + " FROM " + from;
                }
            }
            else if (curr.equals(SIGMA1)) {
                from = getFrom(i);
                i += from.split("(?=[\\({])").length;
                this.sql += "SELECT * FROM " + from;
            }
            else if (curr.equals(SIGMA2)) {
                where = getCols(i);     // literally same code as getWhere() would have
                i += where.split("(?=[\\({])").length + 1;

                where = where.replace("||", "OR").replace("&&", "AND").substring(1, where.length()-1);

                from = getFrom(i);
                psql = new Parser3(replaceWords(from));
                from = psql.sql;

                i += from.split("(?=[\\({])").length;
                this.sql += "SELECT * FROM " + from + " WHERE " + where;
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
                System.out.println("CURR: " + curr);
                this.sql += curr;
            }
        }

        return this.sql.replace("  ", " ");
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
//            System.out.println("cols curr: " + curr);

            curr = this.texArray[i];
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

    private String replaceWords(String str) {
        return str.replace(PI, "\\Pi_").replace(SIGMA1, "\\sigma").replace(SIGMA2, "\\sigma_").replace(NATJOIN, "\\bowtie")
                .replace(CROSSJOIN, "\\bigtimes").replace(UNION, "\\cup").replace(INTERSECT, "\\cap").replace(RENAME, "\\rho")
                .replace(AND1, "\\vee").replace(AND2, "\\land").replace(OR1, "\\wedge").replace(OR2, "\\lor").replace(EXCEPT, "\\-")
                .replace(LOJ, "\\leftouterjoin").replace(ROJ, "\\rightouterjoin").replace(FOJ, "\\fullouterjoin");
    }

    public String runTest(String input, int num, Parser3 p) {
        System.out.println("\nLaTeX Input " + num + ": " + input);
        System.out.println("Array Output: " + Arrays.toString(p.texArray));
        System.out.println("SQL Output: " + p.sql);
        return p.sql;
    }

    public static void main(String[] args) {
        String sampleInput1 = "\\Pi_{name}(Person)";
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie \\sigma(Eats))";
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally' && mother == 'not me'}(Person \\bowtie Eats)";
        String sampleInput4 = "_{age, name}\\G_{max(age)}(Person \\bowtie Eats)";
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age < 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";
        String sampleInput6 = "\\sigma(Person)\\bowtie Eats\\bowtie\\sigma_{age < 10}(Person)";

        Parser3 p = new Parser3(sampleInput1);
        System.out.println("Latex: " + sampleInput1);
        System.out.println("Array: " + Arrays.toString(p.latexToArray()));
        System.out.println("SQL: " + p.sql);
    }
}
