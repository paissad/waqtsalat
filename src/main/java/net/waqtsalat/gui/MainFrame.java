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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.prefs.BackingStoreException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import net.waqtsalat.Messages;
import net.waqtsalat.gui.addons.ImageLabel;
import net.waqtsalat.utils.Utils;
import static net.waqtsalat.gui.WaqtSalatPrefs.userPrefs;
import static net.waqtsalat.WaqtSalat.logger;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel topPanel;
	private JPanel imgHeaderPanel;
	private JTabbedPane tabbedPane;
	private JPanel generalPanel;
	private JPanel locationPanel;
	private JPanel praytimesPanel;
	private JPanel alertsPanel;
	private JPanel preferencesPanel;
	private JPanel advancedPanel;

	public MainFrame() {

		chooseCustomLookAndFeel();

		this.setTitle("WaqtSalat");
		this.setBackground(WsConstants.COLOR_BACKGROUND_MAINFRAME);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setPreferredSize(new Dimension(WsConstants.MAINFRAME_PREFERED_WIDTH, WsConstants.MAINFRAME_PREFERED_HEIGHT));
		this.setMinimumSize(new Dimension(WsConstants.MAINFRAME_MINIMUM_WIDTH, WsConstants.MAINFRAME_MININUM_HEIGHT));
		this.setResizable(true);
		this.setJMenuBar(new WsMenuBar());
		this.setLocationRelativeTo(null);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		topPanel = new JPanel();
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.fill = GridBagConstraints.BOTH;
		gbc_topPanel.gridx = 0;
		gbc_topPanel.gridy = 1;
		getContentPane().add(topPanel, gbc_topPanel);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{0, 0};
		gbl_topPanel.rowHeights = new int[]{0, 0, 0};
		gbl_topPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_topPanel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_topPanel);

		// Panel which contains the image ...
		imgHeaderPanel = new JPanel();
		GridBagConstraints gbc_imgHeaderPanel = new GridBagConstraints();
		gbc_imgHeaderPanel.insets = new Insets(0, 0, 5, 0);
		gbc_imgHeaderPanel.fill = GridBagConstraints.BOTH;
		gbc_imgHeaderPanel.gridx = 0;
		gbc_imgHeaderPanel.gridy = 0;
		topPanel.add(imgHeaderPanel, gbc_imgHeaderPanel);
		imgHeaderPanel.setLayout(new BorderLayout(0, 0));
		imgHeaderPanel.add(new ImageLabel(WsConstants.HEADER_IMAGE_SUNSET));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); 
		//tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT); // REMOVE: TESTING:
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		topPanel.add(tabbedPane, gbc_tabbedPane);

		// Declarations of the tabs ...
		generalPanel     = new GeneralTab();
		locationPanel    = new LocationTab();
		praytimesPanel   = new PrayTimesTab();
		alertsPanel      = new AlertsTab();
		preferencesPanel = new PreferencesTab();
		advancedPanel    = new AdvancedTab();

		// Add each tab to the JTabbedPane ...
		tabbedPane.addTab(Messages.getString("Tab.General"),
				WsConstants.TAB_ICON_GENERAL, generalPanel, null);
		tabbedPane.addTab(Messages.getString("Tab.Location"),
				WsConstants.TAB_ICON_LOCATION, locationPanel, null);
		tabbedPane.addTab(Messages.getString("Tab.PrayTimes"),
				WsConstants.TAB_ICON_PRAYTIMES, praytimesPanel, null);
		tabbedPane.addTab(Messages.getString("Tab.Alerts"),
				WsConstants.TAB_ICON_ALERTS, alertsPanel, null);
		tabbedPane.addTab(Messages.getString("Tab.Preferences"),
				WsConstants.TAB_ICON_PREFERENCES, preferencesPanel, null);
		tabbedPane.addTab(Messages.getString("Tab.Advanced"),
				WsConstants.TAB_ICON_ADVANCED, advancedPanel, null);

	}

	// =======================================================================

	private void chooseCustomLookAndFeel() {
		Utils os = new Utils();
		try {
			if (os.isMac()) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else { // Not (Linux, Mac, Windows) operating system ... 
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
		}
		catch (Exception e) {
			logger.error("Unable to load custom Look&Feel : {}", e.getMessage());
			//e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e2) {e2.printStackTrace();}
		}
		LookAndFeel currentLAF = UIManager.getLookAndFeel();
		logger.debug(currentLAF.getDescription());
		logger.debug("Is supported LAF ? : {}", currentLAF.isSupportedLookAndFeel() + ""); // TODO: Change the log level to 'TRACE'
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
		} catch (BackingStoreException e) {
			logger.error("Abnormal exit : {}", e.getMessage());
			//e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	// =======================================================================

}
