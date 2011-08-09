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

package net.paissad.waqtsalat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class ComputePrayTimes extends Observable implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ComputePrayTimes.class);
    
    // ======================================================================

    public static enum CalculationMethod { // Method ...

        JAFARI("Ithna Ashari, Jafari"),
        KARACHI("University of Islamic Sciences, Karachi"),
        ISNA("Islamic Society of North America (ISNA)"),
        MWL("Muslim World League (MWL)"),
        MAKKAH("Umm al-Qura, Makkah"),
        EGYPT("Egyptian General Authority of Survey"),
        TEHRAN("Institute of Geophysics, University of Tehran"),
        CUSTOM("Custom");

        private String methodName;

        private CalculationMethod(String name) {
            methodName = name;
        };

        public String getName() {
            return methodName;
        }

    };

    // ======================================================================

    public static enum JuristicMethod { // Madhab ...
        SHAFII("Shafii"),
        HANAFI("Hanafi");

        private String madhabName;

        private JuristicMethod(String name) {
            madhabName = name;
        }

        public String getName() {
            return madhabName;
        }
    };

    // ======================================================================

    public static enum AdjustingMethod {
        NONE,
        MIDNIGHT,
        ONE_SEVENTH,
        ANGLE_BASED
    };

    // ======================================================================

    private static enum State {
        CHANGED,
        NOT_CHANGED
    };

    // ======================================================================

    private static PrayTime          _prayers         = new PrayTime();
    private static ArrayList<String> _prayerTimes;
    private static CalculationMethod _calcMethod      = CalculationMethod.JAFARI;
    private static JuristicMethod    _juristicMethod  = JuristicMethod.SHAFII;
    private static AdjustingMethod   _adjustingMethod = AdjustingMethod.ANGLE_BASED;
    // { Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha }
    private static int[]             _offsets         = { 0, 0, 0, 0, 0, 0, 0 };
    private Calendar                 _cal;
    private static double            _timezone        = TimeZone.getDefault().getRawOffset()
                                                              / (1000L * 3600);
    private static double            _latitude        = WSConstants.LATITUDE_MAKKAH;  ;
    private static double            _longitude       = WSConstants.LONGITUDE_MAKKAH;
    private static State             _state           = State.NOT_CHANGED;
    private Object                   stateLock        = new Object();
    /**
     * Stop the thread if _stop is true.
     */
    private static boolean           _stop            = false;
    /**
     * Whether or not we check for a new day ... and then re-compute the prayer
     * times if we are in a new day (after midnight !).
     */
    private static boolean           _checkNewDay     = true;

    // =======================================================================

    public ComputePrayTimes() {
        this(_latitude, _longitude, _timezone);
    }

    // =======================================================================

    public ComputePrayTimes(double latitude, double longitude) {
        this(latitude, longitude, _timezone);
    }

    // =======================================================================

    public ComputePrayTimes(double latitude, double longitude, double timezone) {
        _prayers.setTimeFormat(_prayers.getTime24());
        _cal = Calendar.getInstance();
        _cal.setTime(_cal.getTime());
        _latitude = latitude;
        _longitude = longitude;
        _timezone = timezone;
        minimalComputing();
    }

    // =======================================================================

    @Override
    public void run() {
        while (true && !_stop) {
            if (_state.equals(State.CHANGED)) {
                logger.debug("Updating pray time computer ...");
                minimalComputing();
                synchronized (stateLock) {
                    _state = State.NOT_CHANGED;
                    setChanged();
                    notifyObservers(_prayerTimes);
                }
            } else {
                try {
                    if (_checkNewDay && isNewDay()) {
                        synchronized (stateLock) {
                            _cal = Calendar.getInstance();
                            _state = State.CHANGED;
                        }
                    }
                    Thread.sleep(10L); // Must be lower than the sleep in the
                                       // method updateState()
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // =======================================================================

    public void updateState() {
        synchronized (stateLock) {
            _state = State.CHANGED;
        }
        try {
            Thread.sleep(20L); // Must be higher than the sleep in the run()
                               // method !!!
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // =======================================================================

    /**
     * Do the minimal and general computing for prayer times.
     */
    private void minimalComputing() {
        _cal = getCalendar();
        _latitude = getLatitude();
        _longitude = getLongitude();
        _timezone = getTimezone();
        _offsets = getOffsets();
        _prayers.setCalcMethod(getCalcMethod());
        _prayers.setAsrJuristic(getJuristicMethod());
        _prayers.setAdjustHighLats(getAdjustingMethod());
        _prayers.tune(_offsets);
        _prayerTimes = _prayers.getPrayerTimes(_cal, _latitude, _longitude, _timezone);
    }

    // =======================================================================

    /**
     * @return Return <code>true</code> is current date of the system (calendar)
     *         is different from the current the date (calendar) used by the
     *         {@link ComputePrayTimes} thread.<br>
     *         Return <code>false</code> otherwise.
     */
    private boolean isNewDay() {
        int sysDateDAY = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return (sysDateDAY != _cal.get(Calendar.DAY_OF_YEAR))
                ? true : false;
    }

    // =======================================================================
    // GETTERS AND SETTERS ---------------------------------------------------

    public synchronized Calendar getCalendar() {
        return _cal;
    }

    public void setCalendar(Calendar cal) {
        _cal = cal;
        updateState();
    }

    public synchronized double getTimezone() {
        return _timezone;
    }

    public void setTimezone(double timezone) {
        _timezone = timezone;
        updateState();
    }

    public synchronized double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        _latitude = latitude;
        updateState();
    }

    public synchronized double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        _longitude = longitude;
        updateState();
    }

    public static synchronized int[] getOffsets() {
        return _offsets;
    }

    public void set_offsets(int[] offsets) {
        _offsets = offsets;
        updateState();
    }

    public synchronized int getCalcMethod() {
        switch (_calcMethod) {
            case JAFARI:
                return _prayers.getJafari();
            case EGYPT:
                return _prayers.getEgypt();
            case KARACHI:
                return _prayers.getKarachi();
            case MAKKAH:
                return _prayers.getMakkah();
            case TEHRAN:
                return _prayers.getTehran();
            case ISNA:
                return _prayers.getISNA();
            case MWL:
                return _prayers.getMWL();
            case CUSTOM:
                return _prayers.getCustom();
            default:
                return _prayers.getJafari(); // Default is JAFARI's method.
        }
    }

    public void setCalcMethod(CalculationMethod calcMethod) {
        _calcMethod = calcMethod;
        updateState();
    }

    public synchronized int getJuristicMethod() {
        switch (_juristicMethod) {
            case HANAFI:
                return _prayers.getHanafi();
            case SHAFII:
                return _prayers.getShafii();
            default:
                return _prayers.getShafii(); // Default method is Shafii.
        }
    }

    public void setJuristicMethod(JuristicMethod juristicMethod) {
        _juristicMethod = juristicMethod;
        updateState();
    }

    public synchronized int getAdjustingMethod() {
        switch (_adjustingMethod) {
            case ANGLE_BASED:
                return _prayers.getAngleBased();
            case MIDNIGHT:
                return _prayers.getMidNight();
            case ONE_SEVENTH:
                return _prayers.getOneSeventh();
            default:
                return _prayers.getAngleBased();
        }
    }

    public void setAdjustingMethod(AdjustingMethod adjustingMethod) {
        _adjustingMethod = adjustingMethod;
        updateState();
    }

    public static synchronized ArrayList<String> getPrayerTimes() {
        return _prayerTimes;
    }

    public synchronized void setPrayerTimes(ArrayList<String> prayerTimes) {
        _prayerTimes = prayerTimes;
        updateState();
    }

    public void stop() {
        _stop = true;
    }

    public synchronized boolean isCheckNewDay() {
        return _checkNewDay;
    }

    public synchronized void checkNewDay(boolean checkNewDay) {
        _checkNewDay = checkNewDay;
    }

    // =======================================================================

}
