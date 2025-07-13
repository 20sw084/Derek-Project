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

public class ValidationModule extends JPanel {
    private final JFileChooser fileChooser;
    private JLabel selectedFileLabel; // New label to display selected file path
    static JButton validateButton, removeButton, selectExcelButton;
    JPanel validateFieldsPanel;
    JPasswordField pwdField = new JPasswordField();
    private int count = 0;
    public static File selectedFile;

    public ValidationModule(ActionListener validateButtonListener) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        validateFieldsPanel = new JPanel();
        validateFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints validateGBC = new GridBagConstraints();
        validateGBC.insets = new Insets(5, 5, 5, 5);

        // Create the file chooser for Excel files
        fileChooser = new JFileChooser();
        FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(excelFilter);

        // Create the button for selecting Excel files
        selectExcelButton = new JButton("Select Excel File");
        selectExcelButton.addActionListener(e -> {
			try {
				openExcelFileChooser();
			} catch (EncryptedDocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        validateGBC.gridx = 0; // Changed from 1
        validateGBC.gridy = 0;
        validateGBC.anchor = GridBagConstraints.LINE_START;
        validateFieldsPanel.add(selectExcelButton, validateGBC);

        // Create the Validate button
        validateButton = new JButton("Validate");
        validateButton.addActionListener(validateButtonListener);
        validateButton.setEnabled(false);
//        validateButton.addActionListener(e -> {
//        	validateButtonListener;
//            // Add your validation logic here
//            JOptionPane.showMessageDialog(ValidationModule.this, "Validation Successful!");
//        });
        validateGBC.gridx = 1; // Changed from 0
        validateGBC.gridy = 0;
        validateGBC.anchor = GridBagConstraints.LINE_END;
        validateFieldsPanel.add(validateButton, validateGBC);

        // Create the label for displaying selected file path
        selectedFileLabel = new JLabel("Selected Excel File: ");
        validateGBC.gridx = 0;
        validateGBC.gridy = 1;
        validateGBC.gridwidth = 2;
        validateGBC.anchor = GridBagConstraints.CENTER;
        validateFieldsPanel.add(selectedFileLabel, validateGBC);

        // Create the "Remove" button
        removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeSelectedFile());
        validateGBC.gridx = 0;
        validateGBC.gridy = 3;
        validateGBC.anchor = GridBagConstraints.LINE_END;
        validateFieldsPanel.add(removeButton, validateGBC);
        add(validateFieldsPanel);
        removeButton.setEnabled(false);
    }

    protected JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }

    private void openExcelFileChooser() throws EncryptedDocumentException, InvalidFormatException {
        int returnValue = fileChooser.showOpenDialog(ValidationModule.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            // Check if the selected file has a valid Excel file extension
            String fileName = selectedFile.getName();
            if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
                // Display a warning message to the user
                JOptionPane.showMessageDialog(ValidationModule.this,
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
                            ValidationModule.this,
                            "Selected Excel File: " + selectedFile.getAbsolutePath(),
                            "File Selection",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    validateButton.setEnabled(true);
                    removeButton.setEnabled(true);
                } else {
                    // Display a warning message if required sheets are missing
                    JOptionPane.showMessageDialog(ValidationModule.this,
                            "The Excel file must contain 'People', 'Position', 'Quota', etc. sheets.",
                            "Missing Sheets Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any exceptions that may occur during file processing
                JOptionPane.showMessageDialog(ValidationModule.this,
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
        removeButton.setEnabled(false);
        validateButton.setEnabled(false);
    }
}
