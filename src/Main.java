
/**
 * Created by rvhirsch on 1/24/17.
 */
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private ArrayList<Rel> tables =  new ArrayList<Rel>();

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

    public ArrayList<Rel> getTableList() {
        return this.tables;
    }

    /**
     *  Returns a string representing the database
     */
    public String toString() {
        String toReturn = "";
        for (int x = 0; x < tables.size(); x++) {
            toReturn+= tables.get(x).getName() + "\n";
            toReturn+= tables.get(x).toString() + "\n";
        }
        return toReturn;
    }

    public void populateDatabase (File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String curLine = "";

        while ((curLine = br.readLine()) != null) {
            Rel rel = new Rel(curLine);

            curLine = br.readLine();
            String categories[] = curLine.split(" ");

            curLine = br.readLine();
            while (curLine.contains("*") == false && curLine != null) {
                Tup tup = new Tup();
                //System.out.println(curLine);
                String[] entries = curLine.split(" ");

                for (int x = 0; x < entries.length; x++) {
                    Attr attr = new Attr(entries[x], categories[x]);
                    tup.addAttr(attr);
                }

                try {
                    rel.insert(tup);
                } catch (IllegalInsertException e) {
                    e.printStackTrace();
                }
                curLine = br.readLine();
            }
            tables.add(rel);
        }
    }


    public static void main(String[] args) throws Exception{
        File file = new File("files\\SimpleInput.txt");
        Main main = new Main();
        System.out.println("Starting...");
        System.out.println();

        main.populateDatabase(file);
        System.out.println(main.toString());

        InputTree itree = new InputTree(main.getTableList());
        String testQ1 = "X person , eats *";
        String testQ2 = "σ age<18 X person , eats *";
        String testQ3 = "Π name σ age<18 X person , eats *";
        Rel resultRel = null;

        itree.read(testQ1);
        System.out.println(itree.toString());

        System.out.println(testQ1);
        System.out.println();
        resultRel = itree.execute();

        System.out.println("Resulting Rel:");
        System.out.println(resultRel);

        System.out.println("Ended!");
    }
}