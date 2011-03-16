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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.waqtsalat.Messages;

/**
 * This class represents/builds the menubar of the GUI.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WsMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private JMenu _menuFile;
	private JMenu _menuHelp;

	private JMenuItem _exit;
	private JMenuItem _about;
	private JMenuItem _help;

	private Desktop desk = null;

	// =======================================================================

	public WsMenuBar() {

		if (Desktop.isDesktopSupported()) {
			 desk = Desktop.getDesktop();
		}
		//setPreferredSize(new Dimension(, WsConstants.MENU_PREFERED_HEIGHT));
		setMinimumSize(getPreferredSize());
		setBackground(WsConstants.COLOR_BACKGROUND_MENUBAR);

		createMenuItems();
		createMenus();

		_menuFile.add(_exit);
		_menuHelp.add(_help);
		_menuHelp.add(_about);

		add(_menuFile);
		add(_menuHelp);

	}
	// =======================================================================

	private void createMenuItems() {

		_exit = new JMenuItem(Messages.getString("Quit"));
		_exit.setMnemonic(KeyEvent.VK_Q);
		_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, WsConstants.SHORTCUT_KEY_MASK));
		_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitActionPerformed();
			}
		});

		_help = new JMenuItem(Messages.getString("Help"), KeyEvent.VK_F1);
		_help.setMnemonic(KeyEvent.VK_F1);
		_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		_help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpActionPerformed();
			}
		});

		_about = new JMenuItem(Messages.getString("About"), KeyEvent.VK_0);
		_about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutActionPerformed();
			}
		});
	}
	// =======================================================================

	private void createMenus() {
		_menuFile = new JMenu("WaqtSalat");
		_menuFile.setBackground(WsConstants.COLOR_BACKGROUND_MENU);

		_menuHelp = new JMenu(Messages.getString("MenuHelp"));
		_menuHelp.setBackground(WsConstants.COLOR_BACKGROUND_MENU);
	}
	// =======================================================================

	//TODO: change this method ...
	private void helpActionPerformed() {
		if(desk != null && desk.isSupported(Desktop.Action.BROWSE)) {
			try {
				desk.browse(new URI("http://dev.paissad.net/projects/waqtsalat/wiki"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else {
			// No action ...
		}
	}
	// =======================================================================

	private void exitActionPerformed() {
		MainFrame.exitGUI();
	}
	// =======================================================================

	//TODO: change this method ...
	private void aboutActionPerformed() {
		JDialog aboutDialog = new About();
		aboutDialog.setVisible(true);
	}
	// =======================================================================

}


