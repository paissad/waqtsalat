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

package net.paissad.waqtsalat.utils.api;

import net.paissad.waqtsalat.pray.PrayMessage;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface PrayCalculator {

    /**
     * Adds a listener.
     * 
     * @param o
     *            - The listener to add.
     */
    public void addListener(PrayListener o);

    /**
     * Deletes a listener from the registered listeners.
     * 
     * @param o
     *            - The listener to delete.
     */
    public void deleteListener(PrayListener o);

    /**
     * Deletes all registered listeners.
     */
    public void deleteAllListeners();

    /**
     * Notifies a listener by calling its 'update' method.<br>
     * The notification will be sent if and only if the hasChanged() method
     * returns <tt>true</tt>.
     * 
     * @param o
     *            - The listener to notify/update.
     * @param message
     *            - The <tt>PrayMessage</tt> to send to the listener.
     * @see PrayListener#update(PrayCalculator, PrayMessage)
     */
    public void notifyListener(PrayListener o, PrayMessage message);

    /**
     * Notifies all registered listeners by calling their 'update' method.<br>
     * The notifications will be sent if and only if the hasChanged() method
     * returns <tt>true</tt>.
     * 
     * @param message
     *            - The <tt>PrayMessage</tt> message to send to the listener.
     */
    public void notifyAllListeners(PrayMessage message);

    /**
     * @return The number of registered listeners.
     */
    public int countListeners();

    /**
     * @return <tt>true</tt> if this <tt>PrayCalculator</tt> object has changed,
     *         <tt>false</tt> otherwise.
     */
    public boolean hasChanged();

    /**
     * Marks this <tt>PrayCalculator</tt> object as having been changed; the
     * hasChanged method will now return <code>true</code>.
     */
    public void setChanged();

    /**
     * Indicates that this object has no longer changed, or that it has already
     * notified all of its observers of its most recent change, so that the
     * hasChanged() method will now return false
     */
    public void clearChanged();

}
