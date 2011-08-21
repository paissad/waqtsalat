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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.gui.GuiConstants;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class PreferencesTab extends JPanel {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(PreferencesTab.class);

    JPanel                    settingsPanel;
    JLabel                    lblResetSettings;
    JButton                   btnResetSettings;

    public PreferencesTab() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        settingsPanel = new JPanel();
        GridBagConstraints gbc_settingsPanel = new GridBagConstraints();
        gbc_settingsPanel.insets = new Insets(10, 5, 0, 5);
        gbc_settingsPanel.anchor = GridBagConstraints.WEST;
        gbc_settingsPanel.gridx = 0;
        gbc_settingsPanel.gridy = 0;
        add(settingsPanel, gbc_settingsPanel);
        GridBagLayout gbl_settingsPanel = new GridBagLayout();
        gbl_settingsPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_settingsPanel.rowHeights = new int[] { 0, 0 };
        gbl_settingsPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        gbl_settingsPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        settingsPanel.setLayout(gbl_settingsPanel);

        String resetInfoString_1 = "Restore defaults";
        String resetInfoString_2 = "(Will need the restart of the application !)";
        lblResetSettings = new JLabel("<html>"
                + resetInfoString_1
                + "<br><span style= \"font-style: italic; font-family: Calibri Italic;\">"
                + resetInfoString_2
                + "</span></html>");
        lblResetSettings.setIcon(GuiConstants.ICON_RESET_PREFERENCES);
        lblResetSettings.setIconTextGap(8);
        GridBagConstraints gbc_lblResetSettings = new GridBagConstraints();
        gbc_lblResetSettings.anchor = GridBagConstraints.WEST;
        gbc_lblResetSettings.insets = new Insets(0, 0, 0, 5);
        gbc_lblResetSettings.gridx = 0;
        gbc_lblResetSettings.gridy = 0;
        settingsPanel.add(lblResetSettings, gbc_lblResetSettings);

        btnResetSettings = new JButton("Reset");
        lblResetSettings.setLabelFor(btnResetSettings);
        btnResetSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnResetSettings_actionPerformed(e);
            }
        });
        GridBagConstraints gbc_btnResetSettings = new GridBagConstraints();
        gbc_btnResetSettings.anchor = GridBagConstraints.WEST;
        gbc_btnResetSettings.gridx = 1;
        gbc_btnResetSettings.gridy = 0;
        settingsPanel.add(btnResetSettings, gbc_btnResetSettings);
    }

    // ======================================================================
    // Actions ...

    private void btnResetSettings_actionPerformed(ActionEvent e) {
        logger.info("Restoring all preferences settings to defaults.");
        try {
            userPrefs.clear();
        } catch (BackingStoreException bse) {
            logger.error("Error while restoring settings to defaults.");
            bse.printStackTrace();
            return;
        }
        logger.info("Finished restoring all preferences to defaults.");
    }

    // ======================================================================

}
