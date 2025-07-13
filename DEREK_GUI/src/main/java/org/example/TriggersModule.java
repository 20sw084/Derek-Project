package org.example;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class TriggersModule extends JPanel {
    static JButton runGenericTriggers, selectDataFile, removeDataFile;
    private final JFileChooser fileChooser;
    private JLabel selectedFileLabel;
    public static File selectedFile;
    JPanel generateTriggersPanel;
    public TriggersModule(ActionListener runTriggersButton) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        generateTriggersPanel = new JPanel();
        generateTriggersPanel.setLayout(new GridBagLayout());
        GridBagConstraints genTriggers = new GridBagConstraints();
        genTriggers.insets = new Insets(5, 5, 5, 5);
        // Create the file chooser for Excel files
        fileChooser = new JFileChooser();
        FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(excelFilter);
        // Create the Select Data File button
        selectDataFile = new JButton("Select Data File");
        selectDataFile.addActionListener(e -> openExcelFileChooser()); // Reuse the same ActionListener
        selectDataFile.setEnabled(true);

        // Create the Remove Data File button
        removeDataFile = new JButton("Remove Data File");
        removeDataFile.addActionListener(e -> openExcelFileChooser()); // Reuse the same ActionListener
        removeDataFile.setEnabled(false); // Initially disabled
        
        // Create the label for displaying selected file path
        selectedFileLabel = new JLabel("Selected Excel File: ");
        genTriggers.gridx = 0;
        genTriggers.gridy = 1;

        // Create the Run Generic Triggers button
        runGenericTriggers = new JButton("Run Generic Trigger");
        runGenericTriggers.addActionListener(runTriggersButton);
        runGenericTriggers.setEnabled(false);

        genTriggers.gridx = 0;
        genTriggers.gridy = 0;
        genTriggers.anchor = GridBagConstraints.LINE_START;
        generateTriggersPanel.add(selectDataFile, genTriggers);

        genTriggers.gridx = 1;
        generateTriggersPanel.add(runGenericTriggers, genTriggers);

        genTriggers.gridx = 0;
        genTriggers.gridy = 1;
        genTriggers.gridwidth = 2;
        genTriggers.anchor = GridBagConstraints.CENTER;
        generateTriggersPanel.add(selectedFileLabel, genTriggers);
        
        genTriggers.gridy = 2;
        generateTriggersPanel.add(removeDataFile, genTriggers);
        
        add(generateTriggersPanel);
    }
    private void openExcelFileChooser() {
        int returnValue = fileChooser.showOpenDialog(TriggersModule.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            // Check if the selected file has a valid Excel file extension
            String fileName = selectedFile.getName();
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
                // Display a warning message to the user
                JOptionPane.showMessageDialog(TriggersModule.this,
                        "Please select a valid Excel file with .xls or .xlsx extension.",
                        "File Format Error", JOptionPane.WARNING_MESSAGE);
                return; // Exit the method without further processing
            }

            try (Workbook workbook = WorkbookFactory.create(selectedFile)) {
                // Check if the required sheets exist
                if (hasRequiredSheets(workbook)) {
                    selectedFileLabel.setText("Selected Excel File: " + selectedFile.getName());
                    // Process the selected Excel file here
                    JOptionPane.showMessageDialog(
                            TriggersModule.this,
                            "Selected Excel File: " + selectedFile.getAbsolutePath(),
                            "File Selection",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    runGenericTriggers.setEnabled(true);
                    removeDataFile.setEnabled(true);
                } else {
                    // Display a warning message if required sheets are missing
                    JOptionPane.showMessageDialog(TriggersModule.this,
                            "The Excel file must contain 'People', 'Position', 'Quota', etc. sheets.",
                            "Missing Sheets Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
                e.printStackTrace();
                // Handle any exceptions that may occur during file processing
                JOptionPane.showMessageDialog(TriggersModule.this,
                        "An error occurred while processing the Excel file.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean hasRequiredSheets(Workbook workbook) {
        String[] requiredSheets = {"People", "Position", "Quota", /* Add more sheet names as needed */};
        for (String sheetName : requiredSheets) {
            if (workbook.getSheet(sheetName) == null) {
                return false; // Required sheet is missing
            }
        }
        return true; // All required sheets exist
    }
    private void removeSelectedFile() {
        selectedFileLabel.setText("Selected CSV File: ");
        removeDataFile.setEnabled(false);
        runGenericTriggers.setEnabled(false);
    }
}
