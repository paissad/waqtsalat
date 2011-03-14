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

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSeparator;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class GeneralTab extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public GeneralTab() {
		
		setMaximumSize(new Dimension(200, 200));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		//gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel prayPanel = new JPanel();
		GridBagConstraints gbc_prayPanel = new GridBagConstraints();
		gbc_prayPanel.anchor = GridBagConstraints.BASELINE;
		gbc_prayPanel.gridwidth = 2;
		gbc_prayPanel.insets = new Insets(5, 5, 0, 5);
		gbc_prayPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_prayPanel.gridx = 0;
		gbc_prayPanel.gridy = 0;
		add(prayPanel, gbc_prayPanel);
		GridBagLayout gbl_prayPanel = new GridBagLayout();
		gbl_prayPanel.columnWidths = new int[]{0, 0, 0};
		gbl_prayPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_prayPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_prayPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		prayPanel.setLayout(gbl_prayPanel);
		
		JCheckBox chckbxDisplayNextPray = new JCheckBox("Display next pray in menu bar");
		GridBagConstraints gbc_chckbxDisplayNextPray = new GridBagConstraints();
		gbc_chckbxDisplayNextPray.anchor = GridBagConstraints.WEST;
		gbc_chckbxDisplayNextPray.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDisplayNextPray.gridwidth = 2;
		gbc_chckbxDisplayNextPray.gridx = 0;
		gbc_chckbxDisplayNextPray.gridy = 0;
		prayPanel.add(chckbxDisplayNextPray, gbc_chckbxDisplayNextPray);
		
		JLabel lblDisplayName = new JLabel("Display Name");
		GridBagConstraints gbc_lblDisplayName = new GridBagConstraints();
		gbc_lblDisplayName.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisplayName.gridx = 0;
		gbc_lblDisplayName.gridy = 1;
		prayPanel.add(lblDisplayName, gbc_lblDisplayName);
		
		JComboBox comboBoxDisplayName = new JComboBox();
		GridBagConstraints gbc_comboBoxDisplayName = new GridBagConstraints();
		gbc_comboBoxDisplayName.anchor = GridBagConstraints.WEST;
		gbc_comboBoxDisplayName.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxDisplayName.gridx = 1;
		gbc_comboBoxDisplayName.gridy = 1;
		prayPanel.add(comboBoxDisplayName, gbc_comboBoxDisplayName);
		
		JLabel lblDisplayTime = new JLabel("Display Time");
		GridBagConstraints gbc_lblDisplayTime = new GridBagConstraints();
		gbc_lblDisplayTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisplayTime.gridx = 0;
		gbc_lblDisplayTime.gridy = 2;
		prayPanel.add(lblDisplayTime, gbc_lblDisplayTime);
		
		JComboBox comboBoxDisplayTime = new JComboBox();
		GridBagConstraints gbc_comboBoxDisplayTime = new GridBagConstraints();
		gbc_comboBoxDisplayTime.anchor = GridBagConstraints.WEST;
		gbc_comboBoxDisplayTime.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxDisplayTime.gridx = 1;
		gbc_comboBoxDisplayTime.gridy = 2;
		prayPanel.add(comboBoxDisplayTime, gbc_comboBoxDisplayTime);
		
		JCheckBox chckbxDisplayIconIn = new JCheckBox("Display icon in menu bar");
		GridBagConstraints gbc_chckbxDisplayIconIn = new GridBagConstraints();
		gbc_chckbxDisplayIconIn.anchor = GridBagConstraints.WEST;
		gbc_chckbxDisplayIconIn.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxDisplayIconIn.gridx = 0;
		gbc_chckbxDisplayIconIn.gridy = 3;
		prayPanel.add(chckbxDisplayIconIn, gbc_chckbxDisplayIconIn);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 10, 5, 10);
		gbc_separator.gridwidth = 2;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		gbc_separator.weightx = 1.0;
		add(separator, gbc_separator);
		
		JPanel sysPanel = new JPanel();
		GridBagConstraints gbc_sysPanel = new GridBagConstraints();
		gbc_sysPanel.anchor = GridBagConstraints.BASELINE;
		gbc_sysPanel.insets = new Insets(0, 5, 5, 5);
		gbc_sysPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_sysPanel.gridx = 0;
		gbc_sysPanel.gridy = 2;
		add(sysPanel, gbc_sysPanel);
		GridBagLayout gbl_sysPanel = new GridBagLayout();
		gbl_sysPanel.columnWidths = new int[]{0, 0, 0};
		gbl_sysPanel.rowHeights = new int[]{0, 0, 0, 0};
		//gbl_sysPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_sysPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		sysPanel.setLayout(gbl_sysPanel);
		
		JCheckBox chckbxStartAtLogin = new JCheckBox("Start at login");
		GridBagConstraints gbc_chckbxStartAtLogin = new GridBagConstraints();
		gbc_chckbxStartAtLogin.anchor = GridBagConstraints.WEST;
		gbc_chckbxStartAtLogin.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxStartAtLogin.gridx = 0;
		gbc_chckbxStartAtLogin.gridy = 0;
		sysPanel.add(chckbxStartAtLogin, gbc_chckbxStartAtLogin);
		
		JCheckBox chckbxCheckForUpdates = new JCheckBox("Check for updates daily");
		GridBagConstraints gbc_chckbxCheckForUpdates = new GridBagConstraints();
		gbc_chckbxCheckForUpdates.anchor = GridBagConstraints.WEST;
		gbc_chckbxCheckForUpdates.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCheckForUpdates.gridx = 0;
		gbc_chckbxCheckForUpdates.gridy = 1;
		sysPanel.add(chckbxCheckForUpdates, gbc_chckbxCheckForUpdates);
		
		JButton btnCheckNow = new JButton("Check now");
		GridBagConstraints gbc_btnCheckNow = new GridBagConstraints();
		gbc_btnCheckNow.anchor = GridBagConstraints.WEST;
		gbc_btnCheckNow.insets = new Insets(0, 0, 5, 0);
		gbc_btnCheckNow.gridx = 1;
		gbc_btnCheckNow.gridy = 1;
		sysPanel.add(btnCheckNow, gbc_btnCheckNow);
		
	}
}
