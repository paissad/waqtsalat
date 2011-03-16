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

import java.awt.Color;
import java.awt.Toolkit;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This interface contains some preferred/fixed values for the GUI.
 *  
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public interface WsConstants {

	// No need to use 'public static final' with an Interface.

	String FS = File.separator;

	int MAINFRAME_PREFERED_WIDTH  = 500;
	int MAINFRAME_PREFERED_HEIGHT = 400;
	int MENUBAR_PREFERED_HEIGHT   = 25;
	int IMAGE_HEADER_PANEL_HEIGHT = 50;
	int SHORTCUT_KEY_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	// Colours ...
	Color COLOR_BACKGROUND_MAINFRAME = Color.LIGHT_GRAY;
	Color COLOR_BACKGROUND_MENUBAR = Color.LIGHT_GRAY;
	Color COLOR_BACKGROUND_MENU = Color.LIGHT_GRAY;

	ImageIcon HEADER_IMAGE_SUNSET = new ImageIcon(ClassLoader.getSystemResource("images" +FS+ "sunset_red.jpg"));

	//About dialog ...
	int ABOUT_WINDOW_PREFERED_WIDTH  = 200;
	int ABOUT_WINDOW_PREFERED_HEIGHT = 200;

	// Icons ...
	Icon TAB_ICON_GENERAL     = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ ""));
	Icon TAB_ICON_LOCATION    = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ "earth.png"));
	Icon TAB_ICON_PREFERENCES = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ "preferences.png"));
	Icon TAB_ICON_PRAYTIMES   = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ ""));
	Icon TAB_ICON_ALERTS      = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ "alert.png"));
	Icon TAB_ICON_ADVANCED    = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ ""));
	Icon SEARCH_LOCATION      = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ "search-icon-1.png"));
	//Icon SEARCH_LOCATION      = new ImageIcon(ClassLoader.getSystemResource("icons" +FS+ "search-icon-2.png"));

	// Adhans
	String DEFAULT_ADHAN_SOUND = ClassLoader.getSystemResource("sounds" +FS+ "adhan.mp3").getPath();
	String DEFAULT_ADHAN_DIR   = "extras" +FS+ "sounds" +FS+ "praytimes.org" +FS+ "audio" +FS+ "adhan";

	// Supported audio files: additional extensions apart from the default ones (wav, au, aif ...)
	// Use lowercase !
	String[] ADDITIONAL_EXTENSIONS = {"mp3"};

}
