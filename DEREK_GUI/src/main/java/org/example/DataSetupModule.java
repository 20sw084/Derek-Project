package org.example;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataSetupModule extends JPanel {
    private List<List<Object>> tempList = new ArrayList<List<Object>>();
    private List<List<Object>> tempListUpdated = new ArrayList<List<Object>>();
    private static List<JPanel> additionalFieldsPanels = new ArrayList<>();
    //    private configurationPanel cPanel;
    public static JPanel horizontalPanel = new JPanel();
    private JPanel dataSetupFieldsPanel;
    public static JButton runDataSetupButton, importButton, clearButton;
    private final JFileChooser fileChooser;
    private JPanel tempPanel;
    private FileNameExtensionFilter excelFilter;
    private JPanel icPlanNamePanel, icCpsPanel, icPekPanel, managerPlanNamePanel, managerCpsPanel, managerPekPanel;
    ProgressSection progressSection = new ProgressSection();
    JPasswordField pwdField = new JPasswordField();
    int count = 0;
    Methods methods;

    public DataSetupModule(ActionListener StartDataSetupButtonListener) {
//        additionalFieldsPanels.add(horizontalPanel);
        setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        methods = new Methods();
        dataSetupFieldsPanel = new JPanel();
        dataSetupFieldsPanel.setLayout(new GridBagLayout());

//        ProgressBarGUI.StartDataSetupButtonListener();

        runDataSetupButton = new JButton("Run Data Setup");
        runDataSetupButton.setFont(new Font("Arial", Font.BOLD, 16));
        runDataSetupButton.addActionListener(StartDataSetupButtonListener);
        runDataSetupButton.setEnabled(false);
//        addDataSetupButton = new JButton("Add");
//        addDataSetupButton.setFont(new Font("Arial", Font.BOLD, 16));
//
//        removeDataSetupButton = new JButton("Remove");
//        removeDataSetupButton.setFont(new Font("Arial", Font.BOLD, 16));

        clearButton = new JButton("Clear all");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 16));
        clearButton.setEnabled(false);

        importButton = new JButton("Import From Excel");
        importButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Create the file chooser for Excel files
        fileChooser = new JFileChooser();
        excelFilter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
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
        dataSetupFieldsPanel.add(runDataSetupButton, gbc);

        // Add the "+ Add" button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        //   dataSetupFieldsPanel.add(addDataSetupButton, gbc);

        // Add the "x Clear all & Import from Excel" button next to the "+ Add" button
        tempPanel.add(clearButton);
        tempPanel.add(importButton);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        dataSetupFieldsPanel.add(tempPanel, gbc);

        // Add the "- Remove" button next to the "Import from Excel" button
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets.right = 5; // Add this line to control the spacing between buttons
//
//        if (!additionalFieldsPanels.isEmpty()) {
//            dataSetupFieldsPanel.add(removeDataSetupButton, gbc);
//        }

//        addDataSetupButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JPanel newFieldsPanel = createNewFieldsPanel();
//                addNewFieldsPanel(newFieldsPanel);
//                removeDataSetupButton.setVisible(!additionalFieldsPanels.isEmpty());
//
//                dataSetupFieldsPanel.revalidate();
//                dataSetupFieldsPanel.repaint();
//            }
//        });

        clearButton.addActionListener(e -> {
            System.out.println(additionalFieldsPanels.size());
            while (!additionalFieldsPanels.isEmpty()) {
                JPanel removedPanel =
                        additionalFieldsPanels.remove(additionalFieldsPanels.size() - 1);
                removedPanel.setVisible(false);
                dataSetupFieldsPanel.remove(removedPanel);
                // Hide the remove button if no additional fields are left
                // removeDataSetupButton.setVisible(!additionalFieldsPanels.isEmpty());
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
            dataSetupFieldsPanel.revalidate();
            dataSetupFieldsPanel.repaint();
            clearButton.setEnabled(false);
            runDataSetupButton.setEnabled(false);
        });

//        removeDataSetupButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!additionalFieldsPanels.isEmpty()) {
//                    JPanel removedPanel =
//                            additionalFieldsPanels.remove(additionalFieldsPanels.size() - 1);
//                    removedPanel.setVisible(false);
//                    dataSetupFieldsPanel.remove(removedPanel);
//
////                    // Hide the remove button if no additional fields are left
////                    removeDataSetupButton.setVisible(!additionalFieldsPanels.isEmpty());
//
//                    dataSetupFieldsPanel.revalidate();
//                    dataSetupFieldsPanel.repaint();
//                }
//            }
//        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
//        horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new GridBagLayout());
        dataSetupFieldsPanel.add(horizontalPanel, gbc);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        icPlanNamePanel = createInputField("IC Plan Name:");
        c.gridx = 0;
        c.gridy = 0;
        horizontalPanel.add(icPlanNamePanel, c);

        icCpsPanel = createInputField("IC CPS:");
        c.gridx = 1;
        c.gridy = 0;
        horizontalPanel.add(icCpsPanel, c);

        icPekPanel = createInputField("IC PEK:");
        c.gridx = 2;
        c.gridy = 0;
        horizontalPanel.add(icPekPanel, c);

        managerPlanNamePanel = createInputField("Manager Plan Name:");
        c.gridx = 3;
        c.gridy = 0;
        horizontalPanel.add(managerPlanNamePanel, c);

        managerCpsPanel = createInputField("Manager CPS:");
        c.gridx = 4;
        c.gridy = 0;
        horizontalPanel.add(managerCpsPanel, c);

        managerPekPanel = createInputField("Manager PEK:");
        c.gridx = 5;
        c.gridy = 0;
        horizontalPanel.add(managerPekPanel, c);

        add(dataSetupFieldsPanel, BorderLayout.NORTH);
    }

    public static List<JPanel> getAdditionalFieldPanels() {
        return additionalFieldsPanels;
    }

    public static String getInputFieldValue(int index, String labelText) {
        if (index >= 0 && index < additionalFieldsPanels.size()) {
            JPanel panel = additionalFieldsPanels.get(index);
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel subPanel) {
                    JLabel label = (JLabel) subPanel.getComponent(0); // updated Here!
                    JTextField textField = (JTextField) subPanel.getComponent(1);
                    if (label.getText().equals(labelText)) {
                        return textField.getText();
                    }
                }
            }
        }
        return ""; // Return an empty string if the field is not found
    }

    private JPanel createInputField(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(250, 25));
//		textField.setEditable(true);
        textField.setEditable(false);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNewFieldsPanel() {
        JPanel newFieldsPanel = new JPanel();
        newFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);

        JPanel icPlanNamePanel = createInputField("IC Plan Name:");
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPlanNamePanel, gbc2);

        JPanel icCpsPanel = createInputField("IC CPS:");
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        newFieldsPanel.add(icCpsPanel, gbc2);

        JPanel icPekPanel = createInputField("IC PEK:");
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPekPanel, gbc2);

        JPanel managerPlanNamePanel = createInputField("Manager Plan Name:");
        gbc2.gridx = 3;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPlanNamePanel, gbc2);

        JPanel managerCpsPanel = createInputField("Manager CPS:");
        gbc2.gridx = 4;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerCpsPanel, gbc2);

        JPanel managerPekPanel = createInputField("Manager PEK:");
        gbc2.gridx = 5;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPekPanel, gbc2);

        return newFieldsPanel;
    }

    void addNewFieldsPanel(JPanel panel) {
        additionalFieldsPanels.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

//        // Add the "- Remove" button
//        gbc.gridx = 2;
//        gbc.gridy = 3;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.gridwidth = 1;
//        gbc.insets.right = 5; // Add this line to control the spacing between buttons
//        dataSetupFieldsPanel.add(removeDataSetupButton, gbc);

        int row = 6; // Start with the first row for additional fields
        for (JPanel fieldsPanel : additionalFieldsPanels) {
            gbc.gridy = row;
            gbc.gridx = 0;
            gbc.gridwidth = 3;
            dataSetupFieldsPanel.add(fieldsPanel, gbc);
            row++;
        }

        dataSetupFieldsPanel.revalidate();
        dataSetupFieldsPanel.repaint();

        // Repaint the input panel
        revalidate();
        repaint();

//        // Enable the "+ Add" button
//        addDataSetupButton.setEnabled(true);
    }
    
    private void openExcelFileChooser() {
        count = tempList.size();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
        	try {
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
                FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheetAt(0);

                for (Row row : sheet) {
                    Cell firstCell = row.getCell(0);
                    if (firstCell != null && firstCell.getCellTypeEnum() == CellType.STRING && "EOF".equals(firstCell.getStringCellValue().trim())) {
                        break; // Stop processing if "EOF" is found
                    }
                    if (methods.rowIsFullyEmpty(row)) {
                        continue; // Skip fully empty rows
                    }
                    Iterator<Cell> cellIterator = row.cellIterator();
                    tempList.add(new ArrayList<>());
                    tempList.set(count, new ArrayList<>(6));
                    for (int i = 0; i < 6; i++) {
                        try {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellTypeEnum()) {
                            case STRING -> tempList.get(count).add(cell.getStringCellValue());
                            case BLANK -> tempList.get(count).add(""); // Replace empty cells with null
                            case NUMERIC -> tempList.get(count).add(cell.getNumericCellValue());
                            default -> {
                            }
                            // Handle other cell types if needed
                        }
                        }catch (NoSuchElementException e) {
                        	tempList.get(count).add(""); 
//                        	print(e);
						}
//                        if (cell.getCellTypeEnum() == CellType.BLANK) {
//                            tempList.get(count).add(""); // Replace empty cells with null
//                            tempList.get(count).add("");
//                            i += 2;
//                        }
                    }
                    count++;
                }
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            removeEmptyRows();
            try {
				validateFields();
	            populateFieldsOnImport();
			} catch (IncompleteDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
    }

    private void removeEmptyRows() {
        for (List<Object> objects : tempList) {
            boolean isRowEmpty = true;

            for (Object obj : objects) {
                if (obj != null) {
                    isRowEmpty = false;
                    break; // Exit the inner loop as soon as a non-null value is found
                }
            }

            if (!isRowEmpty) {
                tempListUpdated.add(objects);
            }
        }
        tempList = clone(tempListUpdated);
        tempListUpdated.clear();
    }
    
    private void validateFields() throws IncompleteDataException {
    	for(int i = 0; i<tempList.size(); i++) {
            boolean allICFieldsPresent = false;
            boolean someICFieldsPresent = false;
            boolean allMgrFieldsPresent = false;
            boolean someMgrFieldsPresent = false;
    		String icPlanName = (String) tempList.get(i).get(0);
    		String icCPS = (String) tempList.get(i).get(1);
    		String icPEK = (String) tempList.get(i).get(2);
    		String mgrPlanName = (String) tempList.get(i).get(3);
    		String mgrCPS = (String) tempList.get(i).get(4);
    		String mgrPEK = (String) tempList.get(i).get(5);
    		
    		if(!icPlanName.isEmpty() && !icCPS.isEmpty() && !icPEK.isEmpty()) {
    			allICFieldsPresent = true;
    		}
    		else if(!icPlanName.isEmpty() || !icCPS.isEmpty() || !icPEK.isEmpty()) {
    			// for some fields
    			someICFieldsPresent = true;
    			System.out.println(i + "-------- else if 1");
    			System.out.println(icPlanName +"-"+ icCPS +"-"+ icPEK);
    		}
    		
    		if(!mgrPlanName.isEmpty() && !mgrCPS.isEmpty() && !mgrPEK.isEmpty()) {
    			allMgrFieldsPresent = true;
    		}
    		else if(!mgrPlanName.isEmpty() || !mgrCPS.isEmpty() || !mgrPEK.isEmpty()) {
    			someMgrFieldsPresent = true;
    			System.out.println(i + "-------- else if 2");
    			System.out.println(mgrPlanName +"-"+ mgrCPS +"-"+ mgrPEK);
    		}
    		
    		if(someICFieldsPresent || someMgrFieldsPresent) {
    			List<String> missing = new ArrayList<String>();
    			if(icPlanName.isEmpty()) missing.add("IC Plan Name");
    			if(icCPS.isEmpty()) missing.add("IC CPS");
    			if(icPEK.isEmpty()) missing.add("IC PEK");
    			if(mgrPlanName.isEmpty()) missing.add("Mgr Plan Name");
    			if(mgrCPS.isEmpty()) missing.add("Mgr CPS");
    			if(mgrPEK.isEmpty()) missing.add("Mgr PEK");
    			JOptionPane.showMessageDialog(this,missing + 
    					" fields are missing at Row:" + i + " (excluding empty rows).",
                        "Fields Missing Error", JOptionPane.ERROR_MESSAGE);
    			tempList.clear();
    			throw new IncompleteDataException(missing + " fields are missing at Row:" + i + " (excluding empty rows).");
    		}
    		
    	}
    }
//    if IC plan is X and IC CPS is Y but user forgot to insert IC Pek: ask user to go back and update that on excel first
    private List<List<Object>> clone(List<List<Object>> ls) {
        List<List<Object>> lsret = new ArrayList<List<Object>>();
        for (int i = 0; i < ls.size(); i++) {
            lsret.add(new ArrayList<Object>());
            for (int j = 0; j < ls.get(i).size(); j++) {
                lsret.get(i).add(ls.get(i).get(j));
            }
        }
        return lsret;
    }

    private JPanel createInputField(String labelText, String text) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(250, 25));
        textField.setText(text);
        textField.setEditable(false);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNewFieldsPanel(String plan_name, String ic_cps, String ic_pek, String mgr_plan_name, String mgr_cps, String mgr_pek) {
        JPanel newFieldsPanel = new JPanel();
        newFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);

        JPanel icPlanNamePanel = createInputField("IC Plan Name:", plan_name);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPlanNamePanel, gbc2);

        JPanel icCpsPanel = createInputField("IC CPS:", ic_cps);
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        newFieldsPanel.add(icCpsPanel, gbc2);

        JPanel icPekPanel = createInputField("IC PEK:", ic_pek);
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        newFieldsPanel.add(icPekPanel, gbc2);

        JPanel managerPlanNamePanel = createInputField("Manager Plan Name:", mgr_plan_name);
        gbc2.gridx = 3;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPlanNamePanel, gbc2);

        JPanel managerCpsPanel = createInputField("Manager CPS:", mgr_cps);
        gbc2.gridx = 4;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerCpsPanel, gbc2);

        JPanel managerPekPanel = createInputField("Manager PEK:", mgr_pek);
        gbc2.gridx = 5;
        gbc2.gridy = 0;
        newFieldsPanel.add(managerPekPanel, gbc2);
        return newFieldsPanel;
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
                    dataSetupFieldsPanel.revalidate();
                    dataSetupFieldsPanel.repaint();
                } catch (Exception e) {
                    break;
                }
            }
            tempList.clear();
        }
        runDataSetupButton.setEnabled(true);
        clearButton.setEnabled(true);
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

class IncompleteDataException extends Exception {
    public IncompleteDataException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}