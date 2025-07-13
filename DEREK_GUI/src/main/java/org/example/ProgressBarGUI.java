package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import testauto.*;

public class ProgressBarGUI extends JFrame {
    private InputPanel inputPanel;
    ProgressSection progressSection = new ProgressSection(); // Create an instance
    private Timer timer;
    JPasswordField pwdField = new JPasswordField();
    public int progress;
    Methods methods;
    private static final Logger logger = LoggerFactory.getLogger(ProgressBarGUI.class);
    Derek derek = new Derek();
    public ProgressBarGUI() {
        super();
        methods = new Methods();
        setTitle("DEREK 1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        inputPanel = new InputPanel(new StartButtonListener(), new StartDataSetupButtonListener(), new ValidateButtonListener(), new cleanUpButtonListener(), new runTriggersButtonListener());
        int desiredValue = 0; // Replace this with the value you want
        progressSection.setProgress(desiredValue); // Update the progress bar value
        timer = new Timer(100, e -> progressSection.setProgress(progress));
        // Create a scrollable pane for the input panel
        JScrollPane inputScrollPane = new JScrollPane(inputPanel);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inputScrollPane.repaint();

        // Create a split pane to hold the input panel and progress section

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScrollPane, progressSection);
        splitPane.setResizeWeight(0.2); // Set the divider location

        // Add the split pane to the content pane

        add(splitPane, BorderLayout.CENTER);

        // Add export button at the bottom
        JButton exportButton = new JButton("Export to NotePad");
        exportButton.addActionListener(e -> progressSection.exportLogsToFileWrapper());
        add(exportButton, BorderLayout.SOUTH);
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            InputPanel.startButton.setEnabled(false); // Disable the start button
            InputPanel.importButton.setEnabled(false);
            InputPanel.clearButton.setEnabled(false);
            Component[] c = inputPanel.getCPanel().getComponents();
            methods.usernameDialogBox((JPanel) c[0], inputPanel);

            final JButton passokay = new JButton("Ok");
            passokay.addActionListener(e12 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e12.getSource());
                pane.setValue(passokay);
            });
            passokay.setEnabled(false);

            final JButton passcancel = new JButton("Cancel");
            passcancel.addActionListener(e14 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e14.getSource());
                pane.setValue(passcancel);
            });

            final JPasswordField passwordField = new JPasswordField();
            passwordField.getDocument().addDocumentListener(new DocumentListener() {
                protected void update() {
                    passokay.setEnabled(passwordField.getText().length() > 0);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });

            if (pwdField.getPassword().length == 0) {
                int option = JOptionPane.showOptionDialog(
                        inputPanel,
                        passwordField,
                        "Please provide Password.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{passokay, passcancel},
                        passokay);
                if (option == JOptionPane.OK_OPTION) {
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String securely
                    // Clear the logs when a password is typed and "OK" is clicked
                    progressSection.clearLogs();
                    runRegressionThread(password);
                    Arrays.fill(passwordChars, ' '); // Clear the password char array after use
                } else {
                    // User clicked on Cancel
                    SwingUtilities.invokeLater(() -> {
                        InputPanel.startButton.setEnabled(true);
                        InputPanel.clearButton.setEnabled(true);
                        InputPanel.importButton.setEnabled(true);
                    });
                }
            } else {
                runRegressionThread(new String(pwdField.getPassword())); // Use existing password text
            }
        }
    }
    public class StartDataSetupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DataSetupModule.runDataSetupButton.setEnabled(false); // Disable the start button
            DataSetupModule.importButton.setEnabled(false); // Disable the start button
            DataSetupModule.clearButton.setEnabled(false); // Disable the start button
            Component[] c = inputPanel.getCPanel().getComponents();
            methods.usernameDialogBox((JPanel) c[0], inputPanel);

            final JButton passokay = new JButton("Ok");
            passokay.addActionListener(e12 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e12.getSource());
                pane.setValue(passokay);
            });
            passokay.setEnabled(false);

            final JButton passcancel = new JButton("Cancel");
            passcancel.addActionListener(e14 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e14.getSource());
                pane.setValue(passcancel);
            });

            final JPasswordField passwordField = new JPasswordField();
            passwordField.getDocument().addDocumentListener(new DocumentListener() {
                protected void update() {
                    passokay.setEnabled(passwordField.getText().length() > 0);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });

            if (pwdField.getPassword().length == 0) {
                int option = JOptionPane.showOptionDialog(
                        inputPanel,
                        passwordField,
                        "Please provide Password.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{passokay, passcancel},
                        passokay);
                if (option == JOptionPane.OK_OPTION) {
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String securely
                    // Clear the logs when a password is typed and "OK" is clicked
                    progressSection.clearLogs();
                    runDataSetupThread(password);
                    Arrays.fill(passwordChars, ' '); // Clear the password char array after use
                } else {
                    // User clicked on Cancel
                    SwingUtilities.invokeLater(() -> {
                        DataSetupModule.runDataSetupButton.setEnabled(true);
                        DataSetupModule.clearButton.setEnabled(true);
                        DataSetupModule.importButton.setEnabled(true);
                    });
                }
            } else {
                runDataSetupThread(new String(pwdField.getPassword())); // Use existing password text
            }
        }
    }
    private void runRegressionThread(String password) {
        Thread regressionThread = new Thread(() -> {
            try {
            	String username = ConfigurationPanel.getUsername();
                String env = ConfigurationPanel.getEnvironment();
                List<String> str = methods.getDataFromFields(inputPanel.getHorizontalPanel().getComponents());
                List<String> icPlanName = new ArrayList<String>();
                icPlanName.add(str.get(0));
                List<String> icCPS = new ArrayList<String>();
                icCPS.add(str.get(1));
                List<String> icPEK = new ArrayList<String>();
                icPEK.add(str.get(2));
                List<String> mgrPlanName = new ArrayList<String>();
                mgrPlanName.add(str.get(3));
                List<String> mgrCPS = new ArrayList<String>();
                mgrCPS.add(str.get(4));
                List<String> mgrPEK = new ArrayList<String>();
                mgrPEK.add(str.get(5));

                int numRows = inputPanel.getAdditionalFieldPanels().size();
                for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                    icPlanName.add(methods.getInputFieldValue(rowIndex, "IC Plan Name:", inputPanel.getAdditionalFieldPanels()));
                    icCPS.add(methods.getInputFieldValue(rowIndex, "IC CPS:", inputPanel.getAdditionalFieldPanels()));
                    icPEK.add(methods.getInputFieldValue(rowIndex, "IC PEK:", inputPanel.getAdditionalFieldPanels()));
                    mgrPlanName.add(methods.getInputFieldValue(rowIndex, "Manager Plan Name:", inputPanel.getAdditionalFieldPanels()));
                    mgrCPS.add(methods.getInputFieldValue(rowIndex, "Manager CPS:", inputPanel.getAdditionalFieldPanels()));
                    mgrPEK.add(methods.getInputFieldValue(rowIndex, "Manager PEK:", inputPanel.getAdditionalFieldPanels()));
                }
                progressSection.redirectSystemOutput(); // Redirect console output to GUI log
                runCommandAndDisplayOutput(username, password, env, icPlanName, icCPS, icPEK, mgrPlanName, mgrCPS, mgrPEK);
            } catch (Exception ex) {
                ex.printStackTrace();
                progressSection.logInfo("[Error] :: Error executing regression command","e2e" );
                progressSection.setProgress(0); // Set progress to 0% in case of exception
            } finally {
                // Enable the start button in case of an error
                SwingUtilities.invokeLater(() -> {
                    InputPanel.startButton.setEnabled(true);
                    InputPanel.clearButton.setEnabled(true);
                    InputPanel.importButton.setEnabled(true);
                });
            }
        });
        regressionThread.start();
    }

    private void runCommandAndDisplayOutput(String username, String password, String env, List<String> icPlanName, List<String>
            icCPS, List<String> icPEK, List<String> mgrPlanName, List<String> mgrCPS, List<String> mgrPEK) {
        try {
            // Print the values for debugging

            for (int i = 0; i < icCPS.size(); i++) {
                System.out.println("Username: " + username);
                System.out.println("Environment: " + env);
                System.out.println("Plan Name: " + icPlanName.get(i));
                System.out.println("CPS Name: " + icCPS.get(i));
                System.out.println("PEK Value: " + icPEK.get(i));
                System.out.println("Manager Plan Name: " + mgrPlanName.get(i));
                System.out.println("Manager CPS Name: " + mgrCPS.get(i));
                System.out.println("Manager PEK Value: " + mgrPEK.get(i));
                System.out.println();

                // Create an object and call the method
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);

                // Redirect standard output to capture it
                PrintStream originalOut = System.out;
                System.setOut(new PrintStream(new OutputStream() {
                    private StringBuilder buffer = new StringBuilder();
                    @Override
                    public void write(int b) throws IOException {
                        try {
                            if (b == '\n') {
                                String line = buffer.toString();
                                progressSection.logInfo(line, "e2e");
                                writer.write(line);
                                buffer.setLength(0);
                            } else {
                                buffer.append((char) b);
                            }
                        } catch (Throwable  exception) {
                            if (exception.toString().toLowerCase().startsWith("exception")) {
                                progressSection.logInfo(exception.toString(), "e2e");
                            }
                        }
                    }
                    @Override
                    public void flush() throws IOException {
                        String remaining = buffer.toString();
                        if (!remaining.isEmpty()) {
                            progressSection.logInfo("---------FLUSH----------------", "e2e");
                            progressSection.logInfo(remaining, "e2e");
                            writer.write(remaining);
                            buffer.setLength(0);
                        }
                    }
                }));
                // Call the method
//                derek.runRegression(username, password, env, icPlanName.get(i).trim(), icCPS.get(i).trim(), icPEK.get(i).trim(), mgrPlanName.get(i).trim(),mgrCPS.get(i).trim(), mgrPEK.get(i).trim());
                logger.info("[Info] :: Regression Complete");

                // Update progress in GUI
                progress = progressSection.calculate_e2eRegression_Progress("[Info] :: Regression Complete");
                progressSection.setProgress(progress);
                revalidate(); // Trigger layout recalculation
                repaint();
                // Reset standard output
                System.setOut(originalOut);
                writer.close();
            }
            // After all iterations, ensure the final progress is set
            progress = 100;
            progressSection.setProgress(progress);
            revalidate(); // Trigger layout recalculation
        } catch (Throwable  ex) {
            ex.printStackTrace();
            progressSection.logInfo("[Error] :: Error executing regression command", "e2e");
            progressSection.setProgress(0); // Set progress to 0% in case of exception

            // Update the GUI components on the EDT
            SwingUtilities.invokeLater(() -> {
                InputPanel.startButton.setEnabled(true);
                InputPanel.clearButton.setEnabled(true);
                InputPanel.importButton.setEnabled(true);
            });
        }
    }

    private void runDataSetupThread(String password) {
        Thread dataSetupThread = new Thread(() -> {
            try {
                String username = ConfigurationPanel.getUsername();
                String env = ConfigurationPanel.getEnvironment();
                List<String> str = methods.getDataFromFields(DataSetupModule.horizontalPanel.getComponents());
                List<String> icPlanName = new ArrayList<String>();
                icPlanName.add(str.get(0));
                List<String> icCPS = new ArrayList<String>();
                icCPS.add(str.get(1));
                List<String> icPEK = new ArrayList<String>();
                icPEK.add(str.get(2));
                List<String> mgrPlanName = new ArrayList<String>();
                mgrPlanName.add(str.get(3));
                List<String> mgrCPS = new ArrayList<String>();
                mgrCPS.add(str.get(4));
                List<String> mgrPEK = new ArrayList<String>();
                mgrPEK.add(str.get(5));
                int numRows = DataSetupModule.getAdditionalFieldPanels().size();
                System.out.println(numRows);
                for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                    icPlanName.add(DataSetupModule.getInputFieldValue(rowIndex, "IC Plan Name:"));
                    icCPS.add(DataSetupModule.getInputFieldValue(rowIndex, "IC CPS:"));
                    icPEK.add(DataSetupModule.getInputFieldValue(rowIndex, "IC PEK:"));
                    mgrPlanName.add(DataSetupModule.getInputFieldValue(rowIndex, "Manager Plan Name:"));
                    mgrCPS.add(DataSetupModule.getInputFieldValue(rowIndex, "Manager CPS:"));
                    mgrPEK.add(DataSetupModule.getInputFieldValue(rowIndex, "Manager PEK:"));
                    System.out.println(DataSetupModule.getInputFieldValue(rowIndex, "IC Plan Name:"));
                }
                progressSection.redirectSystemOutput(); // Redirect console output to GUI log
                runDataSetupCommandAndDisplayOutput(username, password, env, icPlanName, icCPS, icPEK, mgrPlanName, mgrCPS, mgrPEK);

            } catch (Exception ex) {
                ex.printStackTrace();
                progressSection.logInfo("[Error] :: Error executing regression command", "e2e");
                progressSection.setProgress(0); // Set progress to 0% in case of exception
            } finally {
                // Enable the start button in case of an error
                SwingUtilities.invokeLater(() -> {
                    DataSetupModule.runDataSetupButton.setEnabled(true);
                    DataSetupModule.importButton.setEnabled(true);
                    DataSetupModule.clearButton.setEnabled(true);
                });
            }
        });

        dataSetupThread.start();
    }

    private void runDataSetupCommandAndDisplayOutput(String username, String password, String env, List<String> icPlanName, List<String>
            icCPS, List<String> icPEK, List<String> mgrPlanName, List<String> mgrCPS, List<String> mgrPEK) {
        try {
            // Print the values for debugging
            for (int i = 0; i < icCPS.size(); i++) {
                System.out.println("Username: " + username);
                System.out.println("Environment: " + env);
                System.out.println("Plan Name: " + icPlanName.get(i));
                System.out.println("CPS Name: " + icCPS.get(i));
                System.out.println("PEK Value: " + icPEK.get(i));
                System.out.println("Manager Plan Name: " + mgrPlanName.get(i));
                System.out.println("Manager CPS Name: " + mgrCPS.get(i));
                System.out.println("Manager PEK Value: " + mgrPEK.get(i));
                String abcValue = icCPS.get(i);
                String defValue = mgrPlanName.get(i);
                System.out.println();

                // Create an object and call the method
//                testauto.Incent incent = new Incent(username, password, env);
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);

                // Redirect standard output to capture it
                PrintStream originalOut = System.out;
                System.setOut(new PrintStream(new OutputStream() {
                    private StringBuilder buffer = new StringBuilder();

                    @Override
                    public void write(int b) throws IOException {
                        try {
                            if (b == '\n') {
                                String line = buffer.toString();
                                progressSection.logInfo(line, "e2e");
                                writer.write(line);
                                buffer.setLength(0);
                            } else {
                                buffer.append((char) b);
                            }
                        } catch (Throwable exception) {
                            if (exception.toString().toLowerCase().startsWith("exception")) {
                                progressSection.logInfo(exception.toString(), "e2e");
                            }
                        }
                    }

                    @Override
                    public void flush() throws IOException {
                        String remaining = buffer.toString();
                        if (!remaining.isEmpty()) {
                            progressSection.logInfo("---------FLUSH----------------", "e2e");
                            progressSection.logInfo(remaining, "e2e");
                            writer.write(remaining);
                            buffer.setLength(0);
                        }
                    }
                }));
                // Call the method
                if (abcValue != null && !abcValue.trim().isEmpty() && (defValue == null || defValue.trim().isEmpty())) {
//                  Plan plan = new Plan(incent,icPlanName.get(i));
                    System.out.println("[Info] :: Generating Data Setup");
//                    DataSetup datasetup = plan.getTestDataSetup(incent,username,icCPS.get(i),icPEK.get(i),null,null,null,"IC");
//                    File jarFile 			= 	new File(Regression.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//                    String downloadPath 	= 	jarFile.getParent();
                    String workbookName 	= 	"QAWorkbook_"+icPlanName.get(i).replaceAll("[^a-zA-Z0-9]", "_") +"_"+icPEK.get(i).replaceAll("[^a-zA-Z0-9]", "_")+ ".xlsx";
                    System.out.println("[Info] :: Generating Data Setup");
//                    Workbook workbook = datasetup.createWorkBook(downloadPath+File.separator+workbookName);
//                    workbook.close();
                } else if ((abcValue == null || abcValue.trim().isEmpty()) && defValue != null && !defValue.trim().isEmpty()) {
//                    Plan mgrplan = new Plan(incent,mgrPlanName.get(i));
                    System.out.println("[Info] :: Generating Data Setup");
//                    File jarFile 			= 	new File(Regression.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//                    String downloadPath 	= 	jarFile.getParent();
                    System.out.println("[Info] :: Generating Data Setup");
//                    DataSetup datasetup1 = mgrplan.getTestDataSetup(incent,username,mgrPlanName.get(i),mgrPEK.get(i),null,null,null,"MGR");
                    String workbookName1 	= 	"QAWorkbook_"+mgrPlanName.get(i).replaceAll("[^a-zA-Z0-9]", "_")+"_" +mgrPEK.get(i).replaceAll("[^a-zA-Z0-9]", "_")+ ".xlsx";
//                    Workbook workbook2 = datasetup1.createWorkBook(downloadPath+File.separator+workbookName1);
//                    workbook2.close();
                } else {
//                    Plan plan = new Plan(incent,icPlanName.get(i));
//                    Plan mgrplan = new Plan(incent,mgrPlanName.get(i));
//                    System.out.println("[Info] :: Generating Data Setup");
//                    DataSetup datasetup = plan.getTestDataSetup(incent,username,icCPS.get(i),icPEK.get(i),mgrplan,mgrCPS.get(i),mgrPEK.get(i),"IC");
//                    File jarFile 			= 	new File(Regression.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//                    String downloadPath 	= 	jarFile.getParent();
                    String workbookName 	= 	"QAWorkbook_"+icPlanName.get(i).replaceAll("[^a-zA-Z0-9]", "_") +"_"+icPEK.get(i).replaceAll("[^a-zA-Z0-9]", "_")+ ".xlsx";
                    System.out.println("[Info] :: Generating Data Setup");
//                    DataSetup datasetup1 = mgrplan.getTestDataSetup(incent,username,mgrPlanName.get(i),mgrPEK.get(i),plan,icCPS.get(i),icPEK.get(i),"MGR");
                    String workbookName1 	= 	"QAWorkbook_"+mgrPlanName.get(i).replaceAll("[^a-zA-Z0-9]", "_")+"_" +mgrPEK.get(i).replaceAll("[^a-zA-Z0-9]", "_")+ ".xlsx";
//                    Workbook workbook2 = datasetup1.createWorkBook(downloadPath+File.separator+workbookName1);
//                    workbook2.close();
//                    Workbook workbook = datasetup.createWorkBook(downloadPath+File.separator+workbookName);
//                    workbook.close();
                }
                logger.info("[Info] :: Regression Complete");

                // Update progress in GUI
                progress = progressSection.calculate_e2eRegression_Progress("[Info] :: Regression Complete");
                progressSection.setProgress(progress);
                revalidate(); // Trigger layout recalculation
                repaint();
                // Reset standard output
                System.setOut(originalOut);
                writer.close();
            }

            // After all iterations, ensure the final progress is set
            progress = 100;
            progressSection.setProgress(progress);
            revalidate(); // Trigger layout recalculation
        } catch (Throwable  ex) {
            ex.printStackTrace();
            progressSection.logInfo("[Error] :: Error executing regression command", "e2e");
            progressSection.setProgress(0); // Set progress to 0% in case of exception

            // Update the GUI components on the EDT
            SwingUtilities.invokeLater(() -> {
                DataSetupModule.runDataSetupButton.setEnabled(true);
                DataSetupModule.clearButton.setEnabled(true);
                DataSetupModule.importButton.setEnabled(true);
            });
        }
    }

    private class ValidateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ValidationModule.validateButton.setEnabled(false); // Disable the start button
            ValidationModule.removeButton.setEnabled(false);
            ValidationModule.selectExcelButton.setEnabled(false);
            String username = ConfigurationPanel.getUsername(); // Get the username from the main page

            if (username.isEmpty()) {
                final JButton okay = new JButton("Ok");
                okay.addActionListener(e1 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e1.getSource());
                    pane.setValue(okay);
                });
                okay.setEnabled(false);

                final JButton cancel = new JButton("Cancel");
                cancel.addActionListener(e13 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e13.getSource());
                    pane.setValue(cancel);
                });

                final JTextField field = new JTextField();
                field.getDocument().addDocumentListener(new DocumentListener() {
                    protected void update() {
                        okay.setEnabled(!field.getText().isEmpty());
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }
                });
                JOptionPane.showOptionDialog(
                        inputPanel,
                        field,
                        "Please provide Username.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{okay, cancel},
                        okay);

                username = field.getText();
                ConfigurationPanel.setUsername(username);
                if (username == null || username.isEmpty()) {
                    // User clicked on Cancel or left the username blank
                    SwingUtilities.invokeLater(() -> {
                        ValidationModule.validateButton.setEnabled(true);
                        ValidationModule.selectExcelButton.setEnabled(true);
                        ValidationModule.removeButton.setEnabled(true);
                    });
                    return; // Exit the listener if user cancels or leaves username blank
                }
            }

            final JButton passokay = new JButton("Ok");
            passokay.addActionListener(e12 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e12.getSource());
                pane.setValue(passokay);
            });
            passokay.setEnabled(false);

            final JButton passcancel = new JButton("Cancel");
            passcancel.addActionListener(e14 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e14.getSource());
                pane.setValue(passcancel);
            });

            final JPasswordField passwordField = new JPasswordField();
            passwordField.getDocument().addDocumentListener(new DocumentListener() {
                protected void update() {
                    passokay.setEnabled(passwordField.getText().length() > 0);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });

            if (pwdField.getPassword().length == 0) {
//                JPasswordField passwordField = new JPasswordField(); // Use JPasswordField for secure password entry
//                int option = JOptionPane.showConfirmDialog(inputPanel, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION);

                int option = JOptionPane.showOptionDialog(
                        inputPanel,
                        passwordField,
                        "Please provide Password.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{passokay, passcancel},
                        passokay);
                if (option == JOptionPane.OK_OPTION) {
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String securely
                    // Clear the logs when a password is typed and "OK" is clicked
                    progressSection.clearLogs();
                    runValidationThread(password);
                    Arrays.fill(passwordChars, ' '); // Clear the password char array after use
                } else {
                    // User clicked on Cancel
                    SwingUtilities.invokeLater(() -> {
                        ValidationModule.validateButton.setEnabled(true);
                        ValidationModule.removeButton.setEnabled(true);
                        ValidationModule.selectExcelButton.setEnabled(true);
                    });
                }
            } else {
                runValidationThread(new String(pwdField.getPassword())); // Use existing password text
            }
        }
    }

    private void runValidationThread(String password) {
        Thread validateThread = new Thread(() -> {
            try {
                String username = ConfigurationPanel.getUsername();
                String env = ConfigurationPanel.getEnvironment();
                String absolutePath = ValidationModule.selectedFile.getAbsolutePath();
                displayOutputUponValidationButton(username, password, env, absolutePath);
                progressSection.redirectSystemOutput(); // Redirect console output to GUI log
            } catch (Exception ex) {
                ex.printStackTrace();
                progressSection.logInfo("[Error] :: Error executing validate command","validation" );
//                progressSection.setProgress(0); // Set progress to 0% in case of exception
            } finally {
                // Enable the start button in case of an error
                SwingUtilities.invokeLater(() -> {
                    ValidationModule.validateButton.setEnabled(true);
                    ValidationModule.selectExcelButton.setEnabled(true);
                    ValidationModule.removeButton.setEnabled(true);
                });
            }
        });
        validateThread.start();
    }

    private void displayOutputUponValidationButton(String username, String password, String env,String filePath) {
        try {
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);

                // Redirect standard output to capture it
                PrintStream originalOut = System.out;
                System.setOut(new PrintStream(new OutputStream() {
                    private StringBuilder buffer = new StringBuilder();
                    @Override
                    public void write(int b) throws IOException {
                        try {
                            if (b == '\n') {
                                String line = buffer.toString();
                                progressSection.logInfo(line, "validation");
                                writer.write(line);
                                buffer.setLength(0);
                            } else {
                                buffer.append((char) b);
                            }
                        } catch (Throwable  exception) {
                            if (exception.toString().toLowerCase().startsWith("exception")) {
                                progressSection.logInfo(exception.toString(), "validation");
                            }
                        }
                    }
                    @Override
                    public void flush() throws IOException {
                        String remaining = buffer.toString();
                        if (!remaining.isEmpty()) {
                            progressSection.logInfo("---------FLUSH----------------", "validation");
                            progressSection.logInfo(remaining, "validation");
                            writer.write(remaining);
                            buffer.setLength(0);
                        }
                    }
                }));
//        Call the method
//                derek.runValidation(username, password, env, filePath);
                logger.info("[Info] :: Validation Complete");
                // Update progress in GUI
                progress = progressSection.calculateValidationProgress("[Info] :: Validation Complete");
                progressSection.setProgress(progress);
                revalidate(); // Trigger layout recalculation
                repaint();
                // Reset standard output
                System.setOut(originalOut);
                writer.close();
            // After all iterations, ensure the final progress is set
            progress = 100;
            progressSection.setProgress(progress);
            revalidate(); // Trigger layout recalculation
        } catch (Throwable  ex) {
            ex.printStackTrace();
            progressSection.logInfo("[Error] :: Error executing regression command", "validation");
            progressSection.setProgress(0); // Set progress to 0% in case of exception
            // Update the GUI components on the EDT
            SwingUtilities.invokeLater(() -> {
                ValidationModule.validateButton.setEnabled(true);
                ValidationModule.selectExcelButton.setEnabled(true);
                ValidationModule.removeButton.setEnabled(true);
            });
        }
    }
    private class cleanUpButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DataCleanupModule.executeButton.setEnabled(false); // Disable the start button
            String username = ConfigurationPanel.getUsername(); // Get the username from the main page

            if (username.isEmpty()) {

                final JButton okay = new JButton("Ok");
                okay.addActionListener(e1 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e1.getSource());
                    pane.setValue(okay);
                });
                okay.setEnabled(false);

                final JButton cancel = new JButton("Cancel");
                cancel.addActionListener(e13 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e13.getSource());
                    pane.setValue(cancel);
                });

                final JTextField field = new JTextField();
                field.getDocument().addDocumentListener(new DocumentListener() {
                    protected void update() {
                        okay.setEnabled(!field.getText().isEmpty());
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }
                });

                JOptionPane.showOptionDialog(
                        inputPanel,
                        field,
                        "Please provide Username.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{okay, cancel},
                        okay);

                username = field.getText();
                ConfigurationPanel.setUsername(username);
                if (username == null || username.isEmpty()) {
                    // User clicked on Cancel or left the username blank
                    SwingUtilities.invokeLater(() -> {
                        ValidationModule.validateButton.setEnabled(true);
                        ValidationModule.selectExcelButton.setEnabled(true);
                        ValidationModule.removeButton.setEnabled(true);
                    });
                    return; // Exit the listener if user cancels or leaves username blank
                }
            }

            final JButton passokay = new JButton("Ok");
            passokay.addActionListener(e12 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e12.getSource());
                pane.setValue(passokay);
            });
            passokay.setEnabled(false);

            final JButton passcancel = new JButton("Cancel");
            passcancel.addActionListener(e14 -> {
                JOptionPane pane = methods.getOptionPane((JComponent) e14.getSource());
                pane.setValue(passcancel);
            });

            final JPasswordField passwordField = new JPasswordField();
            passwordField.getDocument().addDocumentListener(new DocumentListener() {
                protected void update() {
                    passokay.setEnabled(passwordField.getText().length() > 0);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
            });

            if (pwdField.getPassword().length == 0) {
                int option = JOptionPane.showOptionDialog(
                        inputPanel,
                        passwordField,
                        "Please provide Password.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{passokay, passcancel},
                        passokay);
                if (option == JOptionPane.OK_OPTION) {
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars); // Convert char[] to String securely
                    // Clear the logs when a password is typed and "OK" is clicked
                    progressSection.clearLogs();
                    runCleanupThread(password);
                    Arrays.fill(passwordChars, ' '); // Clear the password char array after use
                } else {
                    // User clicked on Cancel
                    SwingUtilities.invokeLater(() -> {
                        DataCleanupModule.executeButton.setEnabled(true);
                    });
                }
            } else {
                runCleanupThread(new String(pwdField.getPassword())); // Use existing password text
            }
        }
    }

    private void runCleanupThread(String password) {
        Thread cleanupThread = new Thread(() -> {
            try {
                String username = ConfigurationPanel.getUsername();
                String env = ConfigurationPanel.getEnvironment();
                progressSection.redirectSystemOutput(); // Redirect console output to GUI log
                displayOutputUponCleanupButton(username,password,env);
            } catch (Exception ex) {
                ex.printStackTrace();
                progressSection.logInfo("[Error] :: Error executing cleanup", "e2e");
//                progressSection.setProgress(0); // Set progress to 0% in case of exception
            } finally {
                // Enable the start button in case of an error
                SwingUtilities.invokeLater(() -> {
                    DataCleanupModule.executeButton.setEnabled(true);
                });
            }
        });
        cleanupThread.start();
    }

    private void displayOutputUponCleanupButton(String username, String password, String env) {
        try {
            // Print the values for debugging
                System.out.println("Username: " + ConfigurationPanel.getUsername());
                System.out.println("Environment: " + ConfigurationPanel.getEnvironment());
                System.out.println();

                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);

                // Redirect standard output to capture it
                PrintStream originalOut = System.out;
                System.setOut(new PrintStream(new OutputStream() {
                    private StringBuilder buffer = new StringBuilder();
                    @Override
                    public void write(int b) throws IOException {
                        try {
                            if (b == '\n') {
                                String line = buffer.toString();
                                progressSection.logInfo(line, "e2e");
                                writer.write(line);
                                buffer.setLength(0);
                            } else {
                                buffer.append((char) b);
                            }
                        } catch (Throwable  exception) {
                            if (exception.toString().toLowerCase().contains("exception")) {
                                progressSection.logInfo(exception.toString(), "e2e");
                            }
                        }
                    }
                    @Override
                    public void flush() throws IOException {
                        String remaining = buffer.toString();
                        if (!remaining.isEmpty()) {
                            progressSection.logInfo("---------FLUSH----------------", "e2e");
                            progressSection.logInfo(remaining, "e2e");
                            writer.write(remaining);
                            buffer.setLength(0);
                        }
                    }
                }));
//    	        Call the method
//    	        derek.runDataCleanup(username, password, env);
                logger.info("[Info] :: Cleanup Complete");

                // Update progress in GUI
                progress = progressSection.calculate_e2eRegression_Progress("[Info] :: Cleanup Complete");
                progressSection.setProgress(progress);
                revalidate(); // Trigger layout recalculation
                repaint();
                // Reset standard output
                System.setOut(originalOut);
                writer.close();

            // After all iterations, ensure the final progress is set
            progress = 100;
            progressSection.setProgress(progress);
            revalidate(); // Trigger layout recalculation
        } catch (Throwable  ex) {
            ex.printStackTrace();
            progressSection.logInfo("[Error] :: Error executing Cleanup command", "e2e");
            progressSection.setProgress(0); // Set progress to 0% in case of exception

            // Update the GUI components on the EDT
            SwingUtilities.invokeLater(() -> {
                DataCleanupModule.executeButton.setEnabled(true);
            });
        }
    }
    private class runTriggersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TriggersModule.runGenericTriggers.setEnabled(false); // Disable the start button
                Component[] c = inputPanel.getCPanel().getComponents();
                methods.usernameDialogBox((JPanel) c[0], inputPanel);

                final JButton passokay = new JButton("Ok");
                passokay.addActionListener(e12 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e12.getSource());
                    pane.setValue(passokay);
                });
                passokay.setEnabled(false);

                final JButton passcancel = new JButton("Cancel");
                passcancel.addActionListener(e14 -> {
                    JOptionPane pane = methods.getOptionPane((JComponent) e14.getSource());
                    pane.setValue(passcancel);
                });

                final JPasswordField passwordField = new JPasswordField();
                passwordField.getDocument().addDocumentListener(new DocumentListener() {
                    private void update() {
                        passokay.setEnabled(!passwordField.getText().isEmpty());
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }
                });

                if (pwdField.getPassword().length == 0) {
                    int option = JOptionPane.showOptionDialog(
                            inputPanel,
                            passwordField,
                            "Please provide Password.",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{passokay, passcancel},
                            passokay);
                    if (option == JOptionPane.OK_OPTION) {
                        char[] passwordChars = passwordField.getPassword();
                        String password = new String(passwordChars); // Convert char[] to String securely
                        // Clear the logs when a password is typed and "OK" is clicked
                        progressSection.clearLogs();
                        runRegressionThread(password);
                        Arrays.fill(passwordChars, ' '); // Clear the password char array after use
                    } else {
                        // User clicked on Cancel
                        SwingUtilities.invokeLater(() -> {
                            TriggersModule.runGenericTriggers.setEnabled(false);
                        });
                    }
                } else {
                    runRegressionThread(new String(pwdField.getPassword())); // Use existing password text
                }
            }
        }

    public static void main(String[] args) {
        // Define the lock file name and directory
        String lockFileName = "myapp.lock";
        String lockDirectory = System.getProperty("user.home"); // You can specify a different directory

        // Create a File object for the lock file
        File lockFile = new File(lockDirectory, lockFileName);

        // Check if the lock file exists
        if (lockFile.exists()) {
            System.out.println("Another instance of the application is already running.");
            return; // Exit the new instance
        }

        // Create the lock file to prevent multiple instances
        try {
            lockFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the Look and Feel
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ProgressBarGUI progressBarGUI = new ProgressBarGUI();
            progressBarGUI.setVisible(true);
        });

        // Add a shutdown hook to delete the lock file when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(lockFile::delete));
    }
}