import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Josh on 2/15/2017.
 */
public class GUI {
    private JButton projectButton;
    private JButton renameButton;
    private JButton naturalJoinButton;
    private JButton fOJButton;
    private JButton lOJButton;
    private JButton rOJButton;
    private JButton exceptButton;
    private JButton intersectionButton;
    private JButton selectButton;
    private JButton differenceButton;
    private JButton unionButton;
    private JButton crossJoinButton;
    private JButton maxButton;
    private JButton averageButton;
    private JButton countButton;
    private JButton loadDatabaseFileButton;
    private JButton saveEquationPictureButton;
    private JButton populateDatabaseButton;
    private JTextField textField2;
    private JButton clearDatabaseButton;
    private JButton saveResultsButton;
    private JButton clearButton;
    private JButton executeButton;
    private JButton saveEquationSourceButton;
    private JPanel RAC;
    private JPanel calculator;
    private JPanel operations;
    private JTextArea equationTextArea;
    private JPanel log;
    private JPanel results;
    private JTable resultTable;
    private JTextArea programLog;
    private JPanel equation;
    private JButton saveLogButton;
    private JPanel dbVisual;
    private JTabbedPane dbPane;
    private JButton minButton1;


    private DB db;
    private queryResult qr;

    private void setDBPane() throws SQLException{
        //get the values from the database
        //create one tab for each tab
        //populate each tab with a select * table

        while (dbPane.getTabCount() > 0) {
            dbPane.remove(0);
        }

        queryResult[] qr = db.getEverything();

        for (int x = 0; x < qr.length; x++) {
            JTable jTable = new JTable(qr[x].getData(), qr[x].getColumns());
            JScrollPane jsp = new JScrollPane(jTable);
            dbPane.addTab(qr[x].getName(), null, jsp, "");
        }
        dbPane.revalidate();
        dbPane.repaint();
    }


    public GUI(DB db) {
        this.db = db;
        dbPane.remove(0);
        programLog.setAutoscrolls(true);

        populateDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    db.clearDatabase();
                    db.populateDatabase(textField2.getText());
                    setDBPane();
//                    System.out.println("Populate: " + dbPane.getTabCount());
                    addLog("Cleared and Populated Database from File: " + textField2.getText());

                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        loadDatabaseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(RAC);
                textField2.setText(fc.getSelectedFile().getAbsolutePath());
                addLog("Set input file to: " + fc.getSelectedFile().getAbsolutePath());
            }
        });

        clearDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    db.clearDatabase();
//                    setDBPane();
//                    System.out.println("Clear: " + dbPane.getTabCount());

                    while (dbPane.getTabCount() > 0) {
                        dbPane.remove(0);
                    }

//                    System.out.println("Clear: " + dbPane.getTabCount());

                    addLog("Cleared database");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        textField2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (textField2.getText().length() >= 1) {
                    populateDatabaseButton.setEnabled(true);
                } else {
                    populateDatabaseButton.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (textField2.getText().length() >= 1) {
                    populateDatabaseButton.setEnabled(true);
                } else {
                    populateDatabaseButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (textField2.getText().length() >= 1) {
                    populateDatabaseButton.setEnabled(true);
                } else {
                    populateDatabaseButton.setEnabled(false);
                }
            }
        });

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sampleQuery = "SELECT * FROM person";
                try {
                    qr = db.query(sampleQuery);
//                    System.out.println(Arrays.toString(qr.getColumns()));
                    resultTable.setModel(new DefaultTableModel(qr.getData(), qr.getColumns()));
                    addLog("Queried Databased with query: " + sampleQuery);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLog("Saving Log...");
                String logText = programLog.getText();
                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(RAC);
                try {
                    PrintWriter writer = new PrintWriter(fc.getSelectedFile().getAbsolutePath(), "UTF-8");
//                    System.out.println(logText);
                    writer.print(logText);
                    writer.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLog("Saving Log...");
                String toAdd = db.toString();
                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(RAC);

                try {
                    PrintWriter writer = new PrintWriter(fc.getSelectedFile().getAbsolutePath(), "UTF-8");
                    writer.print(toAdd);
                    writer.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveEquationSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLog("Saving Equation Source...");
                String logText = equationTextArea.getText();
                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(RAC);
                try {
                    PrintWriter writer = new PrintWriter(fc.getSelectedFile().getAbsolutePath(), "UTF-8");
//                    System.out.println(logText);
                    writer.print(logText);
                    writer.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });

        projectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = "\\pi_{}(";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = "\\sigma_{}(";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        crossJoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = " \\bigtimes ";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        minButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = "\\min(";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        maxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = "\\max(";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        averageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexForm = "avg(";
                equationTextArea.setText(equationTextArea.getText() + latexForm);
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equationTextArea.setText("");
            }
        });
    }

    private void addLog(String str) {
        programLog.setText(programLog.getText() + "\n" + "-> " + str);
    }

    public static void main(String[] args) {
        DB db = new DB();
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new GUI(db).RAC);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
