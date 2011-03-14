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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;

import net.waqtsalat.ComputePrayTimes;
import net.waqtsalat.ComputePrayTimes.CalculationMethod;
import net.waqtsalat.ComputePrayTimes.JuristicMethod;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class PrayTimesTab extends JPanel {

	private static final long serialVersionUID = 1L;

	private HashMap<CalculationMethod, String> _methods = new HashMap<ComputePrayTimes.CalculationMethod, String>();
	private HashMap<JuristicMethod, String> _madhabs = new HashMap<ComputePrayTimes.JuristicMethod, String>();

	public PrayTimesTab() {

		initMadhab_Names();
		initMethod_Names();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JPanel madhabPanel = new JPanel();
		GridBagConstraints gbc_madhabPanel = new GridBagConstraints();
		gbc_madhabPanel.anchor = GridBagConstraints.BASELINE;
		gbc_madhabPanel.insets = new Insets(5, 15, 0, 15);
		gbc_madhabPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_madhabPanel.gridx = 0;
		gbc_madhabPanel.gridy = 0;
		add(madhabPanel, gbc_madhabPanel);
		GridBagLayout gbl_madhabPanel = new GridBagLayout();
		madhabPanel.setLayout(gbl_madhabPanel);

		JLabel lblMadhab = new JLabel("Madhab:");
		GridBagConstraints gbc_lblMadhab = new GridBagConstraints();
		gbc_lblMadhab.insets = new Insets(0, 0, 5, 5);
		gbc_lblMadhab.gridx = 0;
		gbc_lblMadhab.gridy = 0;
		madhabPanel.add(lblMadhab, gbc_lblMadhab);

		JRadioButton rdbtnShafii = new JRadioButton(_madhabs.get(JuristicMethod.SHAFII));
		rdbtnShafii.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_rdbtnShafii = new GridBagConstraints();
		gbc_rdbtnShafii.weightx = 1.0;
		gbc_rdbtnShafii.anchor = GridBagConstraints.WEST;
		gbc_rdbtnShafii.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnShafii.gridx = 1;
		gbc_rdbtnShafii.gridy = 0;
		madhabPanel.add(rdbtnShafii, gbc_rdbtnShafii);

		JRadioButton rdbtnHanafi = new JRadioButton(_madhabs.get(JuristicMethod.HANAFI));
		rdbtnHanafi.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_rdbtnHanafi = new GridBagConstraints();
		gbc_rdbtnHanafi.weightx = 1.0;
		gbc_rdbtnHanafi.anchor = GridBagConstraints.WEST;
		gbc_rdbtnHanafi.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnHanafi.gridx = 1;
		gbc_rdbtnHanafi.gridy = 1;
		madhabPanel.add(rdbtnHanafi, gbc_rdbtnHanafi);

		JPanel methodPanel = new JPanel();
		GridBagConstraints gbc_methodPanel = new GridBagConstraints();
		gbc_methodPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_methodPanel.anchor = GridBagConstraints.BASELINE;
		gbc_methodPanel.insets = new Insets(0, 15, 5, 15);
		gbc_methodPanel.gridx = 0;
		gbc_methodPanel.gridy = 1;
		add(methodPanel, gbc_methodPanel);
		GridBagLayout gbl_methodPanel = new GridBagLayout();
		gbl_methodPanel.columnWidths = new int[]{0, 0, 0};
		gbl_methodPanel.rowHeights = new int[]{0, 0, 0};
		methodPanel.setLayout(gbl_methodPanel);

		JLabel lblMethod = new JLabel("Method:");
		GridBagConstraints gbc_lblMethod = new GridBagConstraints();
		gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblMethod.gridx = 0;
		gbc_lblMethod.gridy = 0;
		methodPanel.add(lblMethod, gbc_lblMethod);

		JComboBox comboBoxMethod = new JComboBox();
		comboBoxMethod.setModel(new DefaultComboBoxModel(
				_methods.values().toArray(new String[_methods.size()])
		));
		GridBagConstraints gbc_comboBoxMethod = new GridBagConstraints();
		gbc_comboBoxMethod.weightx = 1.0;
		gbc_comboBoxMethod.anchor = GridBagConstraints.WEST;
		gbc_comboBoxMethod.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxMethod.gridx = 1;
		gbc_comboBoxMethod.gridy = 0;
		methodPanel.add(comboBoxMethod, gbc_comboBoxMethod);

		JLabel lblNotice = new JLabel("<html>" +
				"Please, select the pray time settings so that<br>" +
				"it matches the prayer times of you Masjid as closely as possible." +
		"</html>");
		GridBagConstraints gbc_lblNotice = new GridBagConstraints();
		gbc_lblNotice.anchor = GridBagConstraints.WEST;
		gbc_lblNotice.gridwidth = 2;
		gbc_lblNotice.insets = new Insets(0, 0, 0, 5);
		gbc_lblNotice.gridx = 0;
		gbc_lblNotice.gridy = 1;
		methodPanel.add(lblNotice, gbc_lblNotice);

	}

	// ======================================================================

	private void initMethod_Names() {
		CalculationMethod[] allMethods = CalculationMethod.values();
		for (int i=0; i<allMethods.length; i++) {
			_methods.put(allMethods[i], allMethods[i].getName());
		}

		// Let's remove method 'CUSTOM' from the ComboBox,
		// we may add it in the future, but not now ...
		_methods.remove(CalculationMethod.CUSTOM);
	}

	private void initMadhab_Names() {
		JuristicMethod[] allMadhabs = JuristicMethod.values();
		for (int i=0; i<allMadhabs.length; i++) {
			_madhabs.put(allMadhabs[i], allMadhabs[i].getName());
		}
	}
	// ======================================================================

}
