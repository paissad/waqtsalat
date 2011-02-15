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

/**
 * This interface contains some preferred/fixed values for the GUI.
 *  
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public interface WsConstants {

	// No need to use 'public static final' with an interface.

	int PREFERED_WIDTH  = 600;
	int PREFERED_HEIGHT = 500;
	int MENU_PREFERED_HEIGHT = 25;
	int SHORTCUT_KEY_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	// Colours ...
	Color COLOR_BACKGROUND_MAINFRAME = Color.LIGHT_GRAY;
	Color COLOR_BACKGROUND_MENUBAR = Color.DARK_GRAY;
	Color COLOR_BACKGROUND_MENU = Color.DARK_GRAY;

}
