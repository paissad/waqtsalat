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

import java.util.TimeZone;

import net.paissad.waqtsalat.pray.PrayTime.AdjustingMethod;
import net.paissad.waqtsalat.pray.PrayTime.CalcMethod;
import net.paissad.waqtsalat.pray.PrayTime.JuristicMethod;
import net.paissad.waqtsalat.pray.PrayTime.TimeFormat;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class PraySettings implements Cloneable {

    private float           latitude;
    private float           longitude;
    private TimeZone        timeZone;
    private int[]           offsets;

    private TimeFormat      timeFormat;
    private CalcMethod      calcMethod;
    private JuristicMethod  asrJuristic;
    private AdjustingMethod adjustingMethod;

    public PraySettings() {
    }

    public PraySettings(
            final float latitude,
            final float longitude,
            final TimeZone tZone,
            final int[] offsets,
            final TimeFormat timeFormat,
            final CalcMethod calcMethod,
            final JuristicMethod asrJuristic,
            final AdjustingMethod adjustMethod) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = tZone;
        this.timeFormat = timeFormat;
        this.calcMethod = calcMethod;
        this.offsets = offsets;
        this.asrJuristic = asrJuristic;
        this.adjustingMethod = adjustMethod;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public CalcMethod getCalcMethod() {
        return calcMethod;
    }

    public void setCalcMethod(CalcMethod calcMethod) {
        this.calcMethod = calcMethod;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public JuristicMethod getAsrJuristic() {
        return asrJuristic;
    }

    public void setAsrJuristic(JuristicMethod asrJuristic) {
        this.asrJuristic = asrJuristic;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(TimeFormat timeformat) {
        this.timeFormat = timeformat;
    }

    public AdjustingMethod getAdjustingMethod() {
        return adjustingMethod;
    }

    public void setAdjustingMethod(AdjustingMethod adjustingMethod) {
        this.adjustingMethod = adjustingMethod;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
