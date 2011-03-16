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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JLabel;

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

		JPanel topPanel = new JPanel();
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.fill = GridBagConstraints.BOTH;
		gbc_topPanel.gridx = 0;
		gbc_topPanel.gridy = 0;
		add(topPanel, gbc_topPanel);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{0, 0};
		gbl_topPanel.rowHeights = new int[]{0, 0};
		//gbl_topPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		//gbl_topPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_topPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		topPanel.add(tabbedPane, gbc_tabbedPane);

		JPanel adhansPanel = new JPanel();
		tabbedPane.addTab("Adhans", null, adhansPanel, null);
		GridBagLayout gbl_adhansPanel = new GridBagLayout();
		gbl_adhansPanel.columnWidths = new int[]{0, 0, 0};
		gbl_adhansPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		//gbl_adhansPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		//gbl_adhansPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		adhansPanel.setLayout(gbl_adhansPanel);

		JCheckBox chckbxEnableSilentMode = new JCheckBox("Enable silent mode");
		GridBagConstraints gbc_chckbxEnableSilentMode = new GridBagConstraints();
		gbc_chckbxEnableSilentMode.weightx = 1.0;
		gbc_chckbxEnableSilentMode.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxEnableSilentMode.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxEnableSilentMode.gridx = 0;
		gbc_chckbxEnableSilentMode.gridy = 0;
		chckbxEnableSilentMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// if enable mode is selected, then disable adhan combobox ...
			}
		});
		adhansPanel.add(chckbxEnableSilentMode, gbc_chckbxEnableSilentMode);

		AdhanComboBox adhanComboBox_1 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_1 = new GridBagConstraints();
		gbc_adhanComboBox_1.anchor = GridBagConstraints.WEST;
		gbc_adhanComboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_1.gridx = 1;
		gbc_adhanComboBox_1.gridy = 1;
		adhansPanel.add(adhanComboBox_1, gbc_adhanComboBox_1);

		AdhanComboBox adhanComboBox_2 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_2 = new GridBagConstraints();
		gbc_adhanComboBox_2.anchor = GridBagConstraints.BASELINE;
		gbc_adhanComboBox_2.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_2.gridx = 1;
		gbc_adhanComboBox_2.gridy = 2;
		adhansPanel.add(adhanComboBox_2, gbc_adhanComboBox_2);

		AdhanComboBox adhanComboBox_3 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_3 = new GridBagConstraints();
		gbc_adhanComboBox_3.anchor = GridBagConstraints.BASELINE;
		gbc_adhanComboBox_3.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_3.gridx = 1;
		gbc_adhanComboBox_3.gridy = 3;
		adhansPanel.add(adhanComboBox_3, gbc_adhanComboBox_3);

		AdhanComboBox adhanComboBox_4 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_4 = new GridBagConstraints();
		gbc_adhanComboBox_4.anchor = GridBagConstraints.BASELINE;
		gbc_adhanComboBox_4.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_4.gridx = 1;
		gbc_adhanComboBox_4.gridy = 4;
		adhansPanel.add(adhanComboBox_4, gbc_adhanComboBox_4);

		AdhanComboBox adhanComboBox_5 = new AdhanComboBox();
		GridBagConstraints gbc_adhanComboBox_5 = new GridBagConstraints();
		gbc_adhanComboBox_5.insets = new Insets(0, 0, 5, 0);
		gbc_adhanComboBox_5.anchor = GridBagConstraints.BASELINE;
		gbc_adhanComboBox_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_adhanComboBox_5.gridx = 1;
		gbc_adhanComboBox_5.gridy = 5;
		adhansPanel.add(adhanComboBox_5, gbc_adhanComboBox_5);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.anchor = GridBagConstraints.BASELINE;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 6;
		adhansPanel.add(separator, gbc_separator);

		JLabel lblFadjr = new JLabel("Fadjr:");
		lblFadjr.setLabelFor(adhanComboBox_1);
		GridBagConstraints gbc_lblFadjr = new GridBagConstraints();
		gbc_lblFadjr.anchor = GridBagConstraints.EAST;
		gbc_lblFadjr.insets = new Insets(0, 0, 5, 5);
		gbc_lblFadjr.gridx = 0;
		gbc_lblFadjr.gridy = 1;
		adhansPanel.add(lblFadjr, gbc_lblFadjr);

		JLabel lblDhuhr = new JLabel("Dhuhr:");
		lblDhuhr.setLabelFor(adhanComboBox_2);
		GridBagConstraints gbc_lblDhuhr = new GridBagConstraints();
		gbc_lblDhuhr.anchor = GridBagConstraints.EAST;
		gbc_lblDhuhr.insets = new Insets(0, 0, 5, 5);
		gbc_lblDhuhr.gridx = 0;
		gbc_lblDhuhr.gridy = 2;
		adhansPanel.add(lblDhuhr, gbc_lblDhuhr);

		JLabel lblAsr = new JLabel("Asr:");
		lblAsr.setLabelFor(adhanComboBox_3);
		GridBagConstraints gbc_lblAsr = new GridBagConstraints();
		gbc_lblAsr.anchor = GridBagConstraints.EAST;
		gbc_lblAsr.insets = new Insets(0, 0, 5, 0);
		gbc_lblAsr.gridx = 0;
		gbc_lblAsr.gridy = 3;
		adhansPanel.add(lblAsr, gbc_lblAsr);

		JLabel lblMaghrib = new JLabel("Maghrib:");
		lblMaghrib.setLabelFor(adhanComboBox_4);
		GridBagConstraints gbc_lblMaghrib = new GridBagConstraints();
		gbc_lblMaghrib.anchor = GridBagConstraints.EAST;
		gbc_lblMaghrib.insets = new Insets(0, 0, 5, 0);
		gbc_lblMaghrib.gridx = 0;
		gbc_lblMaghrib.gridy = 4;
		adhansPanel.add(lblMaghrib, gbc_lblMaghrib);

		JLabel lblIsha = new JLabel("Isha:");
		lblIsha.setLabelFor(adhanComboBox_5);
		GridBagConstraints gbc_lblIsha = new GridBagConstraints();
		gbc_lblIsha.anchor = GridBagConstraints.EAST;
		gbc_lblIsha.insets = new Insets(0, 0, 0, 5);
		gbc_lblIsha.gridx = 0;
		gbc_lblIsha.gridy = 5;
		adhansPanel.add(lblIsha, gbc_lblIsha);

	}
}
