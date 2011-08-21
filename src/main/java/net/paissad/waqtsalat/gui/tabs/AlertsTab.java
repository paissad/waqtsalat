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

package net.paissad.waqtsalat.gui.tabs;

import static net.paissad.waqtsalat.gui.WaqtSalatPrefs.userPrefs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import com.sun.jna.Platform;

import net.paissad.waqtsalat.gui.AdhanComboBox;
import net.paissad.waqtsalat.gui.GuiConstants;
import net.paissad.waqtsalat.gui.AdhanComboBox.Selection;
import net.paissad.waqtsalat.gui.WaqtSalatPrefs.GuiSettings;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AlertsTab extends JPanel {

    private static final long        serialVersionUID     = 1L;

    private static String            _sep                 = "\0";

    private boolean                  _silentModeState     = userPrefs.getBoolean(
                                                                  GuiSettings.SILENT_MODE.toString(), false);
    private boolean                  _enableNotifications = userPrefs.getBoolean(
                                                                  GuiSettings.ENABLE_NOTIFICATIONS.toString(), false);
    private boolean                  _useGrowl            = userPrefs.getBoolean(
                                                                  GuiSettings.USE_GROWL.toString(), false);

    private ArrayList<JLabel>        _adhansLabels        = new ArrayList<JLabel>();
    private ArrayList<AdhanComboBox> _adhansComboBoxes    = new ArrayList<AdhanComboBox>();

    private JPanel                   topPanel;
    private JTabbedPane              tabbedPane;
    private JPanel                   notificationsPanel;
    private JPanel                   adhansPanel;
    private JCheckBox                chckbxEnableSilentMode;
    private AdhanComboBox            adhanComboBox_1;
    private AdhanComboBox            adhanComboBox_2;
    private AdhanComboBox            adhanComboBox_3;
    private AdhanComboBox            adhanComboBox_4;
    private AdhanComboBox            adhanComboBox_5;
    private JSeparator               separator;
    private JLabel                   lblFadjr;
    private JLabel                   lblDhuhr;
    private JLabel                   lblAsr;
    private JLabel                   lblMaghrib;
    private JLabel                   lblIsha;
    private JCheckBox                chckbxEnableNotifications;
    private JLabel                   lblNotifications;
    private JSeparator               separator_1;
    private JCheckBox                chckbxUseGrowl;
    private JLabel                   lblSoundState;

    public AlertsTab() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        topPanel = new JPanel();
        GridBagConstraints gbc_topPanel = new GridBagConstraints();
        gbc_topPanel.fill = GridBagConstraints.BOTH;
        gbc_topPanel.gridx = 0;
        gbc_topPanel.gridy = 0;
        add(topPanel, gbc_topPanel);
        GridBagLayout gbl_topPanel = new GridBagLayout();
        gbl_topPanel.columnWidths = new int[] { 0, 0 };
        gbl_topPanel.rowHeights = new int[] { 0, 0 };
        gbl_topPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_topPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        topPanel.setLayout(gbl_topPanel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        GridBagLayout gbl_tabbedPane = new GridBagLayout();
        gbl_tabbedPane.columnWidths = new int[] { 0, 0 };
        gbl_tabbedPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        topPanel.add(tabbedPane, gbc_tabbedPane);

        adhansPanel = new JPanel();
        GridBagLayout gbl_adhansPanel = new GridBagLayout();
        gbl_adhansPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_adhansPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_adhansPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_adhansPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        adhansPanel.setLayout(gbl_adhansPanel);
        tabbedPane.addTab("Adhans", GuiConstants.ICON_AUDIO_VOLUME, adhansPanel, null);

        chckbxEnableSilentMode = new JCheckBox("Enable silent mode");
        chckbxEnableSilentMode.setSelected(_silentModeState);
        GridBagConstraints gbc_chckbxEnableSilentMode = new GridBagConstraints();
        gbc_chckbxEnableSilentMode.weightx = 1.0;
        gbc_chckbxEnableSilentMode.fill = GridBagConstraints.HORIZONTAL;
        gbc_chckbxEnableSilentMode.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxEnableSilentMode.gridx = 0;
        gbc_chckbxEnableSilentMode.gridy = 0;
        chckbxEnableSilentMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chckbxEnableSilentMode_actionPerformed(e);
            }
        });

        lblSoundState = new JLabel("");
        this.setIconOfSoundState();
        GridBagConstraints gbc_lblSoundState = new GridBagConstraints();
        gbc_lblSoundState.anchor = GridBagConstraints.WEST;
        gbc_lblSoundState.insets = new Insets(0, 0, 5, 5);
        gbc_lblSoundState.gridx = 1;
        gbc_lblSoundState.gridy = 0;
        adhansPanel.add(lblSoundState, gbc_lblSoundState);
        adhansPanel.add(chckbxEnableSilentMode, gbc_chckbxEnableSilentMode);

        adhanComboBox_1 = new AdhanComboBox();
        _adhansComboBoxes.add(adhanComboBox_1);
        GridBagConstraints gbc_adhanComboBox_1 = new GridBagConstraints();
        gbc_adhanComboBox_1.anchor = GridBagConstraints.WEST;
        gbc_adhanComboBox_1.insets = new Insets(0, 0, 5, 0);
        gbc_adhanComboBox_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_adhanComboBox_1.gridx = 1;
        gbc_adhanComboBox_1.gridy = 2;
        adhansPanel.add(adhanComboBox_1, gbc_adhanComboBox_1);

        adhanComboBox_2 = new AdhanComboBox();
        _adhansComboBoxes.add(adhanComboBox_2);
        GridBagConstraints gbc_adhanComboBox_2 = new GridBagConstraints();
        gbc_adhanComboBox_2.anchor = GridBagConstraints.WEST;
        gbc_adhanComboBox_2.insets = new Insets(0, 0, 5, 0);
        gbc_adhanComboBox_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_adhanComboBox_2.gridx = 1;
        gbc_adhanComboBox_2.gridy = 3;
        adhansPanel.add(adhanComboBox_2, gbc_adhanComboBox_2);

        adhanComboBox_3 = new AdhanComboBox();
        _adhansComboBoxes.add(adhanComboBox_3);
        GridBagConstraints gbc_adhanComboBox_3 = new GridBagConstraints();
        gbc_adhanComboBox_3.anchor = GridBagConstraints.WEST;
        gbc_adhanComboBox_3.insets = new Insets(0, 0, 5, 0);
        gbc_adhanComboBox_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_adhanComboBox_3.gridx = 1;
        gbc_adhanComboBox_3.gridy = 4;
        adhansPanel.add(adhanComboBox_3, gbc_adhanComboBox_3);

        adhanComboBox_4 = new AdhanComboBox();
        _adhansComboBoxes.add(adhanComboBox_4);
        GridBagConstraints gbc_adhanComboBox_4 = new GridBagConstraints();
        gbc_adhanComboBox_4.anchor = GridBagConstraints.WEST;
        gbc_adhanComboBox_4.insets = new Insets(0, 0, 5, 0);
        gbc_adhanComboBox_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_adhanComboBox_4.gridx = 1;
        gbc_adhanComboBox_4.gridy = 5;
        adhansPanel.add(adhanComboBox_4, gbc_adhanComboBox_4);

        adhanComboBox_5 = new AdhanComboBox();
        _adhansComboBoxes.add(adhanComboBox_5);
        GridBagConstraints gbc_adhanComboBox_5 = new GridBagConstraints();
        gbc_adhanComboBox_5.insets = new Insets(0, 0, 5, 0);
        gbc_adhanComboBox_5.anchor = GridBagConstraints.WEST;
        gbc_adhanComboBox_5.fill = GridBagConstraints.HORIZONTAL;
        gbc_adhanComboBox_5.gridx = 1;
        gbc_adhanComboBox_5.gridy = 6;
        adhansPanel.add(adhanComboBox_5, gbc_adhanComboBox_5);

        lblFadjr = new JLabel("Fadjr:");
        _adhansLabels.add(lblFadjr);
        lblFadjr.setLabelFor(adhanComboBox_1);
        GridBagConstraints gbc_lblFadjr = new GridBagConstraints();
        gbc_lblFadjr.anchor = GridBagConstraints.EAST;
        gbc_lblFadjr.insets = new Insets(0, 0, 5, 5);
        gbc_lblFadjr.gridx = 0;
        gbc_lblFadjr.gridy = 2;
        adhansPanel.add(lblFadjr, gbc_lblFadjr);

        lblDhuhr = new JLabel("Dhuhr:");
        _adhansLabels.add(lblDhuhr);
        lblDhuhr.setLabelFor(adhanComboBox_2);
        GridBagConstraints gbc_lblDhuhr = new GridBagConstraints();
        gbc_lblDhuhr.anchor = GridBagConstraints.EAST;
        gbc_lblDhuhr.insets = new Insets(0, 0, 5, 5);
        gbc_lblDhuhr.gridx = 0;
        gbc_lblDhuhr.gridy = 3;
        adhansPanel.add(lblDhuhr, gbc_lblDhuhr);

        lblAsr = new JLabel("Asr:");
        _adhansLabels.add(lblAsr);
        lblAsr.setLabelFor(adhanComboBox_3);
        GridBagConstraints gbc_lblAsr = new GridBagConstraints();
        gbc_lblAsr.anchor = GridBagConstraints.EAST;
        gbc_lblAsr.insets = new Insets(0, 0, 5, 5);
        gbc_lblAsr.gridx = 0;
        gbc_lblAsr.gridy = 4;
        adhansPanel.add(lblAsr, gbc_lblAsr);

        lblMaghrib = new JLabel("Maghrib:");
        _adhansLabels.add(lblMaghrib);
        lblMaghrib.setLabelFor(adhanComboBox_4);
        GridBagConstraints gbc_lblMaghrib = new GridBagConstraints();
        gbc_lblMaghrib.anchor = GridBagConstraints.EAST;
        gbc_lblMaghrib.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaghrib.gridx = 0;
        gbc_lblMaghrib.gridy = 5;
        adhansPanel.add(lblMaghrib, gbc_lblMaghrib);

        lblIsha = new JLabel("Isha:");
        _adhansLabels.add(lblIsha);
        lblIsha.setLabelFor(adhanComboBox_5);
        GridBagConstraints gbc_lblIsha = new GridBagConstraints();
        gbc_lblIsha.anchor = GridBagConstraints.EAST;
        gbc_lblIsha.insets = new Insets(0, 0, 5, 5);
        gbc_lblIsha.gridx = 0;
        gbc_lblIsha.gridy = 6;
        adhansPanel.add(lblIsha, gbc_lblIsha);

        separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.insets = new Insets(0, 0, 0, 5);
        gbc_separator.anchor = GridBagConstraints.BASELINE;
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 7;
        adhansPanel.add(separator, gbc_separator);

        notificationsPanel = new JPanel();
        GridBagLayout gbl_notificationsPanel = new GridBagLayout();
        gbl_notificationsPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_notificationsPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gbl_notificationsPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        gbl_notificationsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        notificationsPanel.setLayout(gbl_notificationsPanel);
        tabbedPane.addTab("Notifications", GuiConstants.ICON_NOTIFICATION_2, notificationsPanel, null);

        chckbxEnableNotifications = new JCheckBox("Enable notifications");
        chckbxEnableNotifications.setSelected(_enableNotifications);
        chckbxEnableNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chckbxEnableNotifications_actionPerformed(e);
            }
        });
        GridBagConstraints gbc_chckbxEnableNotifications = new GridBagConstraints();
        gbc_chckbxEnableNotifications.insets = new Insets(0, 5, 5, 5);
        gbc_chckbxEnableNotifications.anchor = GridBagConstraints.WEST;
        gbc_chckbxEnableNotifications.gridx = 0;
        gbc_chckbxEnableNotifications.gridy = 0;
        notificationsPanel.add(chckbxEnableNotifications, gbc_chckbxEnableNotifications);

        String notificationNote_11 = "Notifications will alert you when it is time to pray.";
        String notificationNote_12 = "The notifications will fade automatically.";
        String notificationNote_13 = "A small window will be displayed at the corner of your screen.";
        lblNotifications = new JLabel("<html>"
                + notificationNote_11 + "<br>"
                + notificationNote_12 + "<br>"
                + notificationNote_13 + "</html>"
                );
        lblNotifications.setFont(new Font(
                "Calibri", lblNotifications.getFont().getStyle() |
                        Font.ITALIC, lblNotifications.getFont().getSize()));
        lblNotifications.setIcon(GuiConstants.ICON_NOTIFICATION_1);
        GridBagConstraints gbc_lblNotifications = new GridBagConstraints();
        gbc_lblNotifications.insets = new Insets(0, 5, 5, 5);
        gbc_lblNotifications.anchor = GridBagConstraints.WEST;
        gbc_lblNotifications.gridx = 0;
        gbc_lblNotifications.gridy = 1;
        notificationsPanel.add(lblNotifications, gbc_lblNotifications);

        separator_1 = new JSeparator();
        separator_1.setForeground(Color.BLACK);
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.gridwidth = 2;
        gbc_separator_1.insets = new Insets(0, 5, 5, 5);
        gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator_1.gridx = 0;
        gbc_separator_1.gridy = 2;
        notificationsPanel.add(separator_1, gbc_separator_1);

        if (Platform.isMac()) {
            chckbxUseGrowl = new JCheckBox("Use Growl for the notifications instead of the default.");
            chckbxUseGrowl.setSelected(_useGrowl);
            chckbxUseGrowl.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chckbxUseGrowl_actionPerformed(e);
                }
            });
            GridBagConstraints gbc_chckbxUseGrowl = new GridBagConstraints();
            gbc_chckbxUseGrowl.fill = GridBagConstraints.NONE;
            gbc_chckbxUseGrowl.insets = new Insets(0, 5, 5, 5);
            gbc_chckbxUseGrowl.anchor = GridBagConstraints.WEST;
            gbc_chckbxUseGrowl.gridx = 0;
            gbc_chckbxUseGrowl.gridy = 3;
            notificationsPanel.add(chckbxUseGrowl, gbc_chckbxUseGrowl);
        }

        chckbxEnableSilentMode_actionPerformed(null); // It is run for the only
                                                      // purpose to disable or
                                                      // enable the ComboBoxes !
        initActionListenersForAdhanComboBoxes();

    }

    // =======================================================================
    // Actions ...

    private void chckbxEnableSilentMode_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxEnableSilentMode.isSelected();
        String default_adhan_mode = Selection.DEFAULT_ADHAN_SOUND.toString();

        for (int i = 0; i < _adhansComboBoxes.size(); i++) {
            _adhansComboBoxes.get(i).setEnabled(!currentSelection);
        }

        for (int i = 0; i < _adhansLabels.size(); i++) {
            _adhansLabels.get(i).setEnabled(!currentSelection);
        }

        if (currentSelection == false) {
            // FIXME:
        }

        setIconOfSoundState();
        userPrefs.putBoolean(
                GuiSettings.SILENT_MODE.toString(), currentSelection);
    }

    // -------------

    private void chckbxEnableNotifications_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxEnableNotifications.isSelected();
        userPrefs.putBoolean(
                GuiSettings.ENABLE_NOTIFICATIONS.toString(), currentSelection);
    }

    // -------------

    private void chckbxUseGrowl_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxUseGrowl.isSelected();
        userPrefs.putBoolean(
                GuiSettings.USE_GROWL.toString(), currentSelection);
    }

    // =======================================================================

    private void initActionListenersForAdhanComboBoxes() {
        for (int i = 0; i < _adhansComboBoxes.size(); i++) {
            _adhansComboBoxes.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    // =======================================================================

    private void updateAdhansSettings() {
        String[] all_combobox_states = new String[5];
        String[] all_adhans_sounds = new String[5];

        // Let's retrieve the values the stored preferences values.
        all_combobox_states = userPrefs.get(
                GuiSettings.ADHAN_COMBOBOXES_CURRENT_STATE.toString(), "").split(_sep);
        all_adhans_sounds = userPrefs.get(
                GuiSettings.ADHAN_CURRENT_SOUNDS.toString(), "").split(_sep);

        for (int i = 0; i < 5; i++) {
            if ((all_combobox_states[i] == null)
                    || (all_combobox_states[i].isEmpty())) {
                all_combobox_states[i] = Selection.DEFAULT_ADHAN_SOUND.toString();
            }
            if ((all_adhans_sounds[i] == null)
                    || all_adhans_sounds[i].isEmpty()) {
                all_adhans_sounds[i] = "";
            }
        }

        for (int i = 0; i < _adhansComboBoxes.size(); i++) {
            // 'acb' like AdhanComboBox !
            AdhanComboBox acb = _adhansComboBoxes.get(i);
            String current_selected_item = (String) acb.getSelectedItem();
            String rawName = getSelectionRawName(current_selected_item);
            if (rawName.equals(Selection.NONE.toString())) {
                all_adhans_sounds[i] = ""; // no sound for this pray time.
            } else if (rawName.equals(Selection.DEFAULT_ADHAN_SOUND.toString())) {
                all_adhans_sounds[i] = GuiConstants.DEFAULT_ADHAN_SOUND; // default
                                                                        // sound
                                                                        // for
                                                                        // this
                                                                        // pray
                                                                        // time.
            } else if (rawName.equals(Selection.SELECT_FILE.toString())) {
                all_adhans_sounds[i] = ""; // FIXME: check the selected file
                                           // from AdhanComboBox ...
            }

        }
    }

    // =======================================================================

    private void setSelected_AdhanStates(String[] states) {
        // concatenation ...
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < states.length; i++) {
            sb.append(states[i]).append(_sep);
        }
        System.out.println("Saving states : " + sb.toString()); // REMOVE:
                                                                // TESTING:
        userPrefs.put(
                GuiSettings.ADHAN_COMBOBOXES_CURRENT_STATE.toString(), sb.toString());
    }

    private void setSelected_AdhanSounds(String[] sounds) {
        // Let's concatenate the String array into one single String with the
        // the separator 'sep' between each field ...
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sounds.length; i++) {
            sb.append(sounds[i]).append(_sep);
        }
        System.out.println("Saving sounds : " + sb.toString()); // REMOVE:
                                                                // TESTING:
        userPrefs.put(
                GuiSettings.ADHAN_CURRENT_SOUNDS.toString(), sb.toString());
    }

    private void setIconOfSoundState() {
        if (chckbxEnableSilentMode.isSelected() == true) {
            lblSoundState.setIcon(GuiConstants.ICON_SOUND_OFF);
        } else {
            lblSoundState.setIcon(GuiConstants.ICON_SOUND_ON);
        }
    }

    // =======================================================================

    /**
     * Return something like <i>NONE</i>, <i>SELECT_FILE</i> or
     * <i>DEFAULT_ADHAN_SOUND</i>
     * 
     * @param selectionFullName
     * @return
     */
    private String getSelectionRawName(String selectionFullName) {
        Selection[] keys = Selection.values();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].getDescName().equals(selectionFullName))
                return keys[i].toString();
        }
        return null;
    }

    // =======================================================================

}
