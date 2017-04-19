public class Parser {

    public Parser() {
    }

    public String parseLatexToString (String input) {
        String statement = "";
        String curWord = "";
//        String prevWord = "";
//        String nextWord = "";

        for (int x = 0; x < input.length(); x++) {
            curWord += input.charAt(x);

            if (curWord.equals("max")) {        // TODO: FINISH THIS
                statement += "max";
            }

//            if (input.charAt(x) == '\\') {
//                curWord = "";
//            }

            if (input.charAt(x) == ' ') {
                statement += curWord.toLowerCase();
                curWord = "";
            }

            if (input.charAt(x) == '(') {
                statement += " (";
                curWord = "";
            }
            else if (input.charAt(x) == ')') {
                statement += curWord.replace(')', '\u0000').toLowerCase();
                statement += ")";
                curWord = "";
            }

            if (input.charAt(x) == '{') {
                curWord = "";
            }
            else if (input.charAt(x) == '}' && (input.contains("<") || input.contains(">") || input.contains("="))) {
                statement += " " + curWord.replace('}', '\u0000').toLowerCase();
                curWord = "";
            }
            else if (input.charAt(x) == '}') {
                statement += " " + curWord.replace('}', '\u0000').toLowerCase();
                curWord = "";
            }

            switch (curWord) {
                case ("\\Pi"):       // project
                    if (input.charAt(x + 1) != ' ' && input.charAt(x + 1) == '_') {
                        statement += "PI ";
                        curWord = "";
                    }

                    break;
                case ("\\sigma"):    // select
                    statement += "SIGMA ";
                    curWord = "";

                    break;
                case ("\\G"):
                    statement += "GROUP ";
                    curWord = "";

                    break;
                case ("\\bigtimes"):  // cross product
                    statement += "CROSSJOIN ";
                    curWord = "";

                    break;
                case ("\\bowtie"):    // natural join
                    statement += "NATJOIN ";
                    curWord = "";

                    break;
                case ("\\rho"):      // rename    TODO
                    statement += "RENAME ";
                    curWord = "";

                    break;
                case ("\\cup"):     // union
                    statement += "UNION ";
                    curWord = "";

                    break;
                case ("\\cap"):   // intersection
                    statement += "INTERSECT ";
                    curWord = "";

                    break;
                case ("\\vee"):   // and
                    statement += "AND ";
                    curWord = "";

                    break;
                case ("\\land"):   // and - 2nd possibility
                    statement += "AND ";
                    curWord = "";

                    break;
                case("\\wedge"):  // or
                    statement += "OR ";
                    curWord = "";

                    break;
                case("\\lor"):  // or - 2nd possibility
                    statement += "OR ";
                    curWord = "";

                    break;
                case("\\-"):
                    statement += "EXCEPT ";
                    curWord = "";

                    break;
                case ("\\leftouterjoin"):
                    statement += "LOJ ";
                    curWord = "";

                    break;
                case ("\\rightouterjoin"):
                    statement += "ROJ ";
                    curWord = "";

                    break;
                case ("\\fullouterjoin"):
                    statement += "FOJ ";
                    curWord = "";

                    break;
            }
        }

        return statement;
    }

    public String parseStringToSQL(String str) {
        str = str.replace("  ", " ");   // standardize input (just in case??)
        String[] commands = str.split(" ");
//        System.out.println("COMMANDS: " + Arrays.toString(commands));

        String sql = "";    // output string

        String from = "";   // table name
        String where = "";  // where clause
        String cols = "";   // cols to select
        String group = "";  // stuff to group by
        String aggr = "";   // aggregation statements - i.e. cols to select when grouping

        String comm;    // current command in list
        for (int i=0; i<commands.length; i++) {
            comm = commands[i];
            switch (comm) {
                case ("PI"):       // project
                    sql += "SELECT ";
                    cols = getCols(commands, i);
                    sql += cols;
                    i += cols.split(" ").length;
//                    System.out.println("i = " + i);

                    sql += " FROM ";

                    from = getFrom(commands, i) + ")";
//                    System.out.println("from: " + from.substring(1, from.length()-1));

                    from = parseStringToSQL(from.substring(1, from.length()-1));
//                    System.out.println("from post parse: " + from);

                    i += from.split(" ").length + 1;

                    sql += "(" + from;

                    break;
                case ("SIGMA"):    // select
                    if (str.contains("<") || str.contains(">") || str.contains("=")) {
                        where = getWhere(commands, i);
                        sql += "SELECT * FROM ";
                        i += where.split(" ").length + 1;
//                        System.out.println("i = " + i);

//                        System.out.println("comm[i] = " + commands[i]);
                        while (commands[i].equals("&&") || commands[i].equals("||")) {
                            if (commands[i].equals("&&")) {
                                where += "AND ";
                            }
                            else if (commands[i].equals("||")) {
                                where += "OR ";
                            }
                            where += getWhere(commands, i);
//                            System.out.println("where: " + where);
                            i += 4;
//                            System.out.println("while i: " + i);
                        }

                        from = getFrom(commands, i-1);
                        sql += parseStringToSQL(from);

                        i += from.split(" ").length + 1;

                        sql += "WHERE " + where;
                    }
                    else {
                        sql += "SELECT * FROM ";
                        from = getFrom(commands, i);

                        i += from.split(" ").length + 1;
                        sql += from;
                    }

//                    System.out.println("GROUP: " + group);
//                    if (!group.equals("")) {
//                        sql += "GROUP BY " + group;
//                    }

                    break;
                case ("(SIGMA"):    // select
                    if (str.contains("<") || str.contains(">") || str.contains("=")) {
                        where = getWhere(commands, i);
                        sql += "(SELECT * FROM ";
                        i += where.split(" ").length + 1;
//                        System.out.println("i = " + i);

//                        System.out.println("comm[i] = " + commands[i]);
                        while (commands[i].equals("&&") || commands[i].equals("||")) {
                            if (commands[i].equals("&&")) {
                                where += "AND ";
                            }
                            else if (commands[i].equals("||")) {
                                where += "OR ";
                            }
                            where += getWhere(commands, i);
//                            System.out.println("where: " + where);
                            i += 4;
//                            System.out.println("while i: " + i);
                        }

                        from = getFrom(commands, i-1);
                        sql += parseStringToSQL(from);

                        i += from.split(" ").length + 1;

                        sql += "WHERE " + where;
                    }
                    else {
                        sql += "(SELECT * FROM ";
                        from = getFrom(commands, i);

                        i += from.split(" ").length + 1;
                        sql += from;
                    }

//                    System.out.println("GROUP: " + group);
//                    if (!group.equals("")) {
//                        sql += "GROUP BY " + group;
//                    }

                    break;
                case ("GROUP"):
                    String grpCols = getCols(commands, i);
                    group = getGroup(commands, i);
                    i += grpCols.split(" ").length;
                    String grpFrom = parseStringToSQL(getFrom(commands, i));

//                    group = getGroup(commands, i);
//                    i += group.split(" ").length;
//
//                    while (group.charAt(group.length()-1) == ',') {
//                        group += getGroup(commands, i);
//                    }
//                    System.out.println("GRP: " + group + "\n");


                    System.out.println("COLS: " + grpCols);
                    System.out.println("FROM: " + grpFrom);
                    System.out.println("GROUP: " + group);

                    sql += "SELECT " + grpCols + " FROM " + grpFrom;
                    if (group.length() > 0) {
                        sql += " GROUP BY " + group;
                    }

                    break;
                case ("UNION"):
                    sql += "UNION ALL ";

                    break;
                case ("INTERSECT"):
                    sql += "INTERSECT ";

                    break;
                case ("AND"):
                    sql += "AND ";

                    break;
                case ("OR"):
                    sql += "OR ";

                    break;
                case ("EXCEPT"):
                    sql += "EXCEPT  ";

                    break;
                case ("CROSSJOIN"):  // cross product
                    sql += "CROSS JOIN ";

                    break;
                case ("NATJOIN"):
                    sql += "INNER JOIN ";

                    break;
                case ("LOJ"):
                    sql += "LEFT JOIN ";

                    break;
                case ("ROJ"):
                    sql += "RIGHT JOIN ";

                    break;
                case ("FOJ"):
                    sql += "FULL JOIN ";

                    break;
                case ("RENAME"):

                    break;
                default:
                    sql += comm + " ";
            }

        }

        sql = sql.replace("\u0000", "");    // replace special space character
        return sql;
    }

    private String getCols(String[] comms, int i) {
        String cols = "";
        for (int j=i+1; j<comms.length; j++) {
            if (comms[j].contains(",")) {
                cols += comms[j] + " ";
            }
            if (!comms[j].contains((","))) {
                cols += comms[j];
                return cols;
            }
        }
        return cols;
    }

    private String getWhere(String[] comms, int i) {
        String where = "";
        for (int j=i+1; j<i+4; j++) {
            where += comms[j] + " ";
        }
        return where;
    }

    private String getFrom(String[] comms, int i) {
        String from = "";
        from += comms[i+1] + " ";
        for (int j=i+2; j<comms.length; j++) {
            if (!comms[j].contains(")")) {
                from += comms[j] + " ";
            }
            else if (comms[j].contains((")"))) {
                from += comms[j];
                return from;
            }
        }

        return from;
    }

    private String getGroup(String[] comms, int i) {
        String group = "";
        group = comms[i-1] + " " + group;
        for (int j=i-1; j>=0; j--) {
            if (comms[j].contains(",")) {
                group = comms[j] + " " + group;
            }
            else if (comms[j].contains("(")) {
                group = comms[j] + " " + group;
                return group;
            }
            else if (j == 0) {
                group = comms[j] + " " + group;
                return group;
            }
        }
//        for (int j=i+1; j<comms.length; j++) {
//            System.out.println("C[j] = " + comms[j]);
//            if (comms[j].contains(",")) {
//                group += comms[j] + " ";
//            }
//            else if (!comms[j].contains((",")) || comms[j].equals("max")) {
//                group += comms[j] + comms[j+1];
//                return group;
//            }
//        }
        return group;
    }

    public String runTest(String input, int num, Parser p) {
        System.out.println("\nLaTeX Input " + num + ": " + input);
        String latexToStr = p.parseLatexToString(input);
        System.out.println("String Output: " + latexToStr);
        String strToSql = p.parseStringToSQL(latexToStr);
        System.out.println("SQL Output: " + strToSql);
        return strToSql;
    }

    public static void main(String[] args) throws Exception {
        Parser p = new Parser();

        // SELECT name FROM (Person)
        String sampleInput1 = "\\Pi_{name}(Person)";

        // SELECT name, age  FROM (SELECT * FROM person INNER JOIN eats ) WHERE age > 16
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";     // WORKS

        // SELECT * FROM (person INNER JOIN eats ) WHERE age > 10 OR name == 'sally' 
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";    // WORKS

        // SELECT max(age) FROM (person INNER JOIN eats) GROUP BY age, name
        String sampleInput4 = "_{age, name}\\G_{max(age)} (Person \\bowtie Eats)";    // WORKS

        // SELECT name, age  FROM (SELECT * FROM (eats INNER JOIN (person INTERSECT pizzeria ) ) ) WHERE age > 10 OR name == ' sally'
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";

        // SELECT * FROM (person)
        String sampleInput6 = "\\sigma(Person)";

        String latexToStr;
        String strToSql;

//        p.runTest(sampleInput1, 1, p);
//        p.runTest(sampleInput2, 2, p);
//        p.runTest(sampleInput3, 3, p);

        p.runTest(sampleInput4, 4, p);      // TODO: STILL TOTALLY BROKEN

//        p.runTest(sampleInput5, 5, p);
//        p.runTest(sampleInput6, 6, p);
    }
}
