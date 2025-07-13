package org.example;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
public class DataCleanupModule extends JPanel {
	static JButton executeButton;

	public DataCleanupModule(ActionListener cleanUpButtonListener) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel dataCleanupFieldsPanel = new JPanel();
		dataCleanupFieldsPanel.setLayout(new GridBagLayout());
		GridBagConstraints dcGBC = new GridBagConstraints();
		dcGBC.insets = new Insets(5, 5, 5, 5);

		// Create the Validate button
		executeButton = new JButton("Clean up Test Data");
		executeButton.addActionListener(cleanUpButtonListener);
		dcGBC.gridx = 0;
		dcGBC.gridy = 0;
		dcGBC.anchor = GridBagConstraints.LINE_END;
		dataCleanupFieldsPanel.add(executeButton, dcGBC);

		add(dataCleanupFieldsPanel);
	}
}
