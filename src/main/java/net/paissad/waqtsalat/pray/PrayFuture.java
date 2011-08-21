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

import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.pray.PrayTime.PrayName;
import net.paissad.waqtsalat.utils.api.PrayCalculator;
import net.paissad.waqtsalat.utils.api.PrayListener;
import net.paissad.waqtsalat.utils.media.AudioPlayerListener;
import net.paissad.waqtsalat.utils.media.SimplePlayer;

/**
 * @author Papa Issa DIAKHATE (paissad)
 */
public class PrayFuture implements PrayListener, Cloneable {

    private static Logger            logger = LoggerFactory.getLogger(PrayFuture.class);

    private PrayName                 name;
    private PraySettings             settings;
    private Calendar                 scheduledTime;
    private URL                      adhanResource;
    private boolean                  playAdhan;
    private ScheduledExecutorService scheduler;

    // _________________________________________________________________________

    public PrayFuture(
            final PrayName name,
            final PraySettings settings,
            final Calendar scheduledTime,
            final URL adhanResource,
            final boolean play) {

        this.name = name;
        this.settings = settings;
        this.scheduledTime = scheduledTime;
        this.adhanResource = adhanResource;
        this.playAdhan = play;
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        
        this.updateSettings();
    }

    // _________________________________________________________________________

    private void updateSettings() {
        Callable<PrayFuture> job = new Callable<PrayFuture>() {
            @Override
            public PrayFuture call() throws Exception {
                if (isPlayAdhan()) {
                    SimplePlayer player = new SimplePlayer(getAdhanResource());
                    player.play();
                }

                final Calendar nextScheduleTime = PrayUtils.getNextPrayScheduleTime(
                        getScheduledTime(), getName(), getSettings());

                return new PrayFuture(
                        getName(),
                        getSettings(),
                        nextScheduleTime,
                        getAdhanResource(),
                        isPlayAdhan());
            }
        };

        scheduler.schedule(job, getScheduledTime().getTimeInMillis(), TimeUnit.MILLISECONDS);
    }

    // _________________________________________________________________________

    @Override
    public void update(PrayCalculator o, PrayMessage message) {
        updateSettings();
    }

    // _________________________________________________________________________

    class MyAudioPlayerListener implements AudioPlayerListener {

        private String filename;

        public MyAudioPlayerListener(final String filename) {
            this.filename = filename;
        }

        @Override
        public void playerStarted() {
            logger.info("Start playing the audio file '{}'", this.filename);
        }

        @Override
        public void playerStopped() {
            logger.info("Finished playing the audio file '{}'", this.filename);
        }
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    public PrayName getName() {
        return name;
    }

    public void setName(PrayName name) {
        this.name = name;
    }

    public PraySettings getSettings() {
        return settings;
    }

    public void setSettings(PraySettings settings) {
        this.settings = settings;
    }

    /**
     * @return The current time for this pray object.
     */
    public Calendar getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Calendar scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public URL getAdhanResource() {
        return adhanResource;
    }

    public void setAdhanResource(URL adhanResource) {
        this.adhanResource = adhanResource;
    }

    public boolean isPlayAdhan() {
        return playAdhan;
    }

    public void setPlayAdhan(boolean playAdhan) {
        this.playAdhan = playAdhan;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
