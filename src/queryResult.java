public class queryResult {
    private String[] columnNames;
    private Object[][] data;
    private String name;

    public queryResult (String[] columnNames, Object[][] data){
        this.columnNames = columnNames;
        this.data = data;
        this.name = "null";
    }

    public String[] getColumns () {
        return this.columnNames;
    }
    public Object[][] getData () {
        return this.data;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}