package org.example;

import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ProgressSection extends JPanel {

    private JProgressBar progressBar;
    private static JScrollPane scrollPane;
    private JTextArea statusTextArea;
    private Logger logger; // Use SLF4J Logger

    public void exportLogsToFileWrapper() {
        exportLogsToFile(); // Call the private method
    }

    public ProgressSection() {
        setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true); // Enable word wrap
        statusTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        statusTextArea.setMargin(new Insets(10, 10, 10, 10));
        scrollPane = new JScrollPane(statusTextArea);

        logger = LoggerFactory.getLogger(ProgressSection.class); // Initialize the SLF4J logger

        add(progressBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void logInfo(String message, String calculationType) {
        statusTextArea.append(message + "\n");
        logger.info(message); // Use SLF4J logger
        int newProgress = -1;

        if ("e2e".equals(calculationType)) {
            newProgress = calculate_e2eRegression_Progress(message);
        } else if ("validation".equals(calculationType)) {
            newProgress = calculateValidationProgress(message);
        }

        if (message.startsWith("Exception")) {
            newProgress = 0;
        }

        if (newProgress >= 0) {
            setProgress(newProgress);
        }
    }

    public int calculate_e2eRegression_Progress(String message) {
        if (message.startsWith("Exception")) {
            return 0;
        } else if (message.contains("[Info] :: Fetching Plan Details")) {
            return 5;
        } else if (message.contains("[Info] :: Generating Data Setup")) {
            return 5 + 20; // Increment progress by 20% (total progress becomes 30%)
        } else if (message.contains("[Info] :: Uploading Data Setup")) {
            return 25 + 25; // Increment progress by 20% (total progress becomes 50%)
        } else if (message.contains("[Info] :: Calculating Batches")) {
            return 50 + 25; // Increment progress by 25% (total progress becomes 75%)
        } else if (message.contains("[Info] :: Regression Complete")) {
            return 75 + 25; // Increment progress by 25% (total progress becomes 100%)
        }
        return -1;
    }
    public int calculateValidationProgress(String message) {
        if (message.startsWith("Exception")) {
            return 0;
        } else if (message.contains("[Info] :: Validation Initiated for")) {
            return 5;
        } else if (message.contains("[Info] :: Uploading Data")) {
            return 5 + 20; // Increment progress by 20% (total progress becomes 30%)
        } else if (message.contains("[Info] :: Calculating Batches")) {
            return 25 + 25; // Increment progress by 20% (total progress becomes 50%)
        } else if (message.contains("[Info] :: Downloading Results")) {
            return 50 + 25; // Increment progress by 25% (total progress becomes 75%)
        } else if (message.contains("[Info] :: Validation Complete")) {
            return 75 + 25; // Increment progress by 25% (total progress becomes 100%)
        }
        return -1;
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    public void clearLogs() {
        statusTextArea.setText(""); // Clear the text area content
    }

    private void exportLogsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(statusTextArea.getText());
                writer.flush();
                JOptionPane.showMessageDialog(this, "Logs exported successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error exporting logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void redirectSystemOutput() {
        GuiOutputStream outputStream = new GuiOutputStream(statusTextArea);
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));
    }

    private static class TextAreaHandler extends Handler {
        private JTextArea textArea;

        public TextAreaHandler(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void publish(LogRecord record) {
            textArea.append(record.getMessage() + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    public JTextArea getStatusTextArea() {
        return statusTextArea;
    }
}