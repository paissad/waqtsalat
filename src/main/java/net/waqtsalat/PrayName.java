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

package net.waqtsalat;

import java.util.ArrayList;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public enum PrayName {
    FADJR(Messages.getString("prayname.Fadjr")),
    SUNRISE(Messages.getString("prayname.Sunrise")),
    DHUHR(Messages.getString("prayname.Dhuhr")),
    ASR(Messages.getString("prayname.Asr")),
    SUNSET(Messages.getString("prayname.Sunset")),
    MAGHRIB(Messages.getString("prayname.Maghrib")),
    ISHA(Messages.getString("prayname.Isha"));

    private String                   _standardName;
    private static ArrayList<String> _standardNameList = new ArrayList<String>();

    PrayName(String name) {
        _standardName = name;
    }

    public String getName() {
        return _standardName;
    }

    public static ArrayList<String> getNamesList() {
        for (PrayName pn : values())
            _standardNameList.add(pn.getName());
        return _standardNameList;
    }
}
