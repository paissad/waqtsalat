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

import java.util.prefs.Preferences;

/**
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WaqtSalatPrefs {

	/**
	 * Some known settings for the GUI ...
	 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
	 *
	 */
	public static enum guiSettings {

		DISPLAY_NEXT_PRAY_IN_MENUBAR,
		DISPLAY_ICON_IN_MENUBAR,
		START_AT_LOGIN,
		CHECK_FOR_DAILY_UPDATES,
		DISPLAY_NAME_COMBOBOX,
		DISPLAY_TIME_COMBOBOX,
	}

	public static Preferences userPrefs;
	public static Preferences systemPrefs;

	static {
		WaqtSalatPrefs o = new WaqtSalatPrefs();
		userPrefs   = Preferences.userRoot().node(o.getClass().getPackage().getName());
		systemPrefs = Preferences.systemRoot().node(o.getClass().getPackage().getName());
		o = null;
	}

	public static void printInfos() {  // To remove ... (only for testing purpose !!!)
		String format = new String("%-20s : %s");
		System.out.println(String.format(format, "Absolute path", userPrefs.absolutePath()));
	}

	/*
	 * (boolean) DISPLAY_NEXT_PRAY_IN_MENUBAR
	 */
}
