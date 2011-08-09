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

package net.paissad.waqtsalat.gui;

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

		LOCATION_FULL_NAME, // String
		LOCATION_COUNTRY_CODE, //String
		LOCATION_VIA_IP, // boolean
		LOCATION_TIMEZONE, // String
		LOCATION_USE_SYS_TIMEZONE, // boolean
		LOCATION_USE_DST, // boolean

		PRAYTIME_MADHAB, // String
		PRAYTIME_METHOD, // String

		SILENT_MODE, // boolean
		ADHAN_COMBOBOXES_CURRENT_STATE, // String (splitted by NUL \0) 
		ADHAN_CURRENT_SOUNDS, // String (splitted by NUL \0)
		ENABLE_NOTIFICATIONS, // boolean
		USE_GROWL, // boolean
	}

	public static Preferences userPrefs;
	public static Preferences systemPrefs;

	static {
		String root = "/waqtsalat";
		userPrefs   = Preferences.userRoot().node(root);
		systemPrefs = Preferences.systemRoot().node(root);
        /*
         * WaqtSalatPrefs o = new WaqtSalatPrefs(); root =
         * o.getClass().getPackage().getName(); userPrefs =
         * Preferences.userRoot().node(root); systemPrefs =
         * Preferences.systemRoot().node(root); o = null;
         */
    }
}
