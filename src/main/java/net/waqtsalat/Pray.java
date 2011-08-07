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

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.waqtsalat.utils.media.SimplePlayer;

/**
 * Encapsulates a pray with all its "options" such like its name, its time, its
 * muezzin call sound and so on ...
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class Pray extends Observable implements Observer {
    
    private static Logger logger = LoggerFactory.getLogger(Pray.class);
    private static final String FS                 = File.separator;

    /**
     * For example, Fadjr may be considered having rank 1, Isha -> rank 5.<br>
     * For some reasons, we will consider that sunrise & sunset are respectively
     * ranked to 6 and 7.
     */
    private int                 rank;
    private PrayName            prayName;
    private String              _currentTimeOfPray = "";
    private boolean             playMuezzin;
    private Scheduler           scheduler;
    private String              muezzinSound;
    private String              cronDate;
    private Thread              t;
    private String              _schedulerID       = new String();

    // =======================================================================

    {
        addObserver(this);
        scheduler = new Scheduler();
        this.scheduler.setDaemon(false);
        this.playMuezzin = false;
        this.muezzinSound = "extras" + FS + "sounds" + FS + "adhan.mp3";
    }

    // =======================================================================

    public Pray() {
    }

    // =======================================================================

    public Pray(int rank) {
        this();
        this.rank = rank;
    }

    // =======================================================================

    public Pray(PrayName prayName) {
        this();
        this.prayName = prayName;
    }

    // =======================================================================

    public Pray(int rank, PrayName prayName) {
        this(rank);
        this.prayName = prayName;
    }

    // =======================================================================

    /**
     * Start the daemon scheduler.
     */
    public void start() {
        t = new Thread("Pray Daemon for " + prayName.toString()) {
            public void run() {
                logger.trace("Starting {}...", Thread.currentThread().getName());
                playMuezzin = true;

                try {
                    cronDate = cronDateFromTime(_currentTimeOfPray);
                } catch (BadTimeFormatForCronJob e) {
                    logger.warn("Error while creating the Pray instance. Bad time format during computation ! ({})", prayName.toString());
                    e.printStackTrace();
                }

                logger.trace("Starting scheduler {} ({}).",
                        Thread.currentThread().getName(), prayName.toString()
                                + ": cron date -> " + cronDate);
                try {
                    _schedulerID = scheduler.schedule(cronDate, new Runnable() {
                        @Override
                        public void run() {
                            new SimplePlayer(muezzinSound).play();
                        }
                    });
                    scheduler.start();
                } catch (Exception e) {
                    logger.error("Error while creating/starting the scheduler for pray daemon ! ({})", prayName.toString());
                    e.printStackTrace();
                    if (scheduler.isStarted())
                        scheduler.stop();
                    playMuezzin = false;
                    return;
                }

                while (scheduler.isStarted() && playMuezzin) {
                    try {
                        Thread.sleep(5 * 1000L);
                        updateTimeOfPray(_currentTimeOfPray);
                    }
                    catch (InterruptedException ie) {
                    }
                    catch (BadTimeFormatForCronJob e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };
        t.start();
    }

    // =======================================================================

    @Override
    public void update(Observable arg0, Object timeOfPray) {
        logger.debug("updating time for '{}'", prayName.toString());
        _currentTimeOfPray = (String) timeOfPray;
    }

    // =======================================================================

    /**
     * Update the daemon scheduler (Change the cron date if the scheduler is
     * started).
     * 
     * @param prayTime
     *            The new time to use for this Pray object.
     * @param schedulerID
     * @throws BadTimeFormatForCronJob
     */
    private synchronized void updateTimeOfPray(String prayTime)
            throws BadTimeFormatForCronJob {
        if (scheduler.isStarted() && t.isAlive()) {
            if (!_currentTimeOfPray.equals(prayTime)) {
                setTime(prayTime);
                cronDate = cronDateFromTime(_currentTimeOfPray);
                scheduler.reschedule(_schedulerID, cronDate);
                setChanged();
                notifyObservers();
            }
        }
    }

    // =======================================================================

    /**
     * Stop the scheduler.
     */
    public void stop() {
        if (scheduler.isStarted()) {
            logger.trace("Stopping scheduler for muezzin call daemon ({}).",
                    prayName.toString());
            scheduler.stop();
            playMuezzin = false;
        } else {
            logger.error("No muezzin call daemon to stop ({}).", prayName.toString());
        }
    }

    // =======================================================================

    /**
     * Computes the string pattern for the scheduler.<br>
     * Creates a cron job date from a specified time like this for example
     * "16:54".<br>
     * Creates something like this "54 16 * * *"
     * 
     * @param time
     *            The time to compute in a cron job date String format.
     * @return Return a {@link String} which represents the cron job date.
     * @throws BadTimeFormatForCronJob
     */
    public synchronized String cronDateFromTime(String time)
            throws BadTimeFormatForCronJob {
        if (time != null && !time.isEmpty()) {
            String[] minHour = time.split(":");
            if (minHour.length < 2) {
                logger.error("Impossible to compute cronJobTime, abnormal time format: {}.", time);
                throw new BadTimeFormatForCronJob("Error while computing cron job date.");
            }
            cronDate = minHour[1] + " " + minHour[0] + " * * *";
            return cronDate;
        } else {
            throw new BadTimeFormatForCronJob("The time to compute into a cron date cannot be null or empty !!!");
        }
    }

    // =======================================================================

    public String toString() {
        String format = "\n| %-14s : %-40s";
        String s = "";
        s += String.format("\n+----------------------------------------------");
        s += String.format(format, "rank", rank);
        s += String.format(format, "prayname", prayName.toString());
        s += String.format(format, "time", _currentTimeOfPray);
        // s += String.format(format, "cron date", cronDate);
        s += String.format(format, "muezzin call", muezzinSound);
        s += String.format("\n+----------------------------------------------");
        return s;
    }

    // =======================================================================

    /**
     * An exception that will be thrown when try to create a cron job from a bad
     * time format.<br>
     * The handled format is "13:45" for example.
     */
    class BadTimeFormatForCronJob extends Exception {
        private static final long serialVersionUID = 1L;

        private BadTimeFormatForCronJob() {
            super();
            System.err.println("Bad time format for the cron job to compute !");
        }

        private BadTimeFormatForCronJob(String arg) {
            super(arg);
        }
    }

    // =======================================================================

    // SETTERS AND GETTERS --------------------------------------------------

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public PrayName getPrayName() {
        return prayName;
    }

    public void setPrayName(PrayName prayName) {
        this.prayName = prayName;
    }

    public String getTime() {
        return _currentTimeOfPray;
    }

    /**
     * @param prayTime
     */
    public synchronized void setTime(String prayTime) {

        if (!_currentTimeOfPray.equals(prayTime)) {
            _currentTimeOfPray = prayTime;
            // cronDateFromTime(_currentTimeOfPray) // TODO ...
            setChanged();
            notifyObservers(_currentTimeOfPray);
        }
    }

    public boolean getPlayMuezzin() {
        return playMuezzin;
    }

    public void setPrayRun(boolean playMuezzin) {
        this.playMuezzin = playMuezzin;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Set the muezzin call.
     * 
     * @param muezzinSound
     *            Muezzin call to use/play.
     */
    public void setMuezzinSound(String muezzinSound) {
        this.muezzinSound = muezzinSound;
    }

    /**
     * Get the muezzin call which will be played.
     * 
     * @return Return the muezzin call used by this object.
     */
    public String getMuezzinSound() {
        return muezzinSound;
    }

}
