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

import static net.waqtsalat.WaqtSalat.logger;
import static net.waqtsalat.utils.GeoipUtils.GEOIP_WORLDCITIES_FULL_PATH;

import java.io.File;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class DBDatas {

	private final String _dbName       = "worldcitiespop";
	private final String _tableName    = "WORLDCITIES";
	private final String _driver       = "org.h2.Driver";
	private final String _protocol     = "jdbc:h2:";
	private final String _dbSettings   = ";LARGE_TRANSACTIONS=true;OPTIMIZE_IN_SELECT=true;OPTIMIZE_OR=true";
	private final String _dbHomeKey    = "h2.baseDir";
	private final String _subprotocol  = "";

	public void loadDriver() {
		System.setProperty(_dbHomeKey,
				new File (GEOIP_WORLDCITIES_FULL_PATH).getParent());
		try {
			Class.forName(_driver).newInstance();
			logger.debug("Loading the appropriate JDBC driver.");
		} catch (InstantiationException ie) {
			logger.debug("Unable to instanciate the JDBC driver.");
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			logger.error("Unable to acces the JDBC driver.");
			iae.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.error("Unable to load the JDBC driver.");
			cnfe.printStackTrace();
		}
	}

	public String get_dbName() {
		return _dbName;
	}

	public String get_tableName() {
		return _tableName;
	}

	public String get_driver() {
		return _driver;
	}

	public String get_protocol() {
		return _protocol;
	}

	public String get_dbSettings() {
		return _dbSettings;
	}

	public String get_subprotocol() {
		return _subprotocol;
	}

	public String get_dbHomeKey() {
		return _dbHomeKey;
	}

	public String get_ConnectionURL() {
		return get_protocol()
		+ get_subprotocol()
		+ get_dbName()
		+ get_dbSettings();
	}

}
