package org.example;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputPanel extends JPanel {
    private List<List<Object>> tempList = new ArrayList<List<Object>>();
    private List<List<Object>> tempListUpdated = new ArrayList<List<Object>>();
    JTabbedPane jtdp;
    Methods methods;
    private List<JPanel> additionalRegressionFieldsPanels = new ArrayList<>();
    int count = 0;
    ConfigurationPanel cPanel;
    private JPanel horizontalPanel = new JPanel();
    static JPanel inputFieldsPanel, regressionPanel;
    static JButton startButton, importButton, clearButton;
    //  private JButton addButton;
    //   private JButton removeButton;
    private final JFileChooser fileChooser;
    private JPanel tempPanel;
    private JPanel icPlanNamePanel, icCpsPanel, icPekPanel, managerPlanNamePanel, managerCpsPanel, managerPekPanel;

    public InputPanel(ActionListener startButtonListener, ActionListener startDataSetupButtonListener, ActionListener validateButtonListener, ActionListener cleanUpButtonListener, ActionListener runTriggersButton) {
//        additionalFieldsPanels.add(horizontalPanel);
        setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        methods = new Methods();
        regressionPanel = new JPanel();
        regressionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        inputFieldsPanel = new JPanel();
        inputFieldsPanel.setLayout(new GridBagLayout());

        startButton = new JButton("Run Regression");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.addActionListener(startButtonListener);
        startButton.setEnabled(false);
//        addButton = new JButton("Add");
//        addButton.setFont(new Font("Arial", Font.BOLD, 16));

//        removeButton = new JButton("Remove");
//        removeButton.setFont(new Font("Arial", Font.BOLD, 16));

        clearButton = new JButton("Clear all");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 16));
        clearButton.setEnabled(false);
        importButton = new JButton("Import From Excel");
        importButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Create the file chooser for Excel files
        fileChooser = new JFileChooser();
        FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(excelFilter);

        importButton.addActionListener(e -> openExcelFileChooser());

        tempPanel = new JPanel();
        tempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add the "Run Regression" button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        inputFieldsPanel.add(startButton, gbc);

//        // Add the "+ Add" button
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.gridwidth = 1;
//        inputFieldsPanel.add(addButton, gbc);

        // Add the "x Clear all & Import from Excel" button next to the "+ Add" button
        tempPanel.add(clearButton);
        tempPanel.add(importButton);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        inputFieldsPanel.add(tempPanel, gbc);

        // Add the "- Remove" button next to the "Import from Excel" button
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets.right = 5; // Add this line to control the spacing between buttons

//        if (!additionalFieldsPanels.isEmpty()) {
//            inputFieldsPanel.add(removeButton, gbc);
//        }

//        addButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JPanel newFieldsPanel = createNewFieldsPanel();
//                addNewFieldsPanel(newFieldsPanel);
//                removeButton.setVisible(!additionalFieldsPanels.isEmpty());
//
//                inputFieldsPanel.revalidate();
//                inputFieldsPanel.repaint();
//            }
//        });

        clearButton.addActionListener(e -> {
            //  System.out.println(additionalFieldsPanels.size());
            while (!additionalRegressionFieldsPanels.isEmpty()) {
                JPanel removedPanel =
                        additionalRegressionFieldsPanels.remove(additionalRegressionFieldsPanels.size() - 1);
                removedPanel.setVisible(false);
                inputFieldsPanel.remove(removedPanel);
                // Hide the remove button if no additional fields are left
                //  removeButton.setVisible(!additionalFieldsPanels.isEmpty());
            }
            Component[] compo = icPlanNamePanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            compo = icCpsPanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            compo = icPekPanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            compo = managerPlanNamePanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            compo = managerCpsPanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            compo = managerPekPanel.getComponents();
            ((JTextField) compo[1]).setText("");
            ((JTextField) compo[1]).setEditable(false);
            inputFieldsPanel.revalidate();
            inputFieldsPanel.repaint();
            clearButton.setEnabled(false);
            startButton.setEnabled(false);
            tempList.clear();
            tempListUpdated.clear();
        });

//        removeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!additionalFieldsPanels.isEmpty()) {
//                    JPanel removedPanel =
//                            additionalFieldsPanels.remove(additionalFieldsPanels.size() - 1);
//                    removedPanel.setVisible(false);
//                    inputFieldsPanel.remove(removedPanel);
//
//                    // Hide the remove button if no additional fields are left
//                //    removeButton.setVisible(!additionalFieldsPanels.isEmpty());
//
//                    inputFieldsPanel.revalidate();
//                    inputFieldsPanel.repaint();
//                }
//            }
//        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
//        horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new GridBagLayout());
        inputFieldsPanel.add(horizontalPanel, gbc);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        icPlanNamePanel = methods.createInputField("IC Plan Name:");
        c.gridx = 0;
        c.gridy = 0;
        horizontalPanel.add(icPlanNamePanel, c);

        icCpsPanel = methods.createInputField("IC CPS:");
        c.gridx = 1;
        c.gridy = 0;
        horizontalPanel.add(icCpsPanel, c);

        icPekPanel = methods.createInputField("IC PEK:");
        c.gridx = 2;
        c.gridy = 0;
        horizontalPanel.add(icPekPanel, c);

        managerPlanNamePanel = methods.createInputField("Manager Plan Name:");
        c.gridx = 3;
        c.gridy = 0;
        horizontalPanel.add(managerPlanNamePanel, c);

        managerCpsPanel = methods.createInputField("Manager CPS:");
        c.gridx = 4;
        c.gridy = 0;
        horizontalPanel.add(managerCpsPanel, c);

        managerPekPanel = methods.createInputField("Manager PEK:");
        c.gridx = 5;
        c.gridy = 0;
        horizontalPanel.add(managerPekPanel, c);

        // Add the inputFieldsPanel to the main panel
//        add(inputFieldsPanel);
        regressionPanel.add(inputFieldsPanel, BorderLayout.NORTH);

        jtdp = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        cPanel = new ConfigurationPanel();
        jtdp.add("Configuration", cPanel);

        jtdp.add("E2E Regression", regressionPanel);
        DataCleanupModule dcleanModule = new DataCleanupModule(cleanUpButtonListener);

        DataSetupModule dsetModule = new DataSetupModule(startDataSetupButtonListener);

        ValidationModule vModule = new ValidationModule(validateButtonListener);
        TriggersModule triggers = new TriggersModule(runTriggersButton);

        jtdp.add("Data Setup", dsetModule);
        jtdp.add("Validation", vModule);
        jtdp.add("Generic Triggers", triggers);
        jtdp.add("Data Cleanup", dcleanModule);

        jtdp.setOpaque(true);

        jtdp.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // Default backcolor
                Color bg = isSelected ? new Color(196, 199, 218, 186) : new Color(109, 127, 232, 171); // Light blue shades

                g.setColor(bg);

                switch (tabPlacement) {
                    case SwingConstants.TOP -> g.fillRect(x + 1, y + 1, w - 1, h - 1);
                    case SwingConstants.BOTTOM -> g.fillRect(x, y, w - 1, h - 1);
                    case SwingConstants.LEFT -> g.fillRect(x + 1, y + 1, w - 1, h - 2);
                    case SwingConstants.RIGHT -> g.fillRect(x, y + 1, w - 1, h - 2);
                }
            }
        });

        add(jtdp);
    }

    private JPanel createNewFieldsPanel(String plan_name, String ic_cps, String ic_pek, String mgr_plan_name, String mgr_cps, String mgr_pek) {
        JPanel newFieldsPanel = new JPanel();
        newFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);

        JPanel icPlanNamePanel = methods.createInputField("IC Plan Name:", plan_name);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPlanNamePanel, gbc2);

        JPanel icCpsPanel = methods.createInputField("IC CPS:", ic_cps);
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        newFieldsPanel.add(icCpsPanel, gbc2);

        JPanel icPekPanel = methods.createInputField("IC PEK:", ic_pek);
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPekPanel, gbc2);

        JPanel managerPlanNamePanel = methods.createInputField("Manager Plan Name:", mgr_plan_name);
        gbc2.gridx = 3;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPlanNamePanel, gbc2);

        JPanel managerCpsPanel = methods.createInputField("Manager CPS:", mgr_cps);
        gbc2.gridx = 4;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerCpsPanel, gbc2);

        JPanel managerPekPanel = methods.createInputField("Manager PEK:", mgr_pek);
        gbc2.gridx = 5;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPekPanel, gbc2);

        return newFieldsPanel;
    }

    void addNewFieldsPanel(JPanel panel) {
        additionalRegressionFieldsPanels.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

//        // Add the "- Remove" button
//        gbc.gridx = 2;
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.gridwidth = 1;
//        gbc.insets.right = 5; // Add this line to control the spacing between buttons
//        inputFieldsPanel.add(removeButton, gbc);

        int row = 6; // Start with the first row for additional fields
        for (JPanel fieldsPanel : additionalRegressionFieldsPanels) {
            gbc.gridy = row;
            gbc.gridx = 0;
            gbc.gridwidth = 3;
            inputFieldsPanel.add(fieldsPanel, gbc);
            row++;
        }

        inputFieldsPanel.revalidate();
        inputFieldsPanel.repaint();

        // Repaint the input panel
        revalidate();
        repaint();

        // Enable the "+ Add" button
        //   addButton.setEnabled(true);
    }
//    // Add a new public method to retrieve values by index
//    public String getInputFieldValue(int index) {
//        if (index >= 0 && index < additionalRegressionFieldsPanels.size()) {
//            JPanel panel = additionalRegressionFieldsPanels.get(index);
//            JTextField textField = (JTextField) panel.getComponent(1);
//            return textField.getText();
//        }
//        return "";
//    }

    public JPanel getCPanel() {
        return cPanel;
    }

    public String getICPlanName(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "IC Plan Name:", additionalRegressionFieldsPanels);
    }

    public String getICCPS(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "IC CPS:", additionalRegressionFieldsPanels);
    }

    public String getICPEK(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "IC PEK:", additionalRegressionFieldsPanels);
    }

    public String getMgrPlanName(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "Manager Plan Name:", additionalRegressionFieldsPanels);
    }

    public String getMgrCPS(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "Manager CPS:", additionalRegressionFieldsPanels);
    }

    public String getMgrPEK(int rowIndex) {
        return methods.getInputFieldValue(rowIndex, "Manager PEK:", additionalRegressionFieldsPanels);
    }

    public void clearAdditionalFieldsPanels() {
        additionalRegressionFieldsPanels.clear();
    }

    public List<JPanel> getAdditionalFieldPanels() {
        return additionalRegressionFieldsPanels;
    }

    public JPanel getHorizontalPanel() {
        return horizontalPanel;
    }

    private void openExcelFileChooser() {
        tempList.clear();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Check if the selected file has a valid Excel file extension
            String fileName = file.getName();
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
                // Display a warning message to the user
                JOptionPane.showMessageDialog(this,
                        "Please select a valid Excel file with .xls or .xlsx extension.",
                        "File Format Error", JOptionPane.WARNING_MESSAGE);
                return; // Exit the method without further processing
            }
            boolean hasErrors = false;
            try (FileInputStream fis = new FileInputStream(file);
                 XSSFWorkbook wb = new XSSFWorkbook(fis)) {
                XSSFSheet sheet = wb.getSheetAt(0);
                boolean hasDataBeyondHeader = false;  // To check if any row beyond the header has data
                for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (!methods.rowIsFullyEmpty(row)) {
                        hasDataBeyondHeader = true;
                        break;
                    }
                }

                if (!hasDataBeyondHeader) {
                    // Display a warning message if the Excel file only has the header
                    JOptionPane.showMessageDialog(this,
                            "The Excel file appears to be empty beyond its headers. Please check your data.",
                            "Empty File Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                for (Row row : sheet) {
                    Cell firstCell = row.getCell(0);
                    if (firstCell != null && firstCell.getCellTypeEnum() == CellType.STRING && "EOF".equals(firstCell.getStringCellValue().trim())) {
                        break; // Stop processing if "EOF" is found
                    }
                    if (methods.rowIsFullyEmpty(row)) {
                        continue; // Skip fully empty rows
                    }
                    ArrayList<Object> currentRowData = new ArrayList<>(sheet.getRow(0).getPhysicalNumberOfCells());
                    for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
                        Cell cell = row.getCell(i);
                        if (isCellEmpty(cell)) {
                            String warningMessage = "Warning: The Excel file has an empty cell. Please check your data.";
                            // Check for specific columns and append special messages
                            if (i == 2) { // For the C column (IC PEK)
                                warningMessage += "<br><b>NOTE: If the IC PEK value is not available, kindly enter \"No PEK.\"</b>";
                            } else if (i == 5) { // For the F column (Manager PEK)
                                warningMessage += "<br><b>NOTE: If the Manager PEK value is not available, kindly enter \"No PEK.\"</b>";
                            }
                            JOptionPane.showMessageDialog(this, "<html>" + warningMessage + "</html>", "Warning", JOptionPane.WARNING_MESSAGE);
                            hasErrors = true;
                            break; // Break out of cell iteration for current row
                        }
                        switch (cell.getCellTypeEnum()) {
                            case STRING -> currentRowData.add(cell.getStringCellValue());
                            case NUMERIC -> currentRowData.add(cell.getNumericCellValue());
                            default -> {
                            }
                        }
                    }
                    if (hasErrors) {
                        break; // Break out of row iteration if there's an error
                    }
                    tempList.add(currentRowData);
                }
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!hasErrors) {
                populateFieldsOnImport();
            } else {
                tempList.clear(); // Clear tempList in case of errors
            }
        }
    }

    void populateFieldsOnImport() {
        if (!tempList.isEmpty()) {
            for (int i = 1; i < tempList.size(); i++) {
                try {
                    Component[] compo = icPlanNamePanel.getComponents();
                    if (((JTextField) compo[1]).getText().isEmpty()) {
                        ((JTextField) compo[1]).setText(tempList.get(1).get(0).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        compo = icCpsPanel.getComponents();
                        ((JTextField) compo[1]).setText(tempList.get(1).get(1).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        compo = icPekPanel.getComponents();
                        ((JTextField) compo[1]).setText(tempList.get(1).get(2).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        compo = managerPlanNamePanel.getComponents();
                        ((JTextField) compo[1]).setText(tempList.get(1).get(3).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        compo = managerCpsPanel.getComponents();
                        ((JTextField) compo[1]).setText(tempList.get(1).get(4).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        compo = managerPekPanel.getComponents();
                        ((JTextField) compo[1]).setText(tempList.get(1).get(5).toString());
                        ((JTextField) compo[1]).setEditable(false);
                        continue;
                    }
                    JPanel newFieldsPanel = createNewFieldsPanel(tempList.get(i).get(0).toString(), tempList.get(i).get(1).toString(), tempList.get(i).get(2).toString(), tempList.get(i).get(3).toString(), tempList.get(i).get(4).toString(), tempList.get(i).get(5).toString());
                    addNewFieldsPanel(newFieldsPanel);
                    //    removeButton.setVisible(!additionalFieldsPanels.isEmpty());
                    inputFieldsPanel.revalidate();
                    inputFieldsPanel.repaint();
                } catch (Exception e) {
                    break;
                }
            }
        }
        clearButton.setEnabled(true);
        startButton.setEnabled(true);
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        if (cell.getCellTypeEnum() == CellType.BLANK) {
            return true;
        }
        return cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().trim().isEmpty();
    }
}