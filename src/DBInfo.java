import java.util.ArrayList;

public class DBInfo {
    public class TBInfo {
        private String name;
        private String[] colNames;
        private String[] colTypes;

        public String getName() {
            return this.name;
        }

        public String[] getColNames() {
            return this.colNames;
        }

        public String[] getColTypes() {
            return this.colTypes;
        }

        public TBInfo(String name, String[] colNames) {
            this.name = name;
            this.colNames = colNames;
//            this.colTypes = colTypes;
        }
    }

    private ArrayList<TBInfo> tables;
    private ArrayList<String> TBNames;
    private ArrayList<String> colNames;

    public DBInfo() {
        this.tables = new ArrayList<TBInfo>();
        this.TBNames = new ArrayList<String>();
        this.colNames = new ArrayList<String>();

    }

    /**
     * create and add a TBInfo object to the DBInfo object
     * @param name  Table name
     * @param colNames  Column names in Table
     */
    public void addTable (String name, String[] colNames) {
        this.tables.add(new TBInfo(name, colNames));
        this.extractData();
    }

    public ArrayList<String> getTBNames() {
        return this.TBNames;
    }

    public ArrayList<String> getColNames() {
        return this.colNames;
    }

    public int getNumOfColOfTable(String tbName) {
        for (int x = 0; x < tables.size(); x++) {
            if (tbName.equalsIgnoreCase(tables.get(x).getName())) {
                return tables.get(x).getColNames().length;
            }
        }
        return 0;
    }

    public String toString() {
        String toReturn = "Table Names: ";

        //get table names
        for (int x = 0; x < tables.size(); x++) {
            if (x > 0)  toReturn += ", ";

            toReturn += this.tables.get(x).getName();
        }

        //line break
        toReturn += "\n" + "Column Names: ";

        //get column names
        for (int y = 0; y < tables.size(); y++) {
            if (y > 0)  toReturn += ", ";

            for (int z = 0; z < this.tables.get(y).getColNames().length; z++) {
                if (z > 0)  toReturn += ", ";

                toReturn += this.tables.get(y).getColNames()[z];
            }
        }
        return toReturn;
    }

    private void extractData() {
        this.loadTBNames();
        this.loadColNames();
    }

    private void loadTBNames() {
        this.TBNames = new ArrayList<String>();
        for (int x = 0; x < this.tables.size(); x++) {
            this.TBNames.add(this.tables.get(x).getName());
        }
    }

    private void loadColNames() {
        //get total number of columns
        int totalCols = 0;
        for (int x = 0; x < this.tables.size(); x++) {
            for (int y = 0; y < this.tables.get(x).getColNames().length; y++) {
                this.colNames.add(this.tables.get(x).getColNames()[y]);
            }
        }
    }
}
