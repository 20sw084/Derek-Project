package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ConfigurationPanel extends JPanel {

    private JPanel configFieldsPanel;
    private static JTextField usernameField;
    private static JComboBox<String> environmentComboBox;
    private JFileChooser fileChooser;
    private JLabel selectedFileLabel;

    public ConfigurationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        configFieldsPanel = new JPanel();
        configFieldsPanel.setLayout(new GridBagLayout());

        usernameField = new JTextField(20);
        environmentComboBox = new JComboBox<>(new String[]{"Implement 1",
                "EU Implement 1", "Sandbox 1", "Sandbox 3", "Sandbox 4"});
        GridBagConstraints configGBC = new GridBagConstraints();
        configGBC.insets = new Insets(5, 5, 5, 5);

        configGBC.gridx = 0;
        configGBC.gridy = 0;
        configGBC.anchor = GridBagConstraints.LINE_END;
        configFieldsPanel.add(new JLabel("Username:"), configGBC);

        configGBC.gridx = 1;
        configGBC.gridy = 0;
        configGBC.anchor = GridBagConstraints.LINE_START;
        usernameField.setPreferredSize(new Dimension(200, 25));
        configFieldsPanel.add(usernameField, configGBC);

        configGBC.gridx = 0;
        configGBC.gridy = 2;
        configGBC.anchor = GridBagConstraints.LINE_END;
        configFieldsPanel.add(new JLabel("Environment:"), configGBC);

        configGBC.gridx = 1;
        configGBC.gridy = 2;
        configGBC.anchor = GridBagConstraints.LINE_START;
        configFieldsPanel.add(environmentComboBox, configGBC);

        // Create the file chooser for CSV files
        fileChooser = new JFileChooser();
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(csvFilter);

        // Create the button for selecting CSV files
        JButton selectCSVButton = new JButton("Select Tag Doc");
        selectCSVButton.addActionListener(e -> openCSVFileChooser());
        configGBC.gridx = 1;
        configGBC.gridy = 3;
        configGBC.anchor = GridBagConstraints.LINE_START;
        configFieldsPanel.add(selectCSVButton, configGBC);

        // Create the "Remove" button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeSelectedFile());
        configGBC.gridx = 0;
        configGBC.gridy = 3;
        configGBC.anchor = GridBagConstraints.LINE_END;
        configFieldsPanel.add(removeButton, configGBC);

        // Create the label for displaying selected file path
        selectedFileLabel = new JLabel("Selected CSV File: ");
        configGBC.gridx = 0;
        configGBC.gridy = 4;
        configGBC.gridwidth = 2;
        configGBC.anchor = GridBagConstraints.CENTER;
        configFieldsPanel.add(selectedFileLabel, configGBC);

        add(configFieldsPanel);
    }
    public static String getUsername() {
        return usernameField.getText();
    }
    public static void setUsername(String u) {
        usernameField.setText(u);
    }
    public static String getEnvironment() {
        String selectedOption = Objects.requireNonNull(environmentComboBox.getSelectedItem()).toString();
        return switch (selectedOption) {
            case "Implement 1" -> "implement1";
            case "EU Implement 1" -> "euimplement1";
            case "Sandbox 1" -> "sandbox";
            case "Sandbox 3" -> "sandbox3";
            case "Sandbox 4" -> "sandbox4";
            default -> null;
            // Add more cases for other options if needed
        };
    }
private void openCSVFileChooser() {
        int returnValue = fileChooser.showOpenDialog(ConfigurationPanel.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText("Selected CSV File: " + selectedFile.getName());
            // Process the selected CSV file here
            JOptionPane.showMessageDialog(
                    ConfigurationPanel.this,
                    "Selected CSV File: " + selectedFile.getAbsolutePath(),
                    "File Selection",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    private void removeSelectedFile() {
        selectedFileLabel.setText("Selected CSV File: ");
    }
}
