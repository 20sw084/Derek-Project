package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.poi.ss.usermodel.*;

public class Methods {
    boolean rowIsFullyEmpty(Row row) {
        for (Cell cell : row) {
            if (cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    JPanel createInputField(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(250, 25));
        textField.setEditable(false);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    JPanel createInputField(String labelText, String text) {
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
    public List<List<Object>> clone(List<List<Object>> ls) {
        List<List<Object>> lsret = new ArrayList<List<Object>>();
        for (int i = 0; i < ls.size(); i++) {
            lsret.add(new ArrayList<Object>());
            for (int j = 0; j < ls.get(i).size(); j++) {
                lsret.get(i).add(ls.get(i).get(j));
            }
        }
        return lsret;
    }
    public String getInputFieldValue(int index, String labelText, List<JPanel> additionalFieldsPanels) {
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
    void usernameDialogBox(JPanel jp, InputPanel inputPanel){

        String username = ((JTextField) jp.getComponent(1)).getText(); // Get the username from the main page

        if (username.isEmpty()) {

            final JButton okay = new JButton("Ok");
            okay.addActionListener(e1 -> {
                JOptionPane pane = getOptionPane((JComponent) e1.getSource());
                pane.setValue(okay);
            });
            okay.setEnabled(false);

            final JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e13 -> {
                JOptionPane pane = getOptionPane((JComponent) e13.getSource());
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

            ((JTextField) jp.getComponent(1)).setText(username);
            if (username == null || username.isEmpty()) {
                // User clicked on Cancel or left the username blank
                SwingUtilities.invokeLater(() -> {
                    inputPanel.startButton.setEnabled(true);
                });
                return; // Exit the listener if user cancels or leaves username blank
            }
        }
    }
    JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
    List<String> getDataFromFields(Component[] comps) {
        List<String> str = new ArrayList<String>();
        for (Component com : comps) {
            JPanel j = (JPanel) com;
            Component tf = j.getComponent(1);
            str.add(((JTextField) tf).getText());
        }
        return str;
    }
    public void removeEmptyRows(List<List<Object>> tempList, List<List<Object>> tempListUpdated) {
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
}
