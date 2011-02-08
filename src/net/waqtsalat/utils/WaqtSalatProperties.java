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

package net.waqtsalat.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WaqtSalatProperties {
	private final Properties properties = new Properties();

	private static final String ENCODING = "UTF-8";

	public void loadFromByteArray(byte[] data) throws IOException {
		try {
			String utf = new String(data, ENCODING);
			StringReader reader = new StringReader(utf);
			properties.clear();
			properties.load(reader);
			reader.close();
		} catch (UnsupportedEncodingException e) {
			throw new IOException("Could not decode " + ENCODING);
		}		
	}
	//=======================================================================

	public void clear() {
		properties.clear();
	}
	//=======================================================================

	public String get(String key) {
		Object obj = properties.get(key);
		if (obj != null) {
			return trimAndRemoveQuotes("" + obj);
		} else {
			return "";
		}
	}
	//=======================================================================

	private static String trimAndRemoveQuotes(String in) {
		in = in.trim();
		if (in.startsWith("\"")) {
			in = in.substring(1);
		}
		if (in.endsWith("\"")) {
			in = in.substring(0, in.length() - 1);
		}
		return in;
	}
	//=======================================================================

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}
	//=======================================================================

}
