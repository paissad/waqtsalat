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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import static net.waqtsalat.WaqtSalat.logger;

/**
 * A thread that plays the muezzin call at each pray time.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class MuezzinCallDaemon implements Observer {

    // {Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha}
    ArrayList<PrayName>              _prayNames = new ArrayList<PrayName>();

    // Only the 5 hours of prayers.
    private static Set<Pray>         _prays;
    private static ArrayList<String> _currentTimes;
    private boolean                  _runAll;

    // =======================================================================

    /**
     * Default and sole constructor.
     * 
     * @param prayTimes
     *            An {@link ArrayList} containing the times of pray. (in the
     *            correct order).
     * @throws BadSizePrayTimesArray
     *             If the array specified as argument for the constructor is not
     *             equal to 5 or 7.
     */
    public MuezzinCallDaemon(ArrayList<String> prayTimes)
            throws BadSizePrayTimesArray {

        _currentTimes = prayTimes;
        normalizeArrayOfPrays(_currentTimes);

        Comparator<Pray> comparator = new PrayComparator();
        _prays = new TreeSet<Pray>(comparator);
        for (int i = 0; i < _prayNames.size(); i++) {
            Pray pray = new Pray(i + 1, _prayNames.get(i));
            pray.setTime(_currentTimes.get(i));
            _prays.add(pray);
        }

        _runAll = true;
    }

    // =======================================================================

    /**
     * Start the daemon. (The muezzin call for each pray time is scheduled for
     * playing).
     */
    public void start() {
        Thread t = new Thread("Muezzin Call Daemon") {
            @Override
            public void run() {
                _runAll = true;
                Iterator<Pray> iter = _prays.iterator();
                while (iter.hasNext()) {
                    iter.next().start(); // Start one pray daemon.
                }

                // update the pray times !
                while (isSchedulerActive(_prays)) {
                    try {
                        Thread.sleep(1L * 60L * 1000L); // Update every pray
                                                        // time every minute.
                        updatePrayTimes();
                        logger.trace("{}\n==================================================", _prays.toString());
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.setDaemon(false);
        t.start();
    }

    // =======================================================================

    /**
     * Stop the daemon. (The muezzin call of each pray time is stopped).
     */
    public void stop() {
        _runAll = false;
        Iterator<Pray> iter = _prays.iterator();
        if (_runAll) {
            while (iter.hasNext()) {
                iter.next().stop();
            }
        }
    }

    // =======================================================================

    @Override
    public synchronized void update(Observable o, Object prayerTimes) {
        setTimes(ComputePrayTimes.getPrayerTimes());
        updatePrayTimes();
    }

    // =======================================================================

    /**
     * Update the pray times of the "Pray Daemons' of the Muezzin Call Daemon.
     */
    private synchronized void updatePrayTimes() {
        Iterator<Pray> iter = _prays.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Pray pray = iter.next();
            pray.setTime(_currentTimes.get(i));
            i++;
        }
    }

    // =======================================================================

    /**
     * Check whether or not a pray instance has its daemon scheduler active or
     * not.
     * 
     * @param prays
     *            The <code>set</code> of {@link Pray}.
     * @return Return true if there is at least one pray object who has its
     *         daemon scheduler active.
     */
    public synchronized boolean isSchedulerActive(Set<Pray> prays) {
        Iterator<Pray> iter = prays.iterator();
        while (iter.hasNext()) {
            if (iter.next().getScheduler().isStarted())
                return true;
        }
        return false;
    }

    // =======================================================================

    public String toString() {
        Iterator<Pray> it = _prays.iterator();
        String s = new String();
        while (it.hasNext()) {
            s += it.next().toString();
        }
        return s;
    }

    // =======================================================================

    private void normalizeArrayOfPrays(ArrayList<String> times)
            throws BadSizePrayTimesArray {
        int size = times.size();
        if (size == 7) {
            times.remove(1); // Remove Sunrise time.
            times.remove(4 - 1); // Remove Sunset time.
        } else if (size != 5) {
            logger.warn(
                    "Expected 5 or 7 as array size for the list of time, but got {} instead !",
                    size);
            throw new BadSizePrayTimesArray();
        }

        // At this step, the list 'times' has a size of 5.
        if (_prayNames.size() == 7) {
            _prayNames.remove(1); // Remove Sunrise name!
            _prayNames.remove(4 - 1); // Remove Sunset name!
        } else if (_prayNames.size() != 5) {
            throw new BadSizePrayTimesArray();
        }
    }

    // =======================================================================

    /**
     * An exception that will be thrown when the array of an array of pray times
     * is abnormal (not equal to 5).
     */
    class BadSizePrayTimesArray extends Exception {
        private static final long serialVersionUID = 1L;

        private BadSizePrayTimesArray() {
            super();
            System.err.println(
                    "The size of the array of pray times must be equal to 5.");
        }

        private BadSizePrayTimesArray(String arg) {
            super(arg);
        }
    }

    // =======================================================================

    // SETTERS AND GETTERS --------------------------------------------------

    public void setRunAll(boolean runAll) {
        _runAll = runAll;
    }

    public boolean isRunAll() {
        return _runAll;
    }

    /**
     * After setting the pray times using this method, all {@link Pray} objects
     * will be notified of the change.<br>
     * Indeed, the <code>Pray</code> objects implement the {@link Observer}
     * interface.
     * 
     * @param times
     *            The pray times to set.
     */
    public void setTimes(ArrayList<String> times) {
        if (!_currentTimes.equals(times)) {
            _currentTimes = times;
        }
    }
    // =======================================================================

}
