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

import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import net.paissad.waqtsalat.exception.PrayException;
import net.paissad.waqtsalat.pray.PrayTime.AdjustingMethod;
import net.paissad.waqtsalat.pray.PrayTime.CalcMethod;
import net.paissad.waqtsalat.pray.PrayTime.JuristicMethod;
import net.paissad.waqtsalat.pray.PrayTime.PrayName;
import net.paissad.waqtsalat.pray.PrayTime.TimeFormat;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class PrayUtils {

    /**
     * Get The pray times for the 5 known pray times, plus sunrise and sunset
     * times too.
     * 
     * @param cal
     *            - The date for which we want the pray times.
     * @param settings
     *            - The settings to use.
     * @return A <tt>Map</tt> containing the times.
     * @throws PrayException
     */
    public static Map<PrayName, Calendar> getPrayersTime(
            final Calendar cal,
            final PraySettings settings) throws PrayException {

        Calendar cal2 = (Calendar) cal.clone();
        TimeZone tz = settings.getTimeZone();

        // The number of hours between the specified time and UTC time.
        cal2.setTimeZone(tz);
        double timeZoneOffset = tz.getOffset(cal2.getTimeInMillis()) / TimeUnit.HOURS.toMillis(1);

        PrayTime prayers = new PrayTime();

        TimeFormat timeFormat = settings.getTimeFormat();
        if (!timeFormat.equals(TimeFormat.TIME_24)) {
            throw new PrayException("Only 24-Hour format is supported by the application for computing prayer times.");
        }

        prayers.setTimeFormat(timeFormat);
        prayers.setCalcMethod(settings.getCalcMethod());
        prayers.setAsrJuristic(settings.getAsrJuristic());
        prayers.setAdjustHighLats(settings.getAdjustingMethod());
        prayers.tune(settings.getOffsets());

        // The times of each pray.
        List<String> prayerTimes = prayers.getPrayerTimes(
                cal,
                settings.getLatitude(),
                settings.getLongitude(),
                timeZoneOffset);

        // The names of each pray.
        PrayName[] prayerNames = PrayName.values();

        EnumMap<PrayName, Calendar> result = new EnumMap<PrayName, Calendar>(PrayName.class);
        cal2.set(Calendar.SECOND, 0);

        for (int i = 0; i < prayerNames.length; i++) {
            Calendar cal3 = (Calendar) cal2.clone();

            String[] hourAndMinute = prayerTimes.get(i).split(":");
            final int hour = Integer.parseInt(hourAndMinute[0]);
            final int minute = Integer.parseInt(hourAndMinute[1]);

            cal3.set(Calendar.HOUR_OF_DAY, hour);
            cal3.set(Calendar.MINUTE, minute);

            result.put(prayerNames[i], cal3);
        }

        return result;
    }

    // _________________________________________________________________________

    /**
     * Get the next schedule time for a pray by using the specified settings.
     * 
     * @param ref
     *            - The date from which we add one day before computing the
     *            next pray time.
     * @param name
     *            - The name of the pray for which we want the next schedule
     *            time.
     * @param settings
     *            - The settings to use for calculating the time.
     * @return The next schedule time.
     * @throws PrayException
     */
    public static Calendar getNextPrayScheduleTime(
            final Calendar ref,
            final PrayName name,
            final PraySettings settings) throws PrayException {

        Calendar cal = (Calendar) ref.clone();
        // Let's one day to the current calendar in order to get the next pray
        // time (for the following/coming day)
        cal.add(Calendar.SECOND, (int) TimeUnit.DAYS.toSeconds(1));
        Map<PrayName, Calendar> prayerTimes = getPrayersTime(cal, settings);
        return prayerTimes.get(name);
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! XXX
     */
    public static void main(String[] args) throws Exception {

        Calendar cal = Calendar.getInstance();

        float latitude = 43.6000f;
        float longitude = 3.8833f;
        int[] offsets = { 0, 0, 0, 0, 0, 0, 0 };

        PraySettings settings = new PraySettings(
                latitude, longitude, cal.getTimeZone(), offsets,
                TimeFormat.TIME_24,
                CalcMethod.JAFARI,
                JuristicMethod.SHAFII,
                AdjustingMethod.ANGLE_BASED);

        Map<PrayName, Calendar> result = getPrayersTime(cal, settings);

        System.out.println("The pray times for " + cal.getTime() + "\n");
        for (PrayName name : result.keySet()) {
            System.out.format("%-15s : %s\n", PrayConstants.PRAY_NAMES.get(name), result.get(name).getTime());
        }
        System.out.println("-----------------------------------------------\n");

        Calendar nextFADJR = getNextPrayScheduleTime(cal, PrayName.FADJR, settings);
        System.out.println("Next FADJR --> " + nextFADJR.getTime());
    }
}
