import org.h2.command.Prepared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Object[]> data = new ArrayList<Object[]>();

        //load the categories into the category array
        for (int x = 1; x <= rsmd.getColumnCount(); x++) {
            columnNames[x-1] = rsmd.getColumnName(x);
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
        return new queryResult(columnNames, data.toArray(new Object[data.size()][]));
    }

    /**
     * Populates the database from a file   |   Currently supports int and varchar(255) types
     * @param path   -  String of file path to file to input
     * @throws IOException
     * @throws SQLException
     */
    public void populateDatabase (String path) throws IOException, SQLException {
        //setup the file reader objects
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String curLine = "";

        while ((curLine = br.readLine()) != null) {     //main loop that reads in table titles and categories
//            System.out.println(curLine);
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

            //get the categories for the table, add them to the create and insert strings
            //set the category array to only the category names for later use
            String[] categories = curLine.split(" ");
            String[] tup = categories[0].split("-");
            createQuery += tup[0] + " ";

            if (tup[1].equalsIgnoreCase("string")) {    //if the data type is a string, record varchar(255)
                createQuery += "varchar(255)";
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

//            System.out.println(createQuery);
//            System.out.println(insertQuery);

            //insert the table into the database
            createPS = connection.prepareStatement(createQuery);
            createPS.executeUpdate();
            createPS.close();

//            System.out.println(Arrays.toString(categories));

            //now that we know the general form of the inserts, we can loop and substitute all the values
            //get the values to insert into the table
            while (!(curLine = br.readLine()).contains("**")) {
//                System.out.println(curLine);
                String[] entries = curLine.split(" ");
                insertPS = connection.prepareStatement(insertQuery);

                for (int z = 0; z < entries.length; z++) {
                    switch(categories[z]) {
                        case("int"):
                            insertPS.setInt(z+1,Integer.parseInt(entries[z]));
                            break;
                        case("string"):
                            insertPS.setString(z+1, entries[z]);
                            break;
                    }
                }
                //insert the updates
//                System.out.println(insertQuery);
                insertPS.executeUpdate();
                insertPS.close();
            }
        }
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




    public static void main(String[] args) throws Exception {
        String filePath = "files\\SimpleInput.txt";
        DB db = new DB();
        db.populateDatabase(filePath);
        System.out.println(db.toString());
        System.out.println("-----------------------------------");
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
    }
}
