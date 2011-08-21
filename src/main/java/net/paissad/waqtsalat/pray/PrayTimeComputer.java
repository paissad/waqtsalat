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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.utils.api.PrayCalculator;
import net.paissad.waqtsalat.utils.api.PrayListener;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class PrayTimeComputer implements PrayCalculator {

    private static Logger      logger = LoggerFactory.getLogger(PrayTimeComputer.class);

    private List<PrayListener> listeners;
    public boolean             changed;

    public PrayTimeComputer() {
        this.listeners = Collections.synchronizedList(new ArrayList<PrayListener>());
        this.changed = false;
    }

    // _________________________________________________________________________

    @Override
    public void addListener(PrayListener o) {
        this.listeners.add(o);
    }

    @Override
    public void deleteListener(PrayListener o) {
        this.listeners.remove(o);
    }

    @Override
    public void deleteAllListeners() {
        synchronized (listeners) {
            for (PrayListener aListener : listeners) {
                this.listeners.remove(aListener);
            }
        }
    }

    @Override
    public void notifyListener(PrayListener o, PrayMessage message) {
        if (hasChanged()) {
            try {
                o.update(this, message);
            } catch (Exception e) {
                logger.error("Error while notifying/updating the pray listener : {}", o, e);
                e.printStackTrace(System.err);
            }
            clearChanged();
        }
    }

    @Override
    public void notifyAllListeners(PrayMessage message) {
        if (hasChanged()) {
            for (PrayListener aListener : listeners) {
                try {
                    aListener.update(this, message);
                } catch (Exception e) {
                    logger.error("Error while notifying/updating the pray listener : {}", aListener, e);
                    e.printStackTrace(System.err);
                }
            }
            clearChanged();
        }
    }

    @Override
    public int countListeners() {
        return this.listeners.size();
    }

    @Override
    public boolean hasChanged() {
        return this.changed;
    }

    @Override
    public void setChanged() {
        this.changed = true;
    }

    @Override
    public void clearChanged() {
        this.changed = false;
    }

}
