/*
 * WaqtSalat, for indicating the muslim prayers times in most cities.
 * Copyright (C) 2011  Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 * 
 */

package net.waqtsalat.gui;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AlertsTab extends JPanel {

	private static final long serialVersionUID = 1L;

	public AlertsTab() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JCheckBox checkBox = new JCheckBox("New check box");
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox.gridx = 0;
		gbc_checkBox.gridy = 0;
		panel_1.add(checkBox, gbc_checkBox);
		
		AdhanComboBox adhanComboBox = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox = new GridBagConstraints();
		gbc_adhanComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox.gridx = 0;
		gbc_adhanComboBox.gridy = 1;
		panel_1.add(adhanComboBox, gbc_adhanComboBox);
		
		AdhanComboBox adhanComboBox_1 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_1 = new GridBagConstraints();
		gbc_adhanComboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_1.gridx = 0;
		gbc_adhanComboBox_1.gridy = 2;
		panel_1.add(adhanComboBox_1, gbc_adhanComboBox_1);
		
		AdhanComboBox adhanComboBox_2 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_2 = new GridBagConstraints();
		gbc_adhanComboBox_2.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_2.gridx = 0;
		gbc_adhanComboBox_2.gridy = 3;
		panel_1.add(adhanComboBox_2, gbc_adhanComboBox_2);
		
		AdhanComboBox adhanComboBox_3 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_3 = new GridBagConstraints();
		gbc_adhanComboBox_3.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_3.gridx = 0;
		gbc_adhanComboBox_3.gridy = 4;
		panel_1.add(adhanComboBox_3, gbc_adhanComboBox_3);
		
		AdhanComboBox adhanComboBox_4 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_4 = new GridBagConstraints();
		gbc_adhanComboBox_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_4.gridx = 0;
		gbc_adhanComboBox_4.gridy = 5;
		panel_1.add(adhanComboBox_4, gbc_adhanComboBox_4);

	}
}
