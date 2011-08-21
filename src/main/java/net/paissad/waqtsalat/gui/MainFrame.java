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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.BackingStoreException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.WSConstants;
import net.paissad.waqtsalat.gui.addons.ImageLabel;
import net.paissad.waqtsalat.gui.tabs.AdvancedTab;
import net.paissad.waqtsalat.gui.tabs.AlertsTab;
import net.paissad.waqtsalat.gui.tabs.GeneralTab;
import net.paissad.waqtsalat.gui.tabs.LocationTab;
import net.paissad.waqtsalat.gui.tabs.PrayTimesTab;
import net.paissad.waqtsalat.gui.tabs.PreferencesTab;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class MainFrame extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(MainFrame.class);

    private JPanel            topPanel;
    private JPanel            imgHeaderPanel;
    private JTabbedPane       tabbedPane;
    private JPanel            generalPanel;
    private JPanel            locationPanel;
    private JPanel            praytimesPanel;
    private JPanel            alertsPanel;
    private JPanel            preferencesPanel;
    private JPanel            advancedPanel;

    public MainFrame() throws AWTException {

        chooseCustomLookAndFeel();

        this.setTitle(WSConstants.WS_NAME);
        this.setBackground(GuiConstants.COLOR_BACKGROUND_MAINFRAME);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setPreferredSize(new Dimension(GuiConstants.MAINFRAME_PREFERED_WIDTH, GuiConstants.MAINFRAME_PREFERED_HEIGHT));
        this.setMinimumSize(new Dimension(GuiConstants.MAINFRAME_MINIMUM_WIDTH, GuiConstants.MAINFRAME_MININUM_HEIGHT));
        this.setResizable(true);
        this.setJMenuBar(new WsMenuBar());
        this.setLocationRelativeTo(null);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);

        topPanel = new JPanel();
        GridBagConstraints gbc_topPanel = new GridBagConstraints();
        gbc_topPanel.fill = GridBagConstraints.BOTH;
        gbc_topPanel.gridx = 0;
        gbc_topPanel.gridy = 1;
        getContentPane().add(topPanel, gbc_topPanel);
        GridBagLayout gbl_topPanel = new GridBagLayout();
        gbl_topPanel.columnWidths = new int[] { 0, 0 };
        gbl_topPanel.rowHeights = new int[] { 0, 0, 0 };
        gbl_topPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_topPanel.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        topPanel.setLayout(gbl_topPanel);

        // Panel which contains the image ...
        imgHeaderPanel = new JPanel();
        GridBagConstraints gbc_imgHeaderPanel = new GridBagConstraints();
        gbc_imgHeaderPanel.insets = new Insets(0, 0, 5, 0);
        gbc_imgHeaderPanel.fill = GridBagConstraints.BOTH;
        gbc_imgHeaderPanel.gridx = 0;
        gbc_imgHeaderPanel.gridy = 0;
        imgHeaderPanel.setLayout(new BorderLayout(0, 0));
        imgHeaderPanel.add(new ImageLabel(GuiConstants.HEADER_IMAGE_SUNSET));
        topPanel.add(imgHeaderPanel, gbc_imgHeaderPanel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 1;
        topPanel.add(tabbedPane, gbc_tabbedPane);

        // Declarations of the tabs ...
        generalPanel = new GeneralTab();
        locationPanel = new LocationTab();
        praytimesPanel = new PrayTimesTab();
        alertsPanel = new AlertsTab();
        preferencesPanel = new PreferencesTab();
        advancedPanel = new AdvancedTab();

        // Add each tab to the JTabbedPane ...
        tabbedPane.addTab(I18N.getString("Tab.General"),
                GuiConstants.TAB_ICON_GENERAL, generalPanel, null);
        tabbedPane.addTab(I18N.getString("Tab.Location"),
                GuiConstants.TAB_ICON_LOCATION, locationPanel, null);
        tabbedPane.addTab(I18N.getString("Tab.PrayTimes"),
                GuiConstants.TAB_ICON_PRAYTIMES, praytimesPanel, null);
        tabbedPane.addTab(I18N.getString("Tab.Alerts"),
                GuiConstants.TAB_ICON_ALERTS, alertsPanel, null);
        tabbedPane.addTab(I18N.getString("Tab.Preferences"),
                GuiConstants.TAB_ICON_PREFERENCES, preferencesPanel, null);
        tabbedPane.addTab(I18N.getString("Tab.Advanced"),
                GuiConstants.TAB_ICON_ADVANCED, advancedPanel, null);

        tabbedPane.setSelectedComponent(praytimesPanel);

        // Let's initialise the system tray icon !
        new SysTray();
    }

    // =======================================================================

    private void chooseCustomLookAndFeel() {
        boolean isLafAlreadyChoosen = false;
        try {
            if (Platform.isMac()) {
                for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                    if ("Mac OS X".equals(laf.getName())) {
                        UIManager.setLookAndFeel(laf.getClassName());
                        System.setProperty("apple.laf.useScreenMenuBar", "false");
                        System.setProperty("Window.documentModified", "true");
                        isLafAlreadyChoosen = true;
                        break;
                        /*
                         * XXX: What about adding requestUserAttention() too ?
                         * ...
                         * http://developer.apple.com/library/mac/documentation
                         * /Java
                         * /Reference/JavaSE6_AppleExtensionsRef/api/com/apple
                         * /eawt/Application.html
                         */
                    }
                }
            }

            if (!isLafAlreadyChoosen) {
                for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(laf.getName())) {
                        UIManager.setLookAndFeel(laf.getClassName());
                        isLafAlreadyChoosen = true;
                        break;
                    }
                }
            }
        }

        catch (Exception e) {
            logger.error("Unable to load custom Look&Feel : {}", e.getMessage());
            isLafAlreadyChoosen = false;
            // e.printStackTrace();
        } finally {
            if (!isLafAlreadyChoosen) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        LookAndFeel currentLAF = UIManager.getLookAndFeel();
        logger.debug(currentLAF.getDescription());
        logger.debug("Is supported LAF ? : {}", currentLAF.isSupportedLookAndFeel()
                + "");
        logger.debug("LookAndFeel ID     : {}", currentLAF.getID());
        logger.debug("LookAndFeel Name   : {}", currentLAF.getName());
    }

    // =======================================================================

    /**
     * Save Gui preferences/settings and exit.
     */
    public static void exitGUI() {
        try {
            userPrefs.sync();
            userPrefs.flush();
            System.exit(0);
        } catch (BackingStoreException e) {
            logger.error("Abnormal exit : {}", e.getMessage());
            // e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }

    // =======================================================================
}
