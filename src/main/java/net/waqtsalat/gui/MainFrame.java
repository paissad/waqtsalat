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

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.waqtsalat.Messages;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {

		this.setTitle("WaqtSalat");
		this.setBackground(WsConstants.COLOR_BACKGROUND_MAINFRAME);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setPreferredSize(new Dimension(WsConstants.PREFERED_WIDTH, WsConstants.PREFERED_HEIGHT));
		this.setSize(getPreferredSize());
		this.setResizable(true);
		this.setMinimumSize(getPreferredSize());
		this.setJMenuBar(new WsMenuBar());
		this.setLocationRelativeTo(null);
		this.pack();
		
		JMenuBar menuBar = new WsMenuBar();
		setJMenuBar(menuBar);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel topPanel = new JPanel();
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
		
		JPanel headerPanel = new JPanel();
		GridBagConstraints gbc_headerPanel = new GridBagConstraints();
		gbc_headerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_headerPanel.fill = GridBagConstraints.BOTH;
		gbc_headerPanel.gridx = 0;
		gbc_headerPanel.gridy = 0;
		topPanel.add(headerPanel, gbc_headerPanel);
		GridBagLayout gbl_headerPanel = new GridBagLayout();
		gbl_headerPanel.columnWidths = new int[]{0};
		gbl_headerPanel.rowHeights = new int[]{0};
		gbl_headerPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_headerPanel.rowWeights = new double[]{Double.MIN_VALUE};
		headerPanel.setLayout(gbl_headerPanel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		topPanel.add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.General"), null, panel, null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.Location"), null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.PrayTimes"), null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.Alerts"), null, panel_3, null);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.Preferences"), null, panel_4, null);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab(Messages.getString("Tab.Advanced"), null, panel_5, null);

	}

}
