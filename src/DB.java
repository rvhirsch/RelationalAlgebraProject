import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DB {

    private Connection connection;

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public DB () {
        this.connection = getDBConnection();
    }

    public void startCon () throws SQLException {
        connection.setAutoCommit(false);
        this.connection = getDBConnection();
    }

    public void endCon() throws SQLException {
        this.connection.close();
    }

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

    public ResultSet query (String query) throws SQLException {
        ResultSet rs = null;
        connection.setAutoCommit(false);
        PreparedStatement pstat = connection.prepareStatement(query);

        rs = pstat.executeQuery();

        return rs;
    }

    public void populateDatabase (String path) throws IOException, SQLException {
        this.startCon();
        File file = new File("files\\SimpleInput.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String curLine = "";

        while ((curLine = br.readLine()) != null) {
//            System.out.println(curLine);
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
            if (tup[1].equalsIgnoreCase("string")) {
                createQuery += "varchar(255)";
            } else {
                createQuery += tup[1];
            }
            insertQuery += tup[0];
            categories[0] = tup[1];
            for (int x = 1; x < categories.length; x++) {
                tup = categories[x].split("-");
                createQuery += ", ";
                createQuery += tup[0] + " ";
                if (tup[1].equalsIgnoreCase("string")) {
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
            for (int y = 1; y < categories.length; y++) {
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
                insertPS.executeUpdate();
                insertPS.close();
            }
        }
    }

    public String toString() {
        String toReturn = "";
        try {
            startCon();
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement = connection.prepareStatement("select *");
            ResultSet rs = selectPreparedStatement.executeQuery();
            System.out.println("H2 In-Memory Database inserted through PreparedStatement");
            while (rs.next()) {
                toReturn += "Id " + rs.getInt("id") + " Name " + rs.getString("name") + "\n";
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
    }
}
