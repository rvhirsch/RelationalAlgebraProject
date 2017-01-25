/**
 * Created by rvhirsch on 1/24/17.
 */
import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    private static ArrayList<Rel> tables =  new ArrayList<Rel>();

    /**
     * Adds a table to the database
     * @param table to add
     */
    private void addTable(Rel table) {
        this.tables.add(table);
    }

    /**
     * Removes a table from the database
     * @param table to remove
     */
    private void removeTable(Rel table) {
        this.tables.remove(table);
    }

    /**
     *  Returns a string representing the database
     */
    public String toString() {
        String toReturn = "";
        for (int x = 0; x < this.tables.size(); x++) {
            toReturn+=this.tables.get(x).getName() + "\n";
            toReturn+=this.tables.get(x).toString() + "\n";
        }
        toReturn+="\n\n";
        return toReturn;
    }

    public static boolean populateDatabase (File file) throws IOException {
        boolean finished = false;
        BufferedReader br = new BufferedReader(new FileReader(file));

        String curLine = null;

        //load the first line into the reader and check if its null
        while ((curLine = br.readLine()) != null) {
            Rel newRel = new Rel(curLine);

            curLine = br.readLine();
            String categories[] = curLine.split(" ");

            while ((curLine = br.readLine()) != "*") {
                Tup tup = new Tup();

                //Get the name of the table, 1 word on 1 line
                //get the name of the categories,
                    //load the line containing categories
                    //parse the line into an array, elements seperated by a space
                    //load the next line containing attributes and parse it by space
                    //the nth attribute has a col name of the nth element from the earlier array
                    //end this loop when we hit a "*"

                String entries[] = curLine.split(" ");

                for (int y = 0; y < entries.length; y++) {
                    Attr attr = new Attr(entries[y], categories[y]);
                    tup.addAttr(attr);
                }

                newRel.insert(tup);
            }
            tables.add(newRel);
        }

        br.close();

        return finished;
    }


    public static void main(String[] args) throws IOException{
        File file = new File("C:\\Users\\Josh\\Documents\\Capstone Project\\RelationalAlgebraProject\\src\\SimpleInput.txt");
        System.out.println("Starting...");
        System.out.println();

        //String test = "this is a test 8 9 89";
        //System.out.println(Arrays.toString(test.split(" ")));

        populateDatabase(file);


        System.out.println();
        System.out.println("Ended!");
    }
}
