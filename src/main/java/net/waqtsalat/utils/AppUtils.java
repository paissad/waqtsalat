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

package net.waqtsalat.utils;

import java.util.Observable;

/**
 * Contains some utilities such as update checking for the application ...
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AppUtils extends Observable {

    public static final String APP_UPDATE_URL      = "http://www.waqtsalat.net/download";
    public static final String APP_CURRENT_VERSION = "1.0";

    // =======================================================================

    /**
     * Sole constructor.
     */
    public AppUtils() {
    }

    // =======================================================================

    /**
     * Update the application.
     */
    public void updateApplication() {
        if (isUpdateAvailable()) {

        }
    }

    // =======================================================================

    /**
     * Look for available update for the application.
     * 
     * @return Return true if an update is available, false otherwise.
     */
    public boolean isUpdateAvailable() {
        return false;
    }
    // =======================================================================

}
