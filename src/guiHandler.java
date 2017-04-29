import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class guiHandler {
    public guiHandler () {
    }

    final double INV = 2.5;

    private DB db;
    private DBInfo dbInfo;
    private queryResult curQR;

    private ObservableList<String> addTableColumnOptions;
    private ArrayList<String> tableNameOptions;
    private ObservableList<String> tableNameOptionsFX;

    //for the edit Database Menu
    private Stage editDBStage;
    private Scene editDBScene;
    private TabPane editDBTabPane;
        private Tab editDBAddTableTab;
        private Tab editDBAddRowTab;
        private Tab editDBClearTableTab;
        private Tab editDBClearRowTab;

    //addTable Objects
    private FlowPane editDBAddTableFlowPane;
        private HBox editDBAddTableOptionsHbox;
        private VBox editDBAddTableColVbox;
        private Label editDBAddTableLabel1;
        private Label editDBAddTableLabel2;
        private Button editDBAddTableButton;
        private TextField editDBAddTableTextField;
        private Spinner<Integer> editDBAddTableSpinner;

        private HBox editDBAddTableDefHbox;
        private TextField editDBAddTAbleDefTextField;
        private ComboBox editDBAddTableDefComboBox;
        private Label editDBAddTableDefLabel1;

    //clearTable Objects
    private FlowPane editDBClearTableFlowPane;
        private HBox editDBClearTableHbox;
        private Label editDBClearTableLabel1;
        private Button editDBClearTableButton;
        private ComboBox<String> editDBClearTableComboBox;

    //addRow Objects
    private FlowPane editDBAddRowFlowPane;
        private FlowPane editDBAddRowRowsFlowPane;
        private HBox editDBAddRowHbox;
        private Label editDBAddRowLabel1;
        private Label editDBAddRowLabel2;
        private ComboBox<String> editDBAddRowComboBox;
        private Button editDBAddRowButton1;
        private Spinner<Integer> editDBAddRowSpinner;

        private HBox editDBAddRowTempHbox;
        private TextField editDBAddRowTempTextField;
        private Label editDBAddRowTempLabel;


    private FlowPane editDBClearRowFlowPane;
        private HBox editDBClearRowOptionsHbox;
        private Label editDBClearRowLabel1;
        private Button editDBClearRowButton;
        private ComboBox<String> editDBClearRowComboBox;
        private TableView<String[]> editDBClearRowTableView;
        private VBox editDBClearRowCheckBoxVbox;
        private HBox editDBClearRowHbox;

        private CheckBox editDBClearRowTempCheckBox;




    @FXML private WebView webView;
    @FXML private WebEngine webEngine;

    @FXML private TabPane tabResultPane;
    @FXML private TabPane dbTabPane;
    @FXML private TableView dbTable;
    @FXML private TableView resultTableView;

    @FXML private TextField dbFileField;
    @FXML private TextArea logTextArea;
    @FXML private TextArea latexSourceTextArea;

    @FXML private Button clearButton;
    @FXML private Button clearTabButton;
    @FXML private Button executeButton;
    @FXML private Button populateDBButton;
    @FXML private Button getLatexSrcButton;
    @FXML private Button selectCalcButton;
    @FXML private Button projectCalcButton;
    @FXML private Button crossJoinCalcButton;
    @FXML private Button addTableButton;
    @FXML private Button editDBButton;
    @FXML private Button saveDBSource;
    @FXML private Button saveResultButton;



    private void initializeEditDBWindow() throws SQLException {
        //initialize objects for EditDB window
        editDBStage = new Stage();
        editDBTabPane = new TabPane();
        editDBScene = new Scene(editDBTabPane, 800, 600);

            //Flow panes
        editDBAddTableFlowPane = new FlowPane();
        editDBClearTableFlowPane = new FlowPane();
        editDBAddRowFlowPane = new FlowPane();
        editDBClearRowFlowPane = new FlowPane();

            //tabs
        editDBAddTableTab = new Tab();
        editDBAddRowTab = new Tab();
        editDBClearTableTab = new Tab();
        editDBClearRowTab = new Tab();

        //configure stylistic properties of objects
            //edit DB Window
        editDBStage.setTitle("Edit Database");
        editDBStage.initModality(Modality.APPLICATION_MODAL);
        editDBTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        editDBAddTableTab.setText("Add a Table");
        editDBAddRowTab.setText("Add a row");
        editDBClearTableTab.setText("Remove a Table");
        editDBClearRowTab.setText("Remove a Row");

        //initialize the 4 tabs
        initializeAddTableTab();
        initializeClearTableTab();
        initializeAddRowTab();
        initializeClearRowTab();

        //put together the objects
            //edit DB Window
        editDBStage.setScene(editDBScene);
        editDBAddTableTab.setContent(editDBAddTableFlowPane);
        editDBAddRowTab.setContent(editDBAddRowFlowPane);
        editDBClearTableTab.setContent(editDBClearTableFlowPane);
        editDBClearRowTab.setContent(editDBClearRowFlowPane);

        editDBTabPane.getTabs().addAll(editDBAddTableTab, editDBClearTableTab, editDBAddRowTab, editDBClearRowTab);
    }

    private void initializeAddTableTab(){
        //construct the objects
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 1);

        editDBAddTableOptionsHbox = new HBox();
        editDBAddTableColVbox = new VBox();
        editDBAddTableLabel1 = new Label("Table Name: ");
        editDBAddTableLabel2 = new Label("Number of Columns: ");
        editDBAddTableButton = new Button("Add Table");
        editDBAddTableTextField = new TextField();
        editDBAddTableSpinner = new Spinner<Integer>();
        addTableColumnOptions = FXCollections.observableArrayList("String", "Integer", "Boolean");
        editDBAddTableDefHbox = new HBox();
        editDBAddTAbleDefTextField = new TextField();
        editDBAddTableDefComboBox = new ComboBox(addTableColumnOptions);
        editDBAddTableDefLabel1 = new Label("Col 1: ");

        //put together the add table tab
        editDBAddTableOptionsHbox.getChildren().addAll(editDBAddTableButton, editDBAddTableLabel1, editDBAddTableTextField, editDBAddTableLabel2, editDBAddTableSpinner);
        editDBAddTableDefHbox.getChildren().addAll(editDBAddTableDefLabel1, editDBAddTAbleDefTextField, editDBAddTableDefComboBox);
        editDBAddTableFlowPane.getChildren().addAll(editDBAddTableOptionsHbox, editDBAddTableDefHbox);

        editDBAddTableDefLabel1.setPrefWidth(55);
        editDBAddTableDefLabel1.setTextAlignment(TextAlignment.CENTER);
        editDBAddTableDefLabel1.setAlignment(Pos.CENTER);

        editDBAddTableDefHbox.setAlignment(Pos.TOP_LEFT);
        editDBAddTableDefHbox.setPadding(new Insets(INV,INV,INV,INV));
        editDBAddTableDefHbox.setSpacing(INV);
        editDBAddTableDefHbox.setMargin(editDBAddTableDefLabel1, new Insets(INV,INV,INV,INV));
        editDBAddTableDefHbox.setMargin(editDBAddTAbleDefTextField, new Insets(INV,INV,INV,INV));
        editDBAddTableDefHbox.setMargin(editDBAddTableDefComboBox, new Insets(INV,INV,INV,INV));

        editDBAddTableFlowPane.setAlignment(Pos.TOP_LEFT);
        editDBAddTableFlowPane.setPadding(new Insets(INV,INV,INV,INV));
        editDBAddTableFlowPane.setMargin(editDBAddTableOptionsHbox, new Insets(INV,INV,INV,INV));
        editDBAddTableFlowPane.setMargin(editDBAddTableDefHbox, new Insets(INV,INV,INV,INV));

        editDBAddTableOptionsHbox.setAlignment(Pos.TOP_LEFT);
        editDBAddTableOptionsHbox.setPadding(new Insets(INV,INV,INV,INV));
        editDBAddTableOptionsHbox.setSpacing(INV);
        editDBAddTableOptionsHbox.setMargin(editDBAddTableButton, new Insets(INV,INV,INV,INV));
        editDBAddTableOptionsHbox.setMargin(editDBAddTableLabel1, new Insets(INV,INV,INV,INV));
        editDBAddTableOptionsHbox.setMargin(editDBAddTableTextField, new Insets(INV,INV,INV,INV));
        editDBAddTableOptionsHbox.setMargin(editDBAddTableLabel2, new Insets(INV,INV,INV,INV));
        editDBAddTableOptionsHbox.setMargin(editDBAddTableSpinner, new Insets(INV,INV,INV,INV));


        editDBAddTableSpinner.setValueFactory(valueFactory);

        //all the listeners and handlers
        editDBAddTableSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            //incremented spinner
            if (newValue > oldValue) {
//                System.out.println("Incremented to: " + newValue);

                ComboBox editDBAddTableTempComboBox = new ComboBox(addTableColumnOptions);
                HBox editDBAddTableTempHbox = new HBox();
                editDBAddTableTempHbox.setAlignment(Pos.TOP_LEFT);
                editDBAddTableTempHbox.setPadding(new Insets(INV,INV,INV,INV));
                editDBAddTableTempHbox.setSpacing(INV);

                TextField editDBAddTableTempTextField = new TextField();
                Label editDBAddTableTempLabel1 = new Label("Col " + newValue + ": ");
                editDBAddTableTempLabel1.setPrefWidth(55);
                editDBAddTableTempLabel1.setTextAlignment(TextAlignment.CENTER);
                editDBAddTableTempLabel1.setAlignment(Pos.CENTER);

                editDBAddTableTempHbox.getChildren().addAll(editDBAddTableTempLabel1, editDBAddTableTempTextField, editDBAddTableTempComboBox);
                editDBAddTableFlowPane.getChildren().add(editDBAddTableTempHbox);

                editDBAddTableTempHbox.setMargin(editDBAddTableTempLabel1, new Insets(INV,INV,INV,INV));
                editDBAddTableTempHbox.setMargin(editDBAddTableTempTextField, new Insets(INV,INV,INV,INV));
                editDBAddTableTempHbox.setMargin(editDBAddTableTempComboBox, new Insets(INV,INV,INV,INV));

                editDBAddTableFlowPane.setMargin(editDBAddTableTempHbox, new Insets(INV,INV,INV,INV));

            } else if (newValue < oldValue){
//                System.out.println("Decremented to: " + newValue);
                editDBAddTableFlowPane.getChildren().remove(editDBAddTableFlowPane.getChildren().size()-1);
            }
        });

        editDBAddTableButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (editDBAddTableTextField.getText().equalsIgnoreCase("")&&
                        (editDBAddTAbleDefTextField.getText().equalsIgnoreCase("")) &&
                        (editDBAddTableDefComboBox.getValue() == null)) {

                    popupMaker("Please make sure you have entered all fields");
                    return;
                }

                int num = editDBAddTableSpinner.getValue();

                String tableName = editDBAddTableTextField.getText();
                String[] colNames = new String[num];
                String[] colTypes = new String[num];
                int arrayIncrementer = 0;

                //check if table name is a duplicate name
                if (Police.isTableNameDup(tableName, dbInfo)) {
                    popupMaker("Table name: " + tableName + " already exists in the database. Please pick a name that does not.");
                    return;
                }

                for (int x = 1; x < editDBAddTableFlowPane.getChildren().size(); x++) {
                    if (editDBAddTableFlowPane.getChildren().get(x).getTypeSelector().equalsIgnoreCase("HBox")) {
                        if (editDBAddTableFlowPane.getChildren().get(x) != null) {
                            HBox tempHb = (HBox) editDBAddTableFlowPane.getChildren().get(x);

                            if (!(((TextField)tempHb.getChildren().get(1)).getText().equalsIgnoreCase("")) &&
                                    ((ComboBox)tempHb.getChildren().get(2)).getValue() != null) {

                                TextField tempTF2 = (TextField) tempHb.getChildren().get(1);
                                ComboBox tempCB = (ComboBox) tempHb.getChildren().get(2);

                                System.out.println(tempTF2.getText());

                                colNames[arrayIncrementer] = tempTF2.getText();
                                colTypes[arrayIncrementer] = tempCB.getValue().toString();
                            } else {
                                popupMaker("Please make sure you have entered all fields");
                                return;
                            }

                            if (!Police.isTextOnlyNoSpace(colNames[arrayIncrementer])) {
                                popupMaker("Please make sure your column name only have letters and do not contain spaces");
                                return;
                            }
                            arrayIncrementer += 1;
                        }
                    }
                }

                if (Police.isColNameDup(colNames)) {
                    popupMaker("Your columns must have unique names.");
                    return;
                }

                try {
                    db.addTable(tableName,colNames, colTypes);
                    updateAllDBDisplays();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                while (editDBAddTableSpinner.getValue() > 1) {
                    editDBAddTableSpinner.decrement();
                }

                editDBAddTableTextField.setText("");
                editDBAddTAbleDefTextField.setText("");
                editDBAddTableDefComboBox.valueProperty().set(null);

            }
        });
    }

    private void initializeClearTableTab() {
        //construct the objects
        editDBClearTableLabel1 = new Label("Choose a Table to Remove");
        editDBClearTableButton = new Button("Remove Table");
        editDBClearTableComboBox = new ComboBox<String>(tableNameOptionsFX);
        editDBClearTableHbox = new HBox();

        //put together the clear Table Tab
        editDBClearTableHbox.getChildren().addAll(editDBClearTableButton, editDBClearTableLabel1, editDBClearTableComboBox);
        editDBClearTableFlowPane.getChildren().add(editDBClearTableHbox);

        //set stylistic options
        editDBClearTableComboBox.setPrefWidth(100);

        editDBClearTableFlowPane.setAlignment(Pos.TOP_LEFT);
        editDBClearTableFlowPane.setPadding(new Insets(INV,INV,INV,INV));
        editDBClearTableFlowPane.setMargin(editDBClearTableHbox, new Insets(INV,INV,INV,INV));

        editDBClearTableHbox.setAlignment(Pos.TOP_LEFT);
        editDBClearTableHbox.setPadding(new Insets(INV,INV,INV,INV));
        editDBClearTableHbox.setSpacing(INV);
        editDBClearTableHbox.setMargin(editDBClearTableLabel1, new Insets(INV,INV,INV,INV));
        editDBClearTableHbox.setMargin(editDBClearTableButton, new Insets(INV,INV,INV,INV));
        editDBClearTableHbox.setMargin(editDBClearTableComboBox, new Insets(INV,INV,INV,INV));

        //All listeners and handlers
        editDBClearTableButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String tbName = editDBClearTableComboBox.getValue();
                if (tbName == null) {
                    popupMaker("Please select a table to remove");
                    return;
                }
                try {
//                    System.out.println("Table Name should be: " + tbName);
                    db.clearTable(tbName);
                    updateAllDBDisplays();
                    editDBClearTableComboBox.setItems(tableNameOptionsFX);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    popupMaker("Please select a table to remove");
                }
            }
        });
    }

    private void initializeAddRowTab() {
        //construct the objects
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);

        editDBAddRowLabel1 = new Label("Chose a Table to Add to");
        editDBAddRowButton1 = new Button("Add Row(s)");
        editDBAddRowLabel2 = new Label("Number of Rows to Insert");
        editDBAddRowSpinner = new Spinner<Integer>();
        editDBAddRowComboBox = new ComboBox<String>(tableNameOptionsFX);
        editDBAddRowHbox = new HBox();
        editDBAddRowRowsFlowPane = new FlowPane();
        editDBAddRowTempHbox = new HBox();


        //put together the add table tab
        editDBAddRowHbox.getChildren().addAll(editDBAddRowButton1, editDBAddRowLabel1, editDBAddRowComboBox, editDBAddRowLabel2, editDBAddRowSpinner);
        editDBAddRowFlowPane.getChildren().addAll(editDBAddRowHbox, editDBAddRowRowsFlowPane);

        //set Stylistic options
        editDBAddRowButton1.disableProperty().set(true);
        editDBAddRowSpinner.disableProperty().set(true);

        editDBAddRowRowsFlowPane.setAlignment(Pos.TOP_LEFT);
        editDBAddRowRowsFlowPane.setPadding(new Insets(INV,INV,INV,INV));
//        editDBAddRowRowsFlowPane.setPrefWrapLength(600);

        editDBAddRowHbox.setAlignment(Pos.TOP_LEFT);
        editDBAddRowHbox.setPadding(new Insets(INV,INV,INV,INV));
        editDBAddRowHbox.setSpacing(INV);
        editDBAddRowHbox.setMargin(editDBAddRowButton1, new Insets(INV,INV,INV,INV));
        editDBAddRowHbox.setMargin(editDBAddRowLabel1, new Insets(INV,INV,INV,INV));
        editDBAddRowHbox.setMargin(editDBAddRowComboBox, new Insets(INV,INV,INV,INV));
        editDBAddRowHbox.setMargin(editDBAddRowLabel2, new Insets(INV,INV,INV,INV));
        editDBAddRowHbox.setMargin(editDBAddRowSpinner, new Insets(INV,INV,INV,INV));

        editDBAddRowFlowPane.setAlignment(Pos.TOP_LEFT);
        editDBAddRowFlowPane.setPadding(new Insets(INV,INV,INV,INV));
        editDBAddRowFlowPane.setMargin(editDBAddRowHbox, new Insets(INV,INV,INV,INV));
        editDBAddRowFlowPane.setMargin(editDBAddRowRowsFlowPane, new Insets(INV,INV,INV,INV));

        editDBAddRowSpinner.setValueFactory(valueFactory);
        editDBAddRowButton1.setDisable(true);

        //all listeners and handlers
        editDBAddRowButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String tbName = editDBAddRowComboBox.getValue();
                int colNum = dbInfo.getNumOfColOfTable(tbName);

                String[] rowToAdd = new String[colNum];

                for (int x = 0; x < editDBAddRowRowsFlowPane.getChildren().size(); x++) {
                   HBox tempHb = (HBox)editDBAddRowRowsFlowPane.getChildren().get(x);
                    int rowToAddCounter = 0;
                   for (int y = 1; y < tempHb.getChildren().size(); y++) { //can start from index of one because first node is a label
                        if (y % 2 == 1) {   //if y is odd
                            TextField tempTF = (TextField) tempHb.getChildren().get(y);

                            if (tempTF.getText().equalsIgnoreCase("")) {
                                popupMaker("Please make sure you've entered all fields");
                                return;
                            }

                            if (rowToAddCounter <= colNum) {
                                rowToAdd[rowToAddCounter] = tempTF.getText();
                            }
                            rowToAddCounter += 1;
                        }
                   }

                   if (!Police.checkRowColTypes(rowToAdd, dbInfo.getColTypes(tbName))) {
                       popupMaker("Make sure you are using the right data types. All letters, no numbers for Strings, only numbers for Integers, and \"true\" or \"false\" for Boolean values");
                       return;
                   }

                    try {
                        db.addRow(tbName, rowToAdd);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    updateAllDBDisplays();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                editDBAddRowComboBox.setItems(tableNameOptionsFX);
                editDBAddRowComboBox.setValue(tableNameOptionsFX.get(0));
            }
        });

        //Spinner
        editDBAddRowSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            //incremented spinner
            if (newValue > oldValue) {
                System.out.println("Incremented to: " + newValue);

                String tbName = editDBAddRowComboBox.getValue();
                editDBAddRowTempHbox = new HBox();

                for (int x = 0; x < dbInfo.getNumOfColOfTable(tbName); x++) {
                    editDBAddRowTempLabel = new Label("Column " + Integer.toString(x+1) + ": ");

                    String type = "";
                    if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("varchar")) {
                        type = "Enter a String";
                    } else if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("integer")) {
                        type = "Enter an Integer";
                    } else if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("boolean")) {
                        type = "Enter a Boolean Value";
                    }

                    editDBAddRowTempTextField = new TextField();
                    editDBAddRowTempTextField.setPromptText(type);

                    editDBAddRowTempHbox.getChildren().addAll(editDBAddRowTempLabel, editDBAddRowTempTextField);

                    editDBAddRowTempHbox.setAlignment(Pos.TOP_LEFT);
                    editDBAddRowTempHbox.setPadding(new Insets(INV,INV,INV,INV));
                    editDBAddRowTempHbox.setSpacing(INV);
                    editDBAddRowTempHbox.setMargin(editDBAddRowTempLabel, new Insets(INV,INV, INV, INV));
                    editDBAddRowTempHbox.setMargin(editDBAddRowTempTextField, new Insets(INV, INV, INV, INV));
                }

                editDBAddRowRowsFlowPane.getChildren().add(editDBAddRowTempHbox);
                editDBAddRowRowsFlowPane.setMargin(editDBAddRowTempHbox, new Insets(INV,INV,INV,INV));
            } else if (newValue < oldValue){
                System.out.println("Decremented to: " + newValue);
                editDBAddRowRowsFlowPane.getChildren().remove(editDBAddRowRowsFlowPane.getChildren().size()-1);
            }
        });

        editDBAddRowComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                editDBAddRowSpinner.setDisable(false);
                editDBAddRowButton1.setDisable(false);

                while (editDBAddRowSpinner.getValue() > 1) {
                    editDBAddRowSpinner.decrement();
                }

                for (int x = 0; x < editDBAddRowRowsFlowPane.getChildren().size(); x++) {
//                    System.out.println("removing: " + editDBAddRowFlowPane.getChildren().get(x).toString());
                    editDBAddRowRowsFlowPane.getChildren().remove(x);
                }

                String tbName = newValue;
                if (tbName != null) {
                    editDBAddRowTempHbox = new HBox();

                    for (int x = 0; x < dbInfo.getNumOfColOfTable(tbName); x++) {
                        editDBAddRowTempLabel = new Label("Column " + Integer.toString(x + 1) + ": ");
                        editDBAddRowTempTextField = new TextField();

                        String type = "";
                        if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("varchar")) {
                            type = "Enter a String";
                        } else if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("integer")) {
                            type = "Enter an Integer";
                        } else if (dbInfo.getColTypes(tbName)[x].equalsIgnoreCase("boolean")) {
                            type = "Enter a Boolean Value";
                        }

                        editDBAddRowTempTextField.setPromptText(type);

                        editDBAddRowTempHbox.getChildren().addAll(editDBAddRowTempLabel, editDBAddRowTempTextField);

                        editDBAddRowTempHbox.setAlignment(Pos.TOP_LEFT);
                        editDBAddRowTempHbox.setPadding(new Insets(INV, INV, INV, INV));
                        editDBAddRowTempHbox.setSpacing(INV);
                        editDBAddRowTempHbox.setMargin(editDBAddRowTempLabel, new Insets(INV, INV, INV, INV));
                        editDBAddRowTempHbox.setMargin(editDBAddRowTempTextField, new Insets(INV, INV, INV, INV));

                    }

                    editDBAddRowRowsFlowPane.getChildren().add(editDBAddRowTempHbox);
                    editDBAddRowRowsFlowPane.setMargin(editDBAddRowTempHbox, new Insets(INV, INV, INV, INV));
                }
            }
        });

    }

    private void initializeClearRowTab() {
        //construct the objects
        editDBClearRowLabel1 = new Label("Select a Table: ");
        editDBClearRowButton = new Button("Clear Row(s)");
        editDBClearRowComboBox = new ComboBox<String>(tableNameOptionsFX);
        editDBClearRowOptionsHbox = new HBox();
        String curTbName = "";

        //put together the clear row tab
        editDBClearRowOptionsHbox.getChildren().addAll(editDBClearRowButton, editDBClearRowLabel1, editDBClearRowComboBox);
        editDBClearRowFlowPane.getChildren().add(editDBClearRowOptionsHbox);

        //set the stylistic options
        editDBClearRowButton.disableProperty().set(true);

        editDBClearRowOptionsHbox.setAlignment(Pos.TOP_LEFT);
        editDBClearRowOptionsHbox.setPadding(new Insets(INV,INV,INV,INV));
        editDBClearRowOptionsHbox.setMargin(editDBClearRowButton, new Insets(INV,INV,INV,INV));
        editDBClearRowOptionsHbox.setMargin(editDBClearRowLabel1, new Insets(INV,INV,INV,INV));
        editDBClearRowOptionsHbox.setMargin(editDBClearRowComboBox, new Insets(INV,INV,INV,INV));
        editDBClearRowOptionsHbox.setSpacing(INV);

        editDBClearRowFlowPane.setAlignment(Pos.TOP_LEFT);
        editDBClearRowFlowPane.setPadding(new Insets(INV,INV,INV,INV));
        editDBClearRowFlowPane.setMargin(editDBClearRowOptionsHbox, new Insets(INV,INV,INV,INV));

        //All listeners and handlers
        editDBClearRowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String tbName  = editDBClearRowComboBox.getValue();
                int numRows = editDBClearRowCheckBoxVbox.getChildren().size();
                int numCols = editDBClearRowTableView.getColumns().size();
                Boolean[] rowsToClear = new Boolean[numRows];
                String[] colNames = new String[numCols];
                String comboValue = editDBClearRowComboBox.getValue();

                for (int y = 0; y < numCols; y++) {
                    colNames[y] = editDBClearRowTableView.getColumns().get(y).getText();
                }

                for (int x = 0; x < numRows; x++) {
                    CheckBox tempcb = (CheckBox)editDBClearRowCheckBoxVbox.getChildren().get(x);

                    if (tempcb.isSelected()) {
                        rowsToClear[x] = true;
                    } else {
                        rowsToClear[x] = false;
                    }
                }

                if(!Arrays.asList(rowsToClear).contains(true)) {
                    popupMaker("Please select atleast one row to remove");
                    return;
                }

                for (int y = 0; y < rowsToClear.length; y++) {
                    if (rowsToClear[y] == true) {
                        Object[][] data = {colNames,editDBClearRowTableView.getItems().get(y)};

                        System.out.println(Arrays.deepToString(data));

                        try {
                            db.clearRow(tbName,data);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                try {
                    updateAllDBDisplays();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                editDBClearRowComboBox.setItems(tableNameOptionsFX);
                editDBClearRowComboBox.setValue(comboValue);

            }
        });

        editDBClearRowComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println("Activated ComboBox!");

                editDBClearRowButton.disableProperty().set(false);

                if (editDBClearRowFlowPane.getChildren().size() > 1) {
                    editDBClearRowFlowPane.getChildren().remove(1,editDBClearRowFlowPane.getChildren().size());
                }

                try {
                    int numRows = 0;
                    queryResult[] qr = db.getEverything();
                    String tbName = newValue;

                    if (qr.length == 0) {

                    } else {
                        editDBClearRowTableView = new TableView<>();
                        editDBClearRowTableView.setEditable(true);

                        int index = 0;

                        for (int x = 0; x < qr.length; x++) {
                            if (qr[x].getName().equalsIgnoreCase(tbName)) {
                                index = x;
                            }
                        }
                        numRows = qr[index].getData().length-1;

                        String[][] dataArray = qr[index].getData();
                        ObservableList<String[]> data = FXCollections.observableArrayList();
                        data.addAll(Arrays.asList(dataArray));
                        data.remove(0);

                        for (int y = 0; y < dataArray[0].length; y++) {
                            TableColumn tc = new TableColumn(dataArray[0][y]);
                            final int colNo = y;

                            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                                @Override
                                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                    return new SimpleStringProperty((p.getValue()[colNo]));
                                }
                            });
                            editDBClearRowTableView.getColumns().add(tc);
                        }
                        editDBClearRowTableView.setItems(data);
                    }

                    editDBClearRowCheckBoxVbox = new VBox();
                    editDBClearRowCheckBoxVbox.setAlignment(Pos.TOP_CENTER);
                    editDBClearRowCheckBoxVbox.setSpacing(1.5);
                    editDBClearRowCheckBoxVbox.setPadding(new Insets(24,INV,INV,INV));

                    editDBClearRowHbox = new HBox();
                    editDBClearRowHbox.setAlignment(Pos.TOP_LEFT);
                    editDBClearRowHbox.setSpacing(INV);
                    editDBClearRowHbox.setPadding(new Insets(INV,INV,INV,INV));

                    for (int z = 0; z < numRows; z++) {
                        editDBClearRowCheckBoxVbox.getChildren().add(new CheckBox());
                        editDBClearRowCheckBoxVbox.setMargin(
                                editDBClearRowCheckBoxVbox.getChildren().get(
                                        editDBClearRowCheckBoxVbox.getChildren().size()-1),
                                new Insets(INV,INV,INV,INV));
                    }

                    editDBClearRowHbox.getChildren().addAll(editDBClearRowCheckBoxVbox,editDBClearRowTableView);

                    editDBClearRowHbox.setMargin(editDBClearRowCheckBoxVbox, new Insets(INV,INV,INV,INV));
                    editDBClearRowHbox.setMargin(editDBClearRowTableView, new Insets(INV,INV,INV,INV));

                    editDBClearRowFlowPane.getChildren().add(editDBClearRowHbox);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void refreshDBInfo() throws SQLException{
        this.dbInfo = db.createDBInfoObject();
    }

    private void addLog(String toAdd) {
        logTextArea.setText(logTextArea.getText() + "\n" + "=> " + toAdd);
    }

    private void setResultTable(queryResult qr) throws Exception {
        if (qr == null) {
        } else {
            TableView<String[]> table = new TableView<>();
            table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

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

            if (tabResultPane.getTabs() != null) {
                if (tabResultPane.getTabs().get(0).getText().equalsIgnoreCase("Lorem")) {
                    tabResultPane.getTabs().remove(0);
                }
            }

            String num = "";
            switch (tabResultPane.getTabs().size()+1) {
                case(1):
                    num = "One";
                    break;
                case(2):
                    num = "Two";
                    break;
                case(3):
                    num = "Three";
                    break;
                case(4):
                    num = "Four";
                    break;
                case(5):
                    num = "Five";
                    break;
                case(6):
                    num = "Six";
                    break;
                case(7):
                    num = "Seven";
                    break;
                case(8):
                    num = "Eight";
                    break;
                case(9):
                    num = "Nine";
                    break;
                case(10):
                    num = "Ten";
                    break;
                case(11):
                    num = "Eleven";
                    break;
                case(12):
                    num = "Twelve";
                    break;
                case(13):
                    num = "Thirteen";
                    break;
                case(14):
                    num = "Fourteen";
                    break;
                case(15):
                    num = "Fifteen";
                    break;
                default:
                    num = Integer.toString(tabResultPane.getTabs().size());
            }

            Tab tab = new Tab(num);
            tab.setContent(resultTableView);

            if (tabResultPane.getTabs().size() >= 15) {
                tabResultPane.getTabs().remove(tabResultPane.getTabs().size()-1);
            }

            tabResultPane.getTabs().add(tab);
            tabResultPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
            tabResultPane.getTabs().get(0).setClosable(false);
        }
    }

    private void setDBTabPane() throws Exception {
        //remove everything on the pane currently there
        while (dbTabPane.getTabs().size() > 0) {
            dbTabPane.getTabs().remove(0);
        }

        //get database dump
        queryResult[] qr = db.getEverything();

        if (qr.length == 0) {
            Tab tab = new Tab("Lorem");
            TableView tableView = new TableView();

            TableColumn tabCol = new TableColumn("Ipsum");
            TableColumn tabCol2 = new TableColumn("Dolar");
            TableColumn tabCol3 = new TableColumn("Sit");
            TableColumn tabCol4 = new TableColumn("Amet");
            tableView.getColumns().addAll(tabCol, tabCol2, tabCol3, tabCol4);

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

    private void updateTableNameOptions() throws SQLException {
        tableNameOptions.clear();
        tableNameOptionsFX.clear();

        for (int x = 0; x < dbInfo.getTBNames().size(); x++) {
            tableNameOptions.add(dbInfo.getTBNames().get(x));
        }

        tableNameOptionsFX.addAll(tableNameOptions);

//        System.out.println("utno: " + Arrays.toString(tableNameOptions.toArray()));
    }

    private void updateAllDBDisplays() throws Exception{
        refreshDBInfo();
        updateTableNameOptions();
        setDBTabPane();
    }

    @FXML private void popupMaker(String str) {
        //initialize objects
//       String str = "This is a test of the warning system, please remain calm";
       Stage popupStage = new Stage();
       FlowPane popupFlowPane = new FlowPane();
       Scene popupScene = new Scene(popupFlowPane, 400, 250);
       VBox vbox = new VBox();
       Button okButton = new Button("Close and Continue");
       Text wMessage = new Text(str);

       //set stylistic options
       //text
       wMessage.setWrappingWidth(popupFlowPane.getWidth()-20);
       wMessage.setTextAlignment(TextAlignment.CENTER);

       //vbox
       vbox.setMargin(wMessage, new Insets(5,5,5,5));
       vbox.setMargin(okButton, new Insets(5,5,5,5));
       vbox.setPadding(new Insets(5,5,5,5));
       vbox.setSpacing(5);
       vbox.setAlignment(Pos.TOP_CENTER);

       //flowpane
       popupFlowPane.setAlignment(Pos.TOP_CENTER);
       popupFlowPane.setMargin(vbox, new Insets(5,5,5,5));
       popupFlowPane.setPadding(new Insets(5,5,5,5));

       popupStage.setTitle("Warning: ");
       popupStage.initModality(Modality.APPLICATION_MODAL);

       //put together the window
       vbox.getChildren().addAll(wMessage, okButton);
       popupFlowPane.getChildren().add(vbox);
       popupStage.setScene(popupScene);


       //listeners and handlers
       okButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override public void handle(ActionEvent e) {
                popupStage.close();
           }
       });

       popupStage.showAndWait();

    }

    @FXML private void initialize() throws Exception {
        //initialize database
        this.db = new DB();
        this.dbInfo = new DBInfo();
        setDBTabPane();

        tableNameOptions = new ArrayList<String>();
        tableNameOptionsFX = FXCollections.observableArrayList();

        latexSourceTextArea.setWrapText(true);

       populateDBButton.disableProperty().bind(Bindings.isEmpty(dbFileField.textProperty()));


        //initialize the webview and webengine
        String filePath = "./src/webStuff/app/html/index.html";
        webView.getEngine().load(new File(filePath).toURI().toURL().toExternalForm());
        this.webEngine = webView.getEngine();

        TableView tableView = new TableView();

        TableColumn tabCol = new TableColumn("Ipsum");
        TableColumn tabCol2 = new TableColumn("Dolar");
        TableColumn tabCol3 = new TableColumn("Sit");
        TableColumn tabCol4 = new TableColumn("Amet");
        tableView.getColumns().addAll(tabCol, tabCol2, tabCol3, tabCol4);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        Tab tab = new Tab("Lorem");
        tab.setContent(tableView);

        tabResultPane.getTabs().add(tab);
    }

    @FXML private void populateDatabaseButton() throws IOException{
        try {
            db.clearDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String dberror = null;
        try {
            dberror = db.populateDatabase(dbFileField.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dberror != null) {
                popupMaker("Was unable to load your database file, error in: \n\"" + dberror + "\"");
                return;
            }
        try {
            updateAllDBDisplays();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addLog("Cleared and Populated Database from File: " + dbFileField.getText());
    }

    @FXML private void loadDBButton() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Database Text File:");
        File file = fc.showOpenDialog(dbTabPane.getScene().getWindow());

        if (file != null) {
            dbFileField.setText(file.getAbsolutePath());
        }
        addLog("Set Input File To: " + file.getAbsolutePath());
        try {
            populateDatabaseButton();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @FXML private void clearDBButton() throws Exception {
        db.clearDatabase();
        setDBTabPane();
        addLog("Cleared The Database");
    }

    @FXML private void clearButton() throws Exception {
        System.out.println(webEngine.executeScript("addName()"));
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

    @FXML private void clearResultTabButton() {
        tabResultPane.getTabs().remove(0,tabResultPane.getTabs().size());

        TableView tableView = new TableView();

        TableColumn tabCol = new TableColumn("Ipsum");
        TableColumn tabCol2 = new TableColumn("Dolar");
        TableColumn tabCol3 = new TableColumn("Sit");
        TableColumn tabCol4 = new TableColumn("Amet");
        tableView.getColumns().addAll(tabCol, tabCol2, tabCol3, tabCol4);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        Tab tab = new Tab("Lorem");
        tab.setContent(tableView);

        tabResultPane.getTabs().add(tab);
    }

    @FXML private void executeButton() throws Exception {
//        System.out.println(webEngine.executeScript("getLatex()"));
        String ls = (String)webEngine.executeScript("getLatex()");
        ls = ls.replace("\\left(","(");
        ls = ls.replace("\\right)",")");
        ls = ls.replace(".","'");

//        System.out.println("ls: " + ls);

        Parser3 p = new Parser3(ls, dbInfo);

        String temp = p.sql;
        System.out.println("temp: " + temp);

//        String sampleQuery2 = "SELECT * FROM Person";
//        String sampleQuery = "SELECT name FROM (SELECT * FROM (Person NATURAL JOIN Eats) WHERE pizza='cheese' AND money<100)";
        curQR = db.query(temp);
//        setResultTable(db.query(sampleQuery));
//        System.out.println(db.qrToString(db.query(sampleQuery)));
        setResultTable(curQR);
    }

    @FXML private void openEditDBWindowButton() throws Exception{
        updateAllDBDisplays();
        this.initializeEditDBWindow();
        editDBStage.showAndWait();
   }

   @FXML private void getLatexSrc() {
       String ls = (String)webEngine.executeScript("getLatex()");
//       System.out.println(ls);
       latexSourceTextArea.setText(ls);
   }

   @FXML private void getEqImg() throws IOException {
       FileChooser fc = new FileChooser();
       fc.setTitle("Choose File To Save Log To:");
       FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG Files","png");
       fc.setSelectedExtensionFilter(filter);
       File file = fc.showSaveDialog(dbTabPane.getScene().getWindow());

        WritableImage snapshot = webView.snapshot(new SnapshotParameters(), null);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(snapshot, null);

        ImageIO.write(renderedImage, "png", file);
   }

   @FXML private void saveDBSrc() throws FileNotFoundException {
        String str = db.toString();

       FileChooser fc = new FileChooser();
       fc.setTitle("Choose File To Save Log To:");
       FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text Files","txt");
       fc.setSelectedExtensionFilter(filter);
       File file = fc.showSaveDialog(dbTabPane.getScene().getWindow());

       if (file != null) {
           PrintWriter writer = new PrintWriter(file.getAbsolutePath());
           writer.print(str);
           writer.close();
       }
   }

   @FXML private void saveResult() throws FileNotFoundException, SQLException {
       FileChooser fc = new FileChooser();
       fc.setTitle("Choose File To Save Log To:");
       FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text Files","txt");
       fc.setSelectedExtensionFilter(filter);
       File file = fc.showSaveDialog(dbTabPane.getScene().getWindow());

       if (file != null) {
           PrintWriter writer = new PrintWriter(file.getAbsolutePath());
           writer.print(db.qrToString(curQR));
           writer.close();
       }
   }
}
