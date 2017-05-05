
import org.h2.jdbc.JdbcPreparedStatement;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Josh
 */
public class DB {
    private Connection connection;

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    /**
     * Constructor
     */
    public DB () {
        this.connection = getDBConnection();
    }

    public void addTable(String tableName, String[] colNames, String[] colTypes) throws SQLException{
        PreparedStatement createPS = null;
        String createQuery = "CREATE TABLE " + tableName + " (";

        for (int x = 0; x < colNames.length; x++) {
            createQuery += colNames[x] + " ";

            //parse the datatype for each col
            if (colTypes[x].equalsIgnoreCase("string")) {
                createQuery += "varchar(255)";
            } else if (colTypes[x].equalsIgnoreCase("boolean")) {
                createQuery += "BOOLEAN";
            } else if (colTypes[x].equalsIgnoreCase("Integer")) {
                createQuery += "INT";
            }

            //add a comma, if need
            if (x < colNames.length-1) {
                createQuery += ", ";
            }
        }
        createQuery += ")";

        System.out.println("query: " + createQuery);

        createPS = connection.prepareStatement(createQuery);
        createPS.executeUpdate();
        createPS.close();
    }

    public void addRow(String tableName, Object[] row) throws SQLException{
        PreparedStatement insertPS = null;
        String insertQ = "INSERT INTO " + tableName + " VALUES (";

        for (int x = 0; x < row.length; x++) {
            if (row[x].getClass().equals(String.class)) {
                insertQ += "'" + row[x] + "'";
            } else {
                insertQ += row[x].toString();
            }

            if (x < row.length-1) {
                insertQ += ", ";
            }
        }
        insertQ += ")";
//        System.out.println("Statement: " + insertQ);
        insertPS = connection.prepareStatement(insertQ);
        insertPS.executeUpdate();
        insertPS.close();
    }

    public void clearTable(String tableName) throws SQLException{
        PreparedStatement dropPS = null;
        String dropQ = "Drop TABLE " + tableName;

        if (tableName != null) {
            dropPS = connection.prepareStatement(dropQ);
            dropPS.executeUpdate();
            dropPS.close();
        } else {
            System.out.println("Null Table Name!");
        }
    }

    /**
     *
     * @param tableName
     * @param values    2d array, first row is column names, second row is values
     */
    public void clearRow(String tableName, Object[][] values) throws SQLException{
        PreparedStatement dropPS = null;
        String dropQ = "DELETE FROM " + tableName + " WHERE ";

        System.out.println("Values Length: " + values[0].length);

        for (int x = 0; x < values[0].length; x++) {
            dropQ += values[0][x];

            if (values[1][x].getClass().equals(Integer.class)) {
                dropQ += " = " + values[1][x];
            } else {
                dropQ += " = '" + values[1][x] + "'";
            }
            if (x < values[0].length-1) {
                System.out.println("Made it, x: " + x);
                dropQ += " AND ";
            }
        }
        System.out.println("String: " + dropQ);

        dropPS = connection.prepareStatement(dropQ);
        dropPS.execute();
        dropPS.close();

    }

    /**
     * Initializes connection with the database
     * @throws SQLException
     */
    private void startCon () throws SQLException {
        connection.setAutoCommit(false);
        this.connection = getDBConnection();
    }

    /**
     * Ends the connnection with the database
     * @throws SQLException
     */
    private void endCon() throws SQLException {
        this.connection.close();
    }

    /**
     * Gets/creates the connection object to pass to other methods to create the database
     * @return
     */
    private Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    /**
     * Given a formated string, execute the query and return the object containing the result set
     * @param query -   Formated SQL Query to execute
     * @return queryResult -   Object containing arrays of results
     * @throws SQLException
     */
    public queryResult query (String query) throws SQLException {
        connection.setAutoCommit(false);

        ResultSet rs = null;
        PreparedStatement pstat = connection.prepareStatement(query);
        rs = pstat.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        String[] columnNames = new String[rsmd.getColumnCount()];
        String[] colTypes = new String[rsmd.getColumnCount()];
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        //load the categories into the category array
        for (int x = 1; x <= rsmd.getColumnCount(); x++) {
            columnNames[x-1] = rsmd.getColumnName(x);
            colTypes[x-1] = rsmd.getColumnTypeName(x);
        }

        //load the data into the data array
        while (rs.next()) {
            Object[] dataSubArray = new Object[rsmd.getColumnCount()];
            for (int y = 1; y <= rsmd.getColumnCount(); y++) {
                dataSubArray[y-1] = rs.getObject(y);
            }
            data.add(dataSubArray);
        }

        rs.close();
        pstat.close();
        return new queryResult(columnNames, colTypes, data.toArray(new Object[data.size()][]));
    }

    /**
     * Populates the database from a file   |   Currently supports int and varchar(255) types
     * @param path   -  String of file path to file to input
     * @throws IOException
     * @throws SQLException
     */
    public String populateDatabase(String path) throws IOException, SQLException {
        //setup the file reader objects
        File file = new File(path);
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(file));
        String curLine = "";

        while ((curLine = br.readLine()) != null) {     //main loop that reads in table titles and categories
            //            System.out.println(curLine);
            String curStream = curLine;
            //objects to create queries
            PreparedStatement createPS = null;
            PreparedStatement insertPS = null;
            String createQuery = "";
            String insertQuery = "";

            //get the title for the start of the table creation and insertion strings
            createQuery = "Create Table " + curLine + "(";
            insertQuery = "INSERT INTO " + curLine + "(";

            //get the table categories
            curLine = br.readLine();

            curStream += " " + curLine;

            //get the categories for the table, add them to the create and insert strings
            //set the category array to only the category names for later use
            String[] categories = curLine.split(" ");
            String[] tup = categories[0].split("-");
            createQuery += tup[0] + " ";

            if (tup[1].equalsIgnoreCase("string")) {    //if the data type is a string, record varchar(255)
                createQuery += "varchar(255)";
            } else if (tup[1].equalsIgnoreCase("boolean")) {
                createQuery += "BOOLEAN";
            } else {
                createQuery += tup[1];
            }
            insertQuery += tup[0];
            categories[0] = tup[1];
            for (int x = 1; x < categories.length; x++) {   //loops through categories to seperate name from type and inserts them into the creation string
                tup = categories[x].split("-");        //tup[0] = the name of the category, tup[1] = the data type of the category
                createQuery += ", ";
                createQuery += tup[0] + " ";
                if (tup[1].equalsIgnoreCase("string")) {    //if the data type is a string, record varchar(255)
                    createQuery += "varchar(255)";
                } else if (tup[1].equalsIgnoreCase("boolean")) {
                    createQuery += "BOOLEAN";
                } else {
                    createQuery += tup[1];
                }


                insertQuery += ", ";
                insertQuery += tup[0];

                categories[x] = tup[1];
            }
            createQuery += ")";
            insertQuery += ") values(?";
            for (int y = 1; y < categories.length; y++) {   //adds
                insertQuery += ",?";
            }
            insertQuery += ")";

            //insert the table into the database
            createPS = connection.prepareStatement(createQuery);
            createPS.executeUpdate();
            createPS.close();


            //now that we know the general form of the inserts, we can loop and substitute all the values
            //get the values to insert into the table
            while (!(curLine = br.readLine()).contains("**")) {
                //                System.out.println(curLine);
                curStream += " " + curLine;
                String[] entries = curLine.split(" ");

                insertPS = connection.prepareStatement(insertQuery);
//                System.out.println("1: " + insertPS.toString());

                for (int z = 0; z < entries.length; z++) {
                    switch (categories[z]) {
                        case ("int"):
                            try {
                                insertPS.setInt(z + 1, Integer.parseInt(entries[z]));
                            } catch (Exception e) {
                                return curStream;
                            }
                            break;
                        case ("string"):
                            try {
                                insertPS.setString(z + 1, entries[z]);
                            } catch (Exception e) {
                                return curStream;
                            }
                            break;
                        case ("boolean"):
                            try {
                                insertPS.setString(z + 1, entries[z]);
                            } catch (Exception e) {
                                return curStream;
                            }
                            break;
                    }
                }
                //insert the updates
//                System.out.println("2: " + insertPS.toString());
                insertPS.executeUpdate();
                insertPS.close();
            }
        }
        return null;
    }

    /**
     * Clears the database, essentially reseting it to defaults
     */
    public void clearDatabase() throws SQLException{
        //get a list of all table names
        DatabaseMetaData dbmd = connection.getMetaData();
        ResultSet dbmdrs = dbmd.getTables(null, null, "%"  , new String[] {"TABLE"});
        PreparedStatement dropPS = null;
        String dropQ = "";

        //iterate through a list of table names to drop them all
        while (dbmdrs.next()) {
            //create the Prepared Statement to drop the table
            dropQ = "DROP TABLE " + dbmdrs.getString(3);
            dropPS = connection.prepareStatement(dropQ);

            dropPS.executeUpdate();
        }
    }

    public queryResult[] getEverything() throws SQLException{
        ArrayList<queryResult> toReturn = new ArrayList<queryResult>();
        DatabaseMetaData dbmd = connection.getMetaData();
        ResultSet dbmdrs = dbmd.getTables(null, null, "%", new String[] {"TABLE"});
        queryResult temp = null;
        String selectQ = "";

        while (dbmdrs.next()) {
            selectQ = "SELECT * FROM " + dbmdrs.getString(3);
            temp = query(selectQ);
            temp.setName(dbmdrs.getString(3));
            toReturn.add(temp);
        }
        return toReturn.toArray(new queryResult[toReturn.size()]);
    }

    public DBInfo createDBInfoObject() throws SQLException{
        DBInfo toReturn = new DBInfo();

        queryResult[] qrs = this.getEverything();

        for (int x = 0; x < qrs.length; x++) {
            toReturn.addTable(qrs[x].getName(), qrs[x].getColumns(), qrs[x].getColTypes());
        }
        return toReturn;
    }

    public String qrToString(queryResult qr) throws SQLException {
        String str = "";

        //columns
        for (int x = 0; x < qr.getData().length; x++) {
            for (int y = 0; y < qr.getData()[x].length; y++) {
                str += qr.getData()[x][y] + " ";
            }
            str += "\n";
        }
        return str;
    }

    /**
     * Your typical toString method, doesn't print anything fancy, just plain text representing the database
     * @return  -   A string representing the database
     */
    public String toString() {
        String toReturn = "";
        try {
            startCon();

            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet dbmdrs = dbmd.getTables(null, null, "%"  , new String[] {"TABLE"});
            ResultSet rs = null;
            ResultSetMetaData rsmd = null;

            while (dbmdrs.next()) {
                String selectQ = "SELECT * FROM ";
                selectQ += dbmdrs.getString(3);
                PreparedStatement ps = connection.prepareStatement(selectQ);
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
//                System.out.println(dbmdrs.getString(3));

                if (!toReturn.equalsIgnoreCase("")) {
                    toReturn += "\n" + dbmdrs.getString(3) + "\n";
                } else {
                    toReturn += dbmdrs.getString(3) + "\n";
                }

                for (int x = 1; x <= rsmd.getColumnCount(); x++) {
//                    if (x > 1) System.out.print(", ");
                    if (x > 1) toReturn += " ";

//                    System.out.print(rsmd.getColumnName(x));
                    toReturn += rsmd.getColumnName(x) + "-";

                    switch (rsmd.getColumnTypeName(x)) {
                        case("INTEGER"):
                            toReturn += "int";
                            break;
                        case("VARCHAR"):
                            toReturn += "string";
                            break;
                        case("BOOLEAN"):
                            toReturn += "boolean";
                            break;
                        default:
                    }
                }
//                System.out.println();
                    toReturn += "\n";

                while (rs.next()) {
                    for (int y = 1; y <= rsmd.getColumnCount(); y++) {
//                        if (i > 1) System.out.print(", ");
                        if (y > 1) toReturn += " ";
//                        System.out.print(rs.getString(y));
                        toReturn += rs.getString(y);
                    }
//                    System.out.println();
                    toReturn += "\n";
                }
//                System.out.println();
                toReturn += "**";
            }
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public boolean checkQuery(String query) {
        try {
            connection.setAutoCommit(false);
            ResultSet rs = null;
            PreparedStatement pstat = connection.prepareStatement(query);
            rs = pstat.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "/home/josh/Documents/Capstone/RelationalAlgebraProject/files/SimpleInput.txt";
        DB db = new DB();
        db.populateDatabase(filePath);
        System.out.println(db.toString());
        System.out.println("-----------------------------------");

//        queryResult[] qr = db.getEverything();
//        for (int x = 0; x < qr.length; x++) {
//            for (int y = 0; y < qr[x].getData().length; y++) {
//                for (int z = 0; z < qr[x].getData()[y].length; z++) {
//                    if (z > 0) {
//                        System.out.print(", ");
//                    }
//
//                    System.out.print(qr[x].getData()[y][z]);
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
//        System.out.println();
//        queryResult[] qr = db.getEverything();
//
//        for (int x = 0; x < qr.length; x++) {
//            queryResult temp = qr[x];
//            System.out.println("Table: " + temp.getName());
//            System.out.println("Columns: " + Arrays.toString(temp.getColumns()));
//            System.out.println("Data: " + Arrays.toString(temp.getData()));
//            System.out.println();
//        }
//        System.out.println(db.toString());


//        String[] colNames = {"col1", "col2", "col3", "col4", "col5"};
//        String[] colNames2 = {"Lorem","test", "Ipsum","testtwo"};
//        String[] colTypes = {"Integer", "boolean", "string", "Integer", "string"};
//        String[] colTypes2 = {"String","Integer","String","Integer"};
//        Object[] row = {1, true, "test5", 4, "test3"};
//        Object[] row2 = {11, false, "test2", 44, "test4"};
//        db.addTable("TestTable", colNames2, colTypes2);
//
//        System.out.println();
//        System.out.println(db.toString());
//        System.out.println("-----------------------------------");
//
//        db.addRow("TestTable", row);
//        db.addRow("TestTable", row2);
//
//        System.out.println();
//        System.out.println(db.toString());
//        System.out.println("-----------------------------------");

        //        db.clearTable("TestTable");
//
//        System.out.println();
//        System.out.println("-----------------------------------");
//        System.out.println(db.toString());


//        DBInfo dbi = db.createDBInfoObject();
//
//        System.out.println("Size: " + dbi.getTBNames().size());
//        for (int x = 0; x < dbi.getTBNames().size(); x++) {
//            System.out.println(dbi.getTBNames().get(x));
//        }

//        System.out.println(dbi.getNumOfColOfTable("person"));



//        Object[][]values = {{"ID", "NAME","PIZZA"}, {3,"bill","meat"}};

//        System.out.println(Arrays.deepToString(values));

//        String dropQ = "DELETE FROM Eats WHERE name='bill';";
//        String addQ = "INSERT INTO TestTable VALUES (11, false, 'test2', 44, 'test4')";
//        String addQ2 = "INSERT INTO Eats VALUES (7, 'tom', 'pizzaz')";
//        System.out.println(dropQ);

//        PreparedStatement dropPS = db.connection.prepareStatement(dropQ);
//        dropPS.execute();
//        dropPS.close();

//        db.clearRow("Eats", values);

//        System.out.println(db.toString());

//        db.query("SELECT name FROM (SELECT * FROM (Person, Eats) WHERE age>18 OR loyal==true)");

    }
}
