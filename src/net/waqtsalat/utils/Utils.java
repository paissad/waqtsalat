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
import java.net.URLConnection;
import java.sql.Date;

import net.waqtsalat.WaqtSalat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Utils {

	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	/**
	 * Get the timestamp of a remote file from a given url.
	 * @param url ({@link URL}) Url to use for the check.
	 * @return Return the timestamp of the remote file.
	 * @throws IOException
	 */
	public long getRemoteFileTimestamp(URL url) throws IOException {
		try {
			logger.trace("Trying to get the timestamp of remote file '{}'", url.toString());
			URLConnection urlc = url.openConnection();
			long timestamp = urlc.getLastModified();
			logger.trace("Remote timestamp is: {}", new Date(timestamp));
			return timestamp;
		}
		catch(IOException ioe) {
			throw new IOException("Error while retreiving the timestamp of the remote file!");
		}
	}
	//=======================================================================

	/**
	 * Get the local timestamp of a local file.
	 * @param file File to check the the last modification time (timestamp).
	 * @throws IOException
	 * @return Return the timestamp of the last modification time.
	 */
	public long getLocalFileTimestamp(File file) throws IOException { 
		try {
			logger.trace("Trying to get the timestamp of file '{}'.", file.getAbsolutePath());
			assertFileIsValid(file);
			long timestamp = file.lastModified();
			logger.trace("Local timestamp is: {}", new Date(timestamp));
			return timestamp;
		}
		catch(IOException ioe) {
			throw new IOException("Error while retreiving the timestamp of '" + file.getAbsolutePath() + "' !");
		}
	}
	//=======================================================================

	/**
	 * Check whether or not a file is valid and readable.
	 * @param file File to check whether it is valid or not.
	 * @throws IOException
	 */
	private void assertFileIsValid(File file) throws IOException {
		IOException ioe = new IOException();
		if (!file.isFile()) {
			ioe.printStackTrace();
			throw new IOException("'" + file + "' must be a file.");
		}

		if (!file.canRead()) {
			ioe.printStackTrace();
			throw new IOException("'" + file + "' is not readable."); 
		}
	}
	//=======================================================================

	/**
	 * Check whether or not an update is available for a file by comparing the timestamps of the files. 
	 * @param localTimestamp Local timestamp
	 * @param remoteTimestamp Remote timestamp (candidate for the update)
	 * @return Return <code>true</code> if remoteTimestamp > localTimestamp, <code>false</code> otherwise.
	 */
	public boolean checkUpdate(long localTimestamp, long remoteTimestamp) {
		return (localTimestamp < remoteTimestamp) ? true : false;
	}
	//=======================================================================

	/**
	 * Compare 2 versions lexicographically.
	 * @param localVersion Local version of the software.
	 * @param remoteVersion Remote version of the software.
	 * @return Return <code>true</code> if a remote version is newer, <code>false</code> otherwise. 
	 */
	public boolean checkUpdate(String localVersion, String remoteVersion) {
		return (localVersion.compareTo(remoteVersion) < 0) ? true : false;
	}
	//=======================================================================

}
