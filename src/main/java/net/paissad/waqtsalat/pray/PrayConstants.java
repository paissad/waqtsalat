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

package net.paissad.waqtsalat.pray;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.paissad.waqtsalat.I18N;
import net.paissad.waqtsalat.pray.PrayTime.AdjustingMethod;
import net.paissad.waqtsalat.pray.PrayTime.CalcMethod;
import net.paissad.waqtsalat.pray.PrayTime.JuristicMethod;
import net.paissad.waqtsalat.pray.PrayTime.PrayName;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class PrayConstants {

    /** The pray names as keys with their translations as values. */
    public static final Map<PrayName, String>        PRAY_NAMES        = getPrayNames();

    /** The known calculation methods with their descriptions as values. */
    public static final Map<CalcMethod, String>      CALC_METHODS      = getCalcMethods();

    /** The known adjusting methods with their descriptions as values. */
    public static final Map<AdjustingMethod, String> ADJUSTING_METHODS = getAdjustingMethods();

    /** The known juristic methods with their descriptions as values. */
    public static final Map<JuristicMethod, String>  JURISTIC_METHODS  = getJuristicMethods();

    // _________________________________________________________________________

    private static final Map<PrayName, String> getPrayNames() {
        Map<PrayName, String> names = new LinkedHashMap<PrayTime.PrayName, String>();
        names.put(PrayName.FADJR, I18N.getString("prayname.Fadjr"));
        names.put(PrayName.SUNRISE, I18N.getString("prayname.Sunrise"));
        names.put(PrayName.DHUHR, I18N.getString("prayname.Dhuhr"));
        names.put(PrayName.ASR, I18N.getString("prayname.Asr"));
        names.put(PrayName.SUNSET, I18N.getString("prayname.Sunset"));
        names.put(PrayName.MAGHRIB, I18N.getString("prayname.Maghrib"));
        names.put(PrayName.ISHA, I18N.getString("prayname.Isha"));
        return names;
    }

    // _________________________________________________________________________

    private static final Map<AdjustingMethod, String> getAdjustingMethods() {
        Map<AdjustingMethod, String> result = new HashMap<PrayTime.AdjustingMethod, String>();
        result.put(AdjustingMethod.ANGLE_BASED, I18N.getString("adjustingMethod.angle_based"));
        result.put(AdjustingMethod.MIDNIGHT, I18N.getString("adjustingMethod.midnight"));
        result.put(AdjustingMethod.NONE, I18N.getString("adjustingMethod.none"));
        result.put(AdjustingMethod.ONE_SEVENTH, I18N.getString("adjustingMethod.one_seventh"));
        return result;
    }

    // _________________________________________________________________________

    private static final Map<CalcMethod, String> getCalcMethods() {
        Map<CalcMethod, String> methods = new HashMap<PrayTime.CalcMethod, String>();
        methods.put(CalcMethod.CUSTOM, "calcMethod.custom");
        methods.put(CalcMethod.EGYPT, "calcMethod.egypt");
        methods.put(CalcMethod.ISNA, "calcMethod.isna");
        methods.put(CalcMethod.JAFARI, "calcMethod.jafari");
        methods.put(CalcMethod.KARACHI, "calcMethod.karachi");
        methods.put(CalcMethod.MAKKAH, "calcMethod.makkah");
        methods.put(CalcMethod.MWL, "calcMethod.mwl");
        methods.put(CalcMethod.TEHRAN, "calcMethod.tehran");
        return methods;
    }

    // _________________________________________________________________________

    private static final Map<JuristicMethod, String> getJuristicMethods() {
        Map<JuristicMethod, String> result = new HashMap<PrayTime.JuristicMethod, String>();
        result.put(JuristicMethod.SHAFII, I18N.getString("juristicMethod.shafii"));
        result.put(JuristicMethod.HANAFI, I18N.getString("juristicMethod.hanafi"));
        return result;
    }

    // _________________________________________________________________________
}
