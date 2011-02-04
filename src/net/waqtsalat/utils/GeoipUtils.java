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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.waqtsalat.WaqtSalat;
import net.waqtsalat.utils.Utils;
import net.waqtsalat.utils.DownloadUtils;
import net.waqtsalat.utils.UncompressUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some utilities such as looking for updates, or downloading GeoIP database.
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class GeoipUtils {

	public static final String GEOIP_DATABASE_SAVE_PATH      = "resources" + File.separator + "geoip";
	public static final String GEOIP_DATABASE_FILENAME       = "GeoLiteCity.dat";
	public static final String GEOIP_DATABASE_COMPLETE_PATH  = GEOIP_DATABASE_SAVE_PATH + File.separator + GEOIP_DATABASE_FILENAME;
	public static final String GEOIP_DATABASE_UPDATE_URL     = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz";

	/**
	 * Timestamp of the local GeoLiteCity.dat.gz file.
	 */
	private long localGeoipDatabaseTimestamp;

	/**
	 * Timestamp of the file GeoLiteCity.dat.gz from the remote url.
	 */
	private long remoteGeoipDatabaseTimestamp;  
	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	/**
	 * Sole constructor.
	 */
	public GeoipUtils() {
		try {
			File geoipLocalFile = new File(GEOIP_DATABASE_SAVE_PATH + File.separator + GEOIP_DATABASE_FILENAME);
			if(geoipLocalFile.exists())
				this.localGeoipDatabaseTimestamp = new Utils().getLocalFileTimestamp(geoipLocalFile);
			else {
				logger.info("GeoIP database not yet present in the system, then obviously, update is available!");
				this.localGeoipDatabaseTimestamp = 1;
			}
			try {
				this.remoteGeoipDatabaseTimestamp = new Utils().getRemoteFileTimestamp(new URL(GEOIP_DATABASE_UPDATE_URL));
			}
			catch(IOException ioe) {
				logger.error("Error while retreiving the remote timestamp for GeoIP database.");
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error("Error while creating an instance of {}", getClass().toString());
		}
	}
	//=======================================================================

	/**
	 * Update the GeoIP database if available.
	 */
	public void updateGeoipDatabase() {
		if(isUpdateGeoipAvailable())
			downloadGeoipDatabase();
	}
	//=======================================================================

	/**
	 * Download the GeoIP database file, uncompress it and update its timestamp.<br />
	 * By default, it is GeoLiteCity.dat
	 */
	public void downloadGeoipDatabase() {
		try {
			DownloadUtils util       = new DownloadUtils(new URL(GEOIP_DATABASE_UPDATE_URL));
			File database_Downloaded = util.downloadFile(
					new File(GEOIP_DATABASE_SAVE_PATH + File.separator + util.getFileNameFromURL()));

			File database_Uncompressed = new UncompressUtils(
					database_Downloaded).uncompressSmart();

			// Update the timestamp of the GeoIP database.
			database_Uncompressed.setLastModified(this.remoteGeoipDatabaseTimestamp);

			// Remove the default compressed file, since it is unused and does use space disk for nothing.
			if(database_Downloaded.exists())
				database_Downloaded.delete();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			logger.error("Download of geoip database FAILED.");
		}
	}
	//=======================================================================

	/**
	 * Check for available update for the GeoIP database.
	 * @return Return true if an update is available, false otherwise.
	 */
	public boolean isUpdateGeoipAvailable() {
		boolean update = new Utils().checkUpdate(
				this.localGeoipDatabaseTimestamp, this.remoteGeoipDatabaseTimestamp);
		if(update) 
			logger.info("An update is available for the GeoIP database.");
		else 
			logger.info("No update available yet for the GeoIP database.");
		return update;
	}
	//=======================================================================

}
