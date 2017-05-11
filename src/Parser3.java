import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rvhirsch on 4/7/17.
 */
public class Parser3 {
    // all possible commands - I hope...
    private final static String PI = "\\Pi_";
    private final static String SIGMA1 = "\\sigma";
    private final static String SIGMA2 = "\\sigma_";
    private final static String AGGR = "\\gamma_";
    private final static String NATJOIN = "\\bowtie";
    private final static String CROSSJOIN = "\\times";
    private final static String UNION = "\\cup";
    private final static String INTERSECT = "\\cap";
//    private final static String RENAME = "\\rho";             // TODO
    private final static String AND1 = "\\vee";
    private final static String AND2 = "\\land";
    private final static String OR1 = "\\wedge";
    private final static String OR2 = "\\lor";
    private final static String EXCEPT = "-";
    private final static String LOJ = "\\loj";
    private final static String ROJ = "\\roj";
    private final static String FOJ = "\\foj";
    private final static String GEQ = "\\geq";
    private final static String LEQ = "\\leq";
    private final static String MAX = "\\max";
    private final static String MIN = "\\min";
    private final static String AVG = "\\avg";
    private final static String SUM = "\\sum";
    private final static String COUNT = "\\count";

    private final static String REGEX = "(?=[\\\\({])";

    public static DBInfo info;

    private String latex;
    private String[] texArray;
    public String sql;

    public Parser3(String eqn, DBInfo i) {
        this.latex = eqn;
        this.texArray = this.latexToArray();
        this.sql = "";
        this.sql += this.parseArrayToSQL();

        this.info = i;
    }

    private String[] latexToArray() {
        this.latex = replacePrev(this.latex);

        return this.latex.split(REGEX);      // splits on { ( but keeps delimiters
    }

    private String parseArrayToSQL() {
        String curr, cols, from, where, join;
        String group = "";
        Parser3 psql;

        for (int i=0; i<this.texArray.length; i++) {
            curr = this.texArray[i];

            System.out.println("curr: " + curr);

            if (curr.equals("_") || curr.equals("(_")) {     // group in next spot(s) in array
                group = getCols(i);     // same code as what getGroup() would have
                group = group.replace("{", "").replace("}", "");

                // this.texArray[i] += group;
                int split = group.split(REGEX).length;
                i += split;
            }
            else if (curr.equals(PI) || curr.equals(AGGR)) {
                i = piSelect(i, group);
            }
            else if (curr.equals(SIGMA1)) {
                i = sigma1Select(i, group);
            }
            else if (curr.equals(SIGMA2)) {
                i = sigma2Select(i, group);
            }
//            else if (curr.contains(NATJOIN)) {
//                this.sql += curr.replace(NATJOIN, " NATURAL JOIN ");
//            }
//            else if (curr.equals(INTERSECT)) {
//                this.sql += curr.replace(INTERSECT, " INTERSECT ");
//            }
            else if (curr.equals(UNION)) {
                this.sql += curr.replace(UNION, " UNION ");
            }
//            else if (curr.contains(CROSSJOIN)) {
//                this.sql += curr.replace(CROSSJOIN, " CROSS JOIN ");
//            }
//            else if (curr.contains(LOJ)) {
//                this.sql += curr.replace(LOJ, " LEFT JOIN ");
//            }
//            else if (curr.contains(ROJ)) {
//                this.sql += curr.replace(ROJ, " RIGHT JOIN ");
//            }
//            else if (curr.contains(FOJ)) {
//                this.sql += curr.replace(FOJ, " FULL JOIN ");
//            }
            else if (curr.contains(EXCEPT)) {
                this.sql += curr.replace(EXCEPT, " EXCEPT ");
            }
            else {
//                System.out.println("curr = " + curr);
                this.sql += curr;
            }
        }

        this.sql = this.sql.replace("  ", " ");

//        fixColNames();

        return this.sql;
    }

    private int piSelect(int i, String group) {
        String cols = getCols(i);
        cols = cols.replace("{", "").replace("}", "");

        i += cols.split(REGEX).length + 1;

        String from = getFrom(i);
//        from = from.substring(1, from.length()-1);

        Parser3 psql = new Parser3(from, info);
        from = psql.sql;

        int split = from.split(" ").length;
        i += split + 1;

        if (split > 1) {
            this.sql += "SELECT " + cols + " FROM (" + from + ")";
        }
        else {
            this.sql += "SELECT " + cols + " FROM " + from;
        }

        if (group.length() > 0) {
            this.sql += " GROUP BY " + group;
        }

        return i;
    }

    private int sigma1Select(int i, String group) {
        String from = getFrom(i+1);

        int split = from.split(REGEX).length;
        i += split;

        if (split > 1) {
            this.sql += "SELECT * FROM (" + from + ")";
        }
        else {
            this.sql += "SELECT * FROM " + from;
        }

        if (group.length() > 0) {
            this.sql += " GROUP BY " + group;
        }

        return i;
    }

    private int sigma2Select(int i, String group) {
        String where = getCols(i);     // literally same code as getWhere() would have
        i += where.split(REGEX).length + 1;

        where = where.replace("||", "OR").replace("&&", "AND").substring(1, where.length()-1);

        String from = getFrom(i);
        Parser3 psql = new Parser3(replaceWords(from), info);
        from = psql.sql;

        i += from.split(" ").length + 1;

        this.sql += "SELECT * FROM " + from + " WHERE " + where;

        if (group.length() > 0) {
            this.sql += " GROUP BY " + group;
        }

        return i;
    }

    private String getFrom(int pos) {
        String from = "";
        String curr = "";

        for (int i=pos; i<this.texArray.length; i++) {
//            System.out.println("from curr: " + from);

            curr = this.texArray[i];
            if (curr.contains(")")) {
                from += curr;

                if (from.charAt(0) == '(') {
                    from = from.substring(1);
                }
                if (from.charAt(from.length()-1) == ')') {
                    from = from.substring(0, from.length()-1);
                }
                if (from.charAt(from.length()-2) == ')' && from.charAt(from.length()-1) == ' ') {  // check for trailing spaces
                    from = from.substring(0, from.length()-2);
                }

//                System.out.println("from: " + from);

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
        str = str.replace(":", ", ").replace(AND1, " && ").replace(AND2, " && ").replace(OR1, " || ").replace(OR2, " || ")
                .replace(LEQ, " <= ").replace(GEQ, " >= ").replace(MAX, " max").replace(MIN, " min").replace(AVG, " avg")
                .replace(SUM, " sum"). replace(COUNT, " count")
                .replace(NATJOIN, " NATURAL JOIN ").replace(CROSSJOIN, " CROSS JOIN ")
//                .replace(EXCEPT, "-")
                .replace(LOJ, " LEFT JOIN ").replace(ROJ, " RIGHT JOIN ")
//                .replace(UNION, " UNION ")
                .replace(INTERSECT, " INTERSECT ")
                .replace("  ", " ");

        str = replaceFOJ(FOJ, str);
//        System.out.println("\tstr = " + str);

        return str;
    }

    public String replaceFOJ(String old, String input) {
        int i = input.indexOf(old);

        if (i < 0) {
            return input;
        }

        String partBefore = input.substring(0, i);
        String partAfter  = input.substring(i + old.length());
//        System.out.println("\tpb = " + partBefore);
//        System.out.println("\tpa = " + partAfter);

        Pair<String, String> newWord = getNewFOJ(input, i);
        String right = newWord.getKey();
        String left = newWord.getValue();

        String word = " LEFT JOIN " + left + ") \\cup\\sigma(" + right + " RIGHT JOIN " + left + ")";

        System.out.println(partAfter + right.length());
        String str = partBefore + word + partAfter.substring(right.length()-1);

        return replaceFOJ(FOJ, str);
    }


    private Pair<String, String> getNewFOJ(String str, int i) {

        String left = getLeftTable(str, i);
//        System.out.println("\tleft = " + left);

        String right = getRightTable(str, i + FOJ.length());
//        System.out.println("\tright = " + right);

        return new Pair<String, String>(left, right);
    }

    private String getLeftTable(String str, int pos) {
        String left = "";
        char c = str.charAt(pos);
        while (c != '(' && pos >= 0) {
            if (c != ' ' && c != '\\') {
                left = str.charAt(pos) + left;
            }
            pos--;
            c = str.charAt(pos);
        }
        return left;
    }

    private String getRightTable(String str, int pos) {
        String right = "";
        while (str.charAt(pos) != ')' && pos < str.length()) {
            right += str.charAt(pos);
            pos++;
        }
        return right;
    }

    private String replaceWords(String str) {
        return str.replace("PI_", "\\Pi_").replace("sigma(", "\\sigma(").replace("sigma_", "\\sigma_")
                .replace(UNION, "\\cup").replace(INTERSECT, "\\cap");
//                .replace(RENAME, "\\rho");
    }

    public String runTest(String input, int num, Parser3 p) {
        System.out.println("\nLaTeX Input " + num + ": " + input);
        System.out.println("Array Output: " + Arrays.toString(p.texArray));
        System.out.println("SQL Output: " + p.sql);
        return p.sql;
    }

    public void fixColNames() {
        ArrayList<DBInfo.TBInfo> tables = info.getTables();
        ArrayList<String> tableNames = info.getTBNames();

        ArrayList<String> currNames = new ArrayList<String>();
        ArrayList<String> colNames, otherNames;
        String name;

        String[] words = this.sql.split(" ");

        for (int i=0; i<words.length; i++) {
            name = words[i];
            if (tableNames.contains(name)) {
                currNames.add(name);

            }
        }

        DBInfo.TBInfo table, oldTable;
        String newColName;

        for (int i=0; i<currNames.size(); i++) {
            name = currNames.get(i);
            colNames = this.info.getColNamesbyTBName(name);

            for (String col: colNames) {
                for (int j=i; j<currNames.size(); j++) {
                    otherNames = this.info.getColNamesbyTBName(currNames.get(j));

                    if (otherNames.contains(col)) {
                        // reset cols
                        newColName = currNames.get(j) + "_" + col;
                        oldTable = this.info.getTables().get(j);
                        table = oldTable.resetColName(otherNames.indexOf(col), newColName);
                        this.info.resetTableCol(oldTable, table);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String sampleInput1 = "\\Pi_{name}(Person)";
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";
        String sampleInput4 = "_{age, name}\\gamma_{max(age), name} (Person \\bowtie Eats)";
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";
        String sampleInput6 = "\\sigma(Person)";
        String sampleInput7 = "_{age}\\Pi_{name}(Person)";
        String sampleInput8 = "\\sigma(Person \\foj Eats)";

        // actual test stuff //
        String input = sampleInput8;

        Parser3 p = new Parser3(input, info);
        System.out.println("Latex: " + input);
        System.out.println("Array: " + Arrays.toString(p.latexToArray()));
        System.out.println("SQL: " + p.sql);
    }
}
