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

package net.waqtsalat;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates a pray with all its "options" such like its name, its time, its
 * muezzin call sound and so on ...
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Pray {

	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	private static final String FS = File.separator;

	private int rank; // For example, Fadjr may be considered having rank 1,
						// Isha -> rank 5.
	private String prayName;
	private String time;
	private boolean playMuezzin;
	private Scheduler scheduler;
	private String muezzinSound;
	private String cronDate;
	private Thread t;

	// =======================================================================

	public Pray() {
		scheduler = new Scheduler();
		this.scheduler.setDaemon(true);
		this.playMuezzin = false;
		this.muezzinSound = "resources" + FS + "sounds" + FS + "adhan.mp3";
	}

	// =======================================================================

	public Pray(int rank) {
		this();
		this.rank = rank;
	}

	// =======================================================================

	public Pray(String prayName) {
		this();
		this.prayName = prayName;
	}

	// =======================================================================

	public Pray(int rank, String prayName) {
		this();
		this.rank = rank;
		this.prayName = prayName;
	}

	// =======================================================================

	/**
	 * Start the daemon scheduler.
	 */
	public void start() {
		t = new Thread("Pray Daemon for " + prayName) {
			String schedulerID = new String();

			public void run() {
				logger.trace("Starting {}.", Thread.currentThread().getName());
				playMuezzin = true;

				try {
					cronDate = cronDateFromTime(time);
				} catch (BadTimeFormatForCronJob e) {
					logger.warn("Error while creating the Pray instance. Bad time format during computation !");
					e.printStackTrace();
				}

				logger.trace("Starting scheduler {}. ({})", Thread
						.currentThread().getName(), prayName
						+ ": cron date -> " + cronDate);
				try {
					schedulerID = scheduler.schedule(cronDate, new Runnable() {
						@Override
						public void run() {
							new SimplePlayer(muezzinSound).play();
						}
					});
					scheduler.start();
				} catch (Exception e) {
					logger.error(
							"Error while creating/starting the scheduler for pray daemon ! ({})",
							prayName);
					e.printStackTrace();
					if (scheduler.isStarted())
						scheduler.stop();
					playMuezzin = false;
					return;
				}

				while (scheduler.isStarted() && playMuezzin) {
					try {
						Thread.sleep(5000L);
						update(schedulerID, time);
					} catch (InterruptedException ie) {
					} catch (BadTimeFormatForCronJob e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};
		t.start();
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
	private synchronized void update(String schedulerID, String prayTime)
			throws BadTimeFormatForCronJob {
		if (scheduler.isStarted() && t.isAlive()) {
			if (!time.equals(prayTime)) {
				setTime(prayTime);
				cronDate = cronDateFromTime(time);
				scheduler.reschedule(schedulerID, cronDate);
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
					prayName);
			scheduler.stop();
			playMuezzin = false;
		} else {
			logger.error("No muezzin call daemon to stop ({}).", prayName);
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
				logger.error(
						"Impossible to compute cronJobTime, abnormal time format: {}.",
						time);
				throw new BadTimeFormatForCronJob(
						"Error while computing cron job date.");
			}
			cronDate = minHour[1] + " " + minHour[0] + " * * *";
			return cronDate;
		} else {
			throw new BadTimeFormatForCronJob(
					"The time to compute into a cron date cannot be null or empty !!!");
		}
	}

	// =======================================================================

	public String toString() {
		String s = "";
		s += String.format("\n+----------------------------------------------");
		s += String.format("\n| %-14s : %-40s", "rank", rank);
		s += String.format("\n| %-14s : %-40s", "prayname", prayName);
		s += String.format("\n| %-14s : %-40s", "time", time);
		s += String.format("\n| %-14s : %-40s", "cron date", cronDate);
		s += String.format("\n| %-14s : %-40s", "muezzin call", muezzinSound);
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

	public String getPrayName() {
		return prayName;
	}

	public void setPrayName(String prayName) {
		this.prayName = prayName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String prayTime) {
		time = prayTime;
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
