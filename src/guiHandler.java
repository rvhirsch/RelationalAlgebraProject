import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;

public class guiHandler {
    public guiHandler () {
    }

    private DB db;

    @FXML private Pane resultPane;
    @FXML private TableView dbTable;
    @FXML private TableView resultTableView;
    @FXML private TabPane dbTabPane;
    @FXML private TextField dbFileField;
    @FXML private TextArea logTextArea;
    @FXML private Button executeButton;
    @FXML private Button populateDBButton;
    @FXML private WebView webView;
    @FXML private WebEngine webEngine;



    private void addLog(String toAdd) {
        logTextArea.setText(logTextArea.getText() + "\n" + "=> " + toAdd);
    }

    private void setResultTable(queryResult qr) throws Exception {
        TableView<String[]> table = new TableView<>();
        table.setEditable(true);

        if (qr != null) {
            String[][] dataArray = qr.getData();

            ObservableList<String[]> data = FXCollections.observableArrayList();
            data.addAll(Arrays.asList(dataArray));
            data.remove(0);

            for (int i = 0; i < dataArray[0].length; i++) {
                TableColumn tc = new TableColumn(dataArray[0][i]);
                final int colNo = i;

                tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                        return new SimpleStringProperty((p.getValue()[colNo]));
                    }
                });

                table.getColumns().add(tc);
            }
            table.setItems(data);
        }
        resultTableView = table;
        table.setStyle("-fx-effect: innershadow(three-pass-box, gray, 12 , 0.5, 1, 1)");
        resultPane.getChildren().add(resultTableView);
    }

    private void setDBTabPane() throws Exception {
        //remove everything on the pane currently there
        while (dbTabPane.getTabs().size() > 0) {
            dbTabPane.getTabs().remove(0);
        }

        //get database dump
        queryResult[] qr = db.getEverything();

        if (qr.length == 0) {
            Tab tab = new Tab();
            TableView tableView = new TableView();
            tab.setContent(tableView);
            dbTabPane.getTabs().add(tab);
        } else {
            //create and add the tabs to the pane
            for (int x = 0; x < qr.length; x++) {
                TableView<String[]> table = new TableView<>();
                table.setEditable(true);

                String[][] dataArray = qr[x].getData();

                ObservableList<String[]> data = FXCollections.observableArrayList();
                data.addAll(Arrays.asList(dataArray));
                data.remove(0);

                for (int i = 0; i < dataArray[0].length; i++) {
                    TableColumn tc = new TableColumn(dataArray[0][i]);
                    final int colNo = i;

                    tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                            return new SimpleStringProperty((p.getValue()[colNo]));
                        }
                    });

                    table.getColumns().add(tc);
                }
                table.setItems(data);
                Tab tab = new Tab();
                tab.setContent(table);
                tab.setText(qr[x].getName());
                dbTabPane.getTabs().add(tab);
            }
        }
    }

    @FXML private void initialize() throws Exception{
        //initialize database
        this.db = new DB();
        setDBTabPane();
        setResultTable(null);

        webEngine = webView.getEngine();

        URL url = getClass().getResource("testPage.html");
        webView.getEngine().load(url.toExternalForm());

        resultPane.setStyle("-fx-border-color: black");
    }

    @FXML private void populateDatabaseButton() throws Exception {
        db.clearDatabase();
        db.populateDatabase(dbFileField.getText());
        setDBTabPane();
        addLog("Cleared and Populated Database from File: " + dbFileField.getText());
    }

    @FXML private void loadDBButton() throws Exception {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Database Text File:");
        File file = fc.showOpenDialog(dbTabPane.getScene().getWindow());

        if (file != null) {
            dbFileField.setText(file.getAbsolutePath());
        }
        addLog("Set Input File To: " + file.getAbsolutePath());
    }

    @FXML private void clearDBButton() throws Exception {
        db.clearDatabase();
        setDBTabPane();
        addLog("Cleared The Database");
    }

    @FXML private void saveResultButton() {

    }

    @FXML private void saveLogButton() throws Exception {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose File To Save Log To:");
        File file = fc.showSaveDialog(dbTabPane.getScene().getWindow());

        if (file != null) {
            PrintWriter writer = new PrintWriter(file.getAbsolutePath());
            writer.print(logTextArea.getText());
            writer.close();
        }
    }

    @FXML private void executeButton() throws Exception {
        String sampleQuery = "SELECT * FROM Person";
        setResultTable(db.query(sampleQuery));
    }
}
