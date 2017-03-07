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

            if (input.charAt(x) == '\\') {
                curWord = "";
            }

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
                statement += curWord.replace('}', '\u0000').toLowerCase();
                curWord = "";
            }
            else if (input.charAt(x) == '}') {
                statement += curWord.replace('}', '\u0000').toLowerCase();
                curWord = "";
            }

            switch (curWord) {
                case ("Pi"):       // project
                    if (input.charAt(x + 1) != ' ' && input.charAt(x + 1) == '_') {
                        statement += "PI ";
                        curWord = "";
                    }

                    break;
                case ("sigma"):    // select
                    statement += "SIGMA ";
                    curWord = "";

                    break;
                case ("G"):
                    statement += "GROUP ";
                    curWord = "";

                    break;
                case ("bigtimes"):  // cross product
                    statement += "CROSSJOIN ";
                    curWord = "";

                    break;
                case ("bowtie"):    // natural join
                    statement += "NATJOIN ";
                    curWord = "";

                    break;
                case ("rho"):      // rename    TODO
                    statement += "RENAME ";
                    curWord = "";

                    break;
                case ("cup"):     // union
                    statement += "UNION ";
                    curWord = "";

                    break;
                case ("cap"):   // intersection
                    statement += "INTERSECT ";
                    curWord = "";

                    break;
                case ("vee"):   // and
                    statement += "AND ";
                    curWord = "";

                    break;
                case ("land"):   // and - 2nd possibility
                    statement += "AND ";
                    curWord = "";

                    break;
                case("wedge"):  // or
                    statement += "OR ";
                    curWord = "";

                    break;
                case("lor"):  // or - 2nd possibility
                    statement += "OR ";
                    curWord = "";

                    break;
                case("-"):  // or - 2nd possibility     TODO
                    statement += "EXCEPT ";
                    curWord = "";

                    break;
                case ("leftouterjoin"):
                    statement += "LOJ ";
                    curWord = "";

                    break;
                case ("rightouterjoin"):
                    statement += "ROJ ";
                    curWord = "";

                    break;
                case ("fullouterjoin"):
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
        String cols = "";
        String group = "";

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

                        if (!group.equals("")) {
                            sql += "GROUP BY " + group;
                        }
                    }
                    else {
                        sql += "SELECT * FROM ";
                        from = getFrom(commands, i);

                        i += from.split(" ").length + 1;
                        sql += from;

                        if (!group.equals("")) {
                            sql = sql.substring(0, sql.length()-1);
                            sql += " GROUP BY " + group;
                        }
                    }

//                    System.out.println("GROUP: " + group);
//                    if (!group.equals("")) {
//                        sql += "GROUP BY " + group;
//                    }

                    break;
                case ("GROUP"):
                    group = getGroup(commands, i);
//                    System.out.println("GRP: " + group + "\n");
                    i += group.split(" ").length;

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
        for (int j=i+1; j<comms.length; j++) {
            if (comms[j].contains(",")) {
                group += comms[j] + " ";
            }
            if (!comms[j].contains((","))) {
                group += comms[j];
                return group;
            }
        }
        return group;
    }

    public static void main(String[] args) throws Exception {
        Parser p = new Parser();
        String sampleInput1 = "\\Pi_{name}(Person)";

        // SELECT name, age  FROM (SELECT * FROM person INNER JOIN eats ) WHERE age > 16
        String sampleInput2 = "\\Pi_{name, age}(\\sigma_{age > 16}(Person \\bowtie Eats))";     // WORKS

        // SELECT * FROM (person INNER JOIN eats ) WHERE age > 10 OR name == 'sally' 
        String sampleInput3 = "\\sigma_{age > 10 || name == 'sally'}(Person \\bowtie Eats)";    // WORKS

        // SELECT * FROM (person NATURAL JOIN eats) group by age
        String sampleInput4 = "\\G_{name, age}(\\sigma(Person \\bowtie Eats))";    // WORKS
//        String sampleInput4 = "\\sigma(Person \\bowtie Eats)";    // WORKS

        // SELECT name, age  FROM (SELECT * FROM (eats INNER JOIN (person INTERSECT pizzeria ) ) ) WHERE age > 10 OR name == ' sally'
        String sampleInput5 = "\\Pi_{name, age}(\\sigma_{age > 10 || name == 'sally'}(Eats \\bowtie (Person \\cap Pizzeria)))";    // WORKS

        String latexToStr;
        String strToSql;

//        System.out.println("LaTeX Input 1: " + sampleInput1);
//        latexToStr = p.parseLatexToString(sampleInput1);
//        System.out.println("String Output: " + latexToStr);
//        strToSql = p.parseStringToSQL(latexToStr);
//        System.out.println("SQL Output: " + strToSql);

//        System.out.println("\nLaTeX Input 2: " + sampleInput2);
//        latexToStr = p.parseLatexToString(sampleInput2);
//        System.out.println("String Output: " + latexToStr);
//        strToSql = p.parseStringToSQL(latexToStr);
//        System.out.println("SQL Output: " + strToSql);

        System.out.println("\nLaTeX Input 3: " + sampleInput3);
        latexToStr = p.parseLatexToString(sampleInput3);
        System.out.println("String Output: " + latexToStr);
        strToSql = p.parseStringToSQL(latexToStr);
        System.out.println("SQL Output: " + strToSql);

        System.out.println("\nLaTeX Input 4: " + sampleInput4);
        latexToStr = p.parseLatexToString(sampleInput4);
        System.out.println("String Output: " + latexToStr);
        strToSql = p.parseStringToSQL(latexToStr);
        System.out.println("SQL Output: " + strToSql);

//        System.out.println("\nLaTeX Input 5: " + sampleInput5);
//        latexToStr = p.parseLatexToString(sampleInput5);
//        System.out.println("String Output: " + latexToStr);
//        strToSql = p.parseStringToSQL(latexToStr);
//        System.out.println("SQL Output: " + strToSql);
    }
}
