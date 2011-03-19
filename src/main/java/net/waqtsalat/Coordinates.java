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

/**
 * A class which represent geographical coordinates ...
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Coordinates {

	float _latitude;
	float _longitude;

	public Coordinates() {
	}

	public Coordinates(float latitude, float longitude) {
		this._latitude = latitude;
		this._longitude = longitude;
	}

	public float getLatitude() {
		return _latitude;
	}

	public void setLatitude(float latitude) {
		this._latitude = latitude;
	}

	public float getLongitude() {
		return _longitude;
	}

	public void setLongitude(float longitude) {
		this._longitude = longitude;
	}
	
	@Override
	public String toString() {
		String format = "%-10s : %f\n", s = "";
		s += String.format(format, "latitude", getLatitude());
		s += String.format(format, "longitude", getLongitude());
		return s;
	}
}
