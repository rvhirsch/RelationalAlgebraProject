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
    private final static String AGGR = "\\gamma_";                // TODO
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

//            System.out.println("curr: " + curr);

            if (curr.equals("_")) {     // group in next spot(s) in array
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
            else if (curr.equals(AGGR)) {

            }
            else if (curr.contains(NATJOIN)) {
                this.sql += curr.replace(NATJOIN, " INNER JOIN ");
            }
            else if (curr.contains(INTERSECT)) {
                this.sql += curr.replace(INTERSECT, " INTERSECT ");
            }
            else if (curr.contains(UNION)) {
                this.sql += curr.replace(UNION, " UNION ");
            }
            else if (curr.contains(CROSSJOIN)) {
                this.sql += curr.replace(CROSSJOIN, " CROSS JOIN ");
            }
            else if (curr.contains(LOJ)) {
                this.sql += curr.replace(LOJ, " LEFT JOIN ");
            }
            else if (curr.contains(ROJ)) {
                this.sql += curr.replace(ROJ, " RIGHT JOIN ");
            }
            else if (curr.contains(FOJ)) {
                this.sql += curr.replace(FOJ, " FULL JOIN ");
            }
            else if (curr.contains(EXCEPT)) {
                this.sql += curr.replace(EXCEPT, " EXCEPT ");
            }
            else {
//                System.out.println("thing: " + curr);
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
        from = from.substring(1, from.length()-1);

        Parser3 psql = new Parser3(from, info);
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

        if (group.length() > 0) {
            this.sql += " GROUP BY " + group;
        }

        return i;
    }

    private int sigma1Select(int i, String group) {
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
        i += from.split(REGEX).length + 1;

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
        return str.replace(":", ", ").replace(AND1, " && ").replace(AND2, " && ").replace(OR1, " || ").replace(OR2, " || ")
                .replace(NATJOIN, " INNER JOIN ").replace(CROSSJOIN, " CROSS JOIN ").replace(EXCEPT, "-")
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

        // actual test stuff //
        String input = sampleInput7;

        Parser3 p = new Parser3(input, info);
        System.out.println("Latex: " + input);
        System.out.println("Array: " + Arrays.toString(p.latexToArray()));
        System.out.println("SQL: " + p.sql);
    }
}
