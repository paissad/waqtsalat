/*
 * WaqtSalat, for indicating the muslim prayers times in most cities. Copyright
 * (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat.gui;

import static net.paissad.waqtsalat.gui.WaqtSalatPrefs.userPrefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.paissad.waqtsalat.gui.WaqtSalatPrefs.guiSettings;
import net.paissad.waqtsalat.utils.StartupSession;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class GeneralTab extends JPanel {

    private static final long serialVersionUID         = 1L;

    private JPanel            prayPanel;
    private JCheckBox         chckbxDisplayNextPray;
    private JLabel            lblDisplayName;
    private JComboBox         comboBoxDisplayName;
    private JLabel            lblDisplayTime;
    private JComboBox         comboBoxDisplayTime;
    private JCheckBox         chckbxDisplayIconInMenubar;
    private JSeparator        separator;
    private JPanel            sysPanel;
    private JCheckBox         chckbxStartAtLogin;
    private JCheckBox         chckbxCheckForDailyUpdates;
    private JButton           btnCheckNow;

    private boolean           displayNextPraySelection = userPrefs.getBoolean(
                                                               guiSettings.DISPLAY_NEXT_PRAY_IN_MENUBAR.toString(), true);

    private enum choiceDisplayName {
        FULL_NAME("Full Name"),
        NONE("None");

        String name;

        private choiceDisplayName(String value) {
            name = value;
        }

        private String getName() {
            return name;
        }
    }

    private HashMap<choiceDisplayName, String> _cdn =
                                                            new HashMap<GeneralTab.choiceDisplayName, String>();

    private enum choiceDisplayTime {
        TIME_OF_NEXT_PRAY("Time of next pray"),
        NONE("None");

        String name;

        private choiceDisplayTime(String value) {
            name = value;
        }

        private String getName() {
            return name;
        }
    }

    private HashMap<choiceDisplayTime, String> _cdt =
                                                            new HashMap<GeneralTab.choiceDisplayTime, String>();

    public GeneralTab() {

        init_ChoiceDisplayName();
        init_choiceDisplayTime();

        setMaximumSize(new Dimension(200, 200));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        prayPanel = new JPanel();
        GridBagConstraints gbc_prayPanel = new GridBagConstraints();
        gbc_prayPanel.anchor = GridBagConstraints.BASELINE;
        gbc_prayPanel.gridwidth = 2;
        gbc_prayPanel.insets = new Insets(5, 5, 0, 5);
        gbc_prayPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prayPanel.gridx = 0;
        gbc_prayPanel.gridy = 0;
        add(prayPanel, gbc_prayPanel);
        GridBagLayout gbl_prayPanel = new GridBagLayout();
        gbl_prayPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_prayPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gbl_prayPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_prayPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        prayPanel.setLayout(gbl_prayPanel);

        chckbxDisplayNextPray = new JCheckBox("Display next pray in menu bar");
        GridBagConstraints gbc_chckbxDisplayNextPray = new GridBagConstraints();
        gbc_chckbxDisplayNextPray.anchor = GridBagConstraints.WEST;
        gbc_chckbxDisplayNextPray.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxDisplayNextPray.gridwidth = 2;
        gbc_chckbxDisplayNextPray.gridx = 0;
        gbc_chckbxDisplayNextPray.gridy = 0;
        chckbxDisplayNextPray.setSelected(displayNextPraySelection);
        chckbxDisplayNextPray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayNextPray_actionPerformed(e);
            }
        });
        prayPanel.add(chckbxDisplayNextPray, gbc_chckbxDisplayNextPray);

        lblDisplayName = new JLabel("Display Name");
        GridBagConstraints gbc_lblDisplayName = new GridBagConstraints();
        gbc_lblDisplayName.insets = new Insets(0, 0, 5, 5);
        gbc_lblDisplayName.gridx = 0;
        gbc_lblDisplayName.gridy = 1;
        lblDisplayName.setEnabled(displayNextPraySelection);
        prayPanel.add(lblDisplayName, gbc_lblDisplayName);

        comboBoxDisplayName = new JComboBox();
        comboBoxDisplayName.setModel(new DefaultComboBoxModel(
                _cdn.values().toArray(new String[_cdn.size()])));
        GridBagConstraints gbc_comboBoxDisplayName = new GridBagConstraints();
        gbc_comboBoxDisplayName.anchor = GridBagConstraints.WEST;
        gbc_comboBoxDisplayName.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxDisplayName.gridx = 1;
        gbc_comboBoxDisplayName.gridy = 1;
        comboBoxDisplayName.setEnabled(displayNextPraySelection);
        comboBoxDisplayName.setSelectedItem((String) userPrefs.get(
                guiSettings.DISPLAY_NAME_COMBOBOX.toString(),
                choiceDisplayName.FULL_NAME.getName()));
        comboBoxDisplayName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doAction_itemChanged_DisplayName();
            }
        });
        prayPanel.add(comboBoxDisplayName, gbc_comboBoxDisplayName);

        lblDisplayTime = new JLabel("Display Time");
        GridBagConstraints gbc_lblDisplayTime = new GridBagConstraints();
        gbc_lblDisplayTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblDisplayTime.gridx = 0;
        gbc_lblDisplayTime.gridy = 2;
        lblDisplayTime.setEnabled(displayNextPraySelection);
        prayPanel.add(lblDisplayTime, gbc_lblDisplayTime);

        comboBoxDisplayTime = new JComboBox();
        comboBoxDisplayTime.setModel(new DefaultComboBoxModel(
                _cdt.values().toArray(new String[_cdt.size()])));
        GridBagConstraints gbc_comboBoxDisplayTime = new GridBagConstraints();
        gbc_comboBoxDisplayTime.anchor = GridBagConstraints.WEST;
        gbc_comboBoxDisplayTime.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxDisplayTime.gridx = 1;
        gbc_comboBoxDisplayTime.gridy = 2;
        comboBoxDisplayTime.setEnabled(displayNextPraySelection);
        comboBoxDisplayTime.setSelectedItem((String) userPrefs.get(
                guiSettings.DISPLAY_TIME_COMBOBOX.toString(),
                choiceDisplayTime.TIME_OF_NEXT_PRAY.getName()));
        comboBoxDisplayTime.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doAction_itemChanged_DisplayTime();
            }
        });
        prayPanel.add(comboBoxDisplayTime, gbc_comboBoxDisplayTime);

        chckbxDisplayIconInMenubar = new JCheckBox("Display icon in menu bar");
        GridBagConstraints gbc_chckbxDisplayIconIn = new GridBagConstraints();
        gbc_chckbxDisplayIconIn.anchor = GridBagConstraints.WEST;
        gbc_chckbxDisplayIconIn.insets = new Insets(0, 0, 0, 5);
        gbc_chckbxDisplayIconIn.gridx = 0;
        gbc_chckbxDisplayIconIn.gridy = 3;
        chckbxDisplayIconInMenubar.setSelected(
                userPrefs.getBoolean(guiSettings.DISPLAY_ICON_IN_MENUBAR.toString(), true));
        chckbxDisplayIconInMenubar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayIconInMenubar_actionPerformed(e);
            }
        });
        prayPanel.add(chckbxDisplayIconInMenubar, gbc_chckbxDisplayIconIn);

        separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator.insets = new Insets(0, 10, 5, 10);
        gbc_separator.gridwidth = 2;
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 1;
        gbc_separator.weightx = 1.0;
        add(separator, gbc_separator);

        sysPanel = new JPanel();
        GridBagConstraints gbc_sysPanel = new GridBagConstraints();
        gbc_sysPanel.anchor = GridBagConstraints.BASELINE;
        gbc_sysPanel.insets = new Insets(0, 5, 5, 5);
        gbc_sysPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_sysPanel.gridx = 0;
        gbc_sysPanel.gridy = 2;
        add(sysPanel, gbc_sysPanel);
        GridBagLayout gbl_sysPanel = new GridBagLayout();
        gbl_sysPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_sysPanel.rowHeights = new int[] { 0, 0, 0, 0 };
        // gbl_sysPanel.columnWeights = new double[]{1.0, 0.0,
        // Double.MIN_VALUE};
        gbl_sysPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        sysPanel.setLayout(gbl_sysPanel);

        chckbxStartAtLogin = new JCheckBox("Start at login");
        GridBagConstraints gbc_chckbxStartAtLogin = new GridBagConstraints();
        gbc_chckbxStartAtLogin.anchor = GridBagConstraints.WEST;
        gbc_chckbxStartAtLogin.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxStartAtLogin.gridx = 0;
        gbc_chckbxStartAtLogin.gridy = 0;
        chckbxStartAtLogin.setSelected(
                userPrefs.getBoolean(guiSettings.START_AT_LOGIN.toString(), false));
        chckbxStartAtLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startAtLogin_actionPerformed(e);
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        sysPanel.add(chckbxStartAtLogin, gbc_chckbxStartAtLogin);

        chckbxCheckForDailyUpdates = new JCheckBox("Check for updates daily");
        GridBagConstraints gbc_chckbxCheckForUpdates = new GridBagConstraints();
        gbc_chckbxCheckForUpdates.anchor = GridBagConstraints.WEST;
        gbc_chckbxCheckForUpdates.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxCheckForUpdates.gridx = 0;
        gbc_chckbxCheckForUpdates.gridy = 1;
        chckbxCheckForDailyUpdates.setSelected(
                userPrefs.getBoolean(guiSettings.CHECK_FOR_DAILY_UPDATES.toString(), true));
        chckbxCheckForDailyUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForDailyUpdates_actionPerformed(e);
            }
        });
        sysPanel.add(chckbxCheckForDailyUpdates, gbc_chckbxCheckForUpdates);

        btnCheckNow = new JButton("Check now");
        GridBagConstraints gbc_btnCheckNow = new GridBagConstraints();
        gbc_btnCheckNow.anchor = GridBagConstraints.WEST;
        gbc_btnCheckNow.insets = new Insets(0, 0, 5, 0);
        gbc_btnCheckNow.gridx = 1;
        gbc_btnCheckNow.gridy = 1;
        btnCheckNow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkUpdateNow_actionPerformed(e);
            }
        });
        sysPanel.add(btnCheckNow, gbc_btnCheckNow);

    }

    // =======================================================================
    // Actions ...

    private void displayNextPray_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxDisplayNextPray.isSelected();
        lblDisplayName.setEnabled(currentSelection);
        lblDisplayTime.setEnabled(currentSelection);
        comboBoxDisplayName.setEnabled(currentSelection);
        comboBoxDisplayTime.setEnabled(currentSelection);
        userPrefs.putBoolean(
                guiSettings.DISPLAY_NEXT_PRAY_IN_MENUBAR.toString(), currentSelection);
        if (currentSelection == true) {
            // TODO ... add the pray name in the menu bar ...
        }
    }

    // --------------------------

    private void displayIconInMenubar_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxDisplayIconInMenubar.isSelected();
        userPrefs.putBoolean(
                guiSettings.DISPLAY_NEXT_PRAY_IN_MENUBAR.toString(), currentSelection);
        if (currentSelection == true) {
            // TODO ... add the icon into the menu bar ...
        }
    }

    // --------------------------

    private void startAtLogin_actionPerformed(ActionEvent e) throws Exception {
        boolean currentSelection = chckbxStartAtLogin.isSelected();
        userPrefs.putBoolean(
                guiSettings.START_AT_LOGIN.toString(), currentSelection);
        if (currentSelection == true) {
            StartupSession.addStartupAtLogin();
        } else {
            StartupSession.removeStartupAtLogin();
        }
    }

    // --------------------------

    private void checkForDailyUpdates_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxCheckForDailyUpdates.isSelected();
        userPrefs.putBoolean(
                guiSettings.CHECK_FOR_DAILY_UPDATES.toString(), currentSelection);
        // TODO ... some work to complete ...
    }

    // --------------------------

    private void checkUpdateNow_actionPerformed(ActionEvent e) {
        // TODO ... Check for update now
    }

    // --------------------------

    private void doAction_itemChanged_DisplayName() {
        String currentSelection = (String) comboBoxDisplayName.getSelectedItem();
        userPrefs.put(guiSettings.DISPLAY_NAME_COMBOBOX.toString(), currentSelection);
        // TODO ... continue the work ...
    }

    // --------------------------

    private void doAction_itemChanged_DisplayTime() {
        String currentSelection = (String) comboBoxDisplayTime.getSelectedItem();
        userPrefs.put(guiSettings.DISPLAY_TIME_COMBOBOX.toString(), currentSelection);
        // TODO ... continue the work ...
    }

    // --------------------------

    // =======================================================================

    private void init_ChoiceDisplayName() {
        choiceDisplayName[] all = choiceDisplayName.values();
        for (int i = 0; i < all.length; i++) {
            _cdn.put(all[i], all[i].getName());
        }
    }

    private void init_choiceDisplayTime() {
        choiceDisplayTime[] all = choiceDisplayTime.values();
        for (int i = 0; i < all.length; i++) {
            _cdt.put(all[i], all[i].getName());
        }
    }

    // =======================================================================

}
