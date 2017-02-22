public class queryResult {
    private String[] columnNames;
    private String[][] data;
    private String name;

    public queryResult (String[] columnNames, Object[][] data){
        this.columnNames = columnNames;
        this.data = format(data);
        this.name = "null";
    }

    private String[][] format(Object[][] data) {
        String[][] toReturn = new String[data.length+1][columnNames.length];
        toReturn[0] = this.columnNames;
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y <data[x].length; y++) {
                toReturn[x+1][y] = data[x][y].toString();
            }
        }
        return toReturn;
    }
    public String[] getColumns () {
        return this.columnNames;
    }
    public String[][] getData () {
        return this.data;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}