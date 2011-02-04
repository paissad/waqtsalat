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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.waqtsalat.WaqtSalat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Contains some utilities such as download of a file from a given url (like wget).<br />
 * Method to print some informations about response-header of an url. <br />
 * Method to check for redirections of an url ...
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class DownloadUtils {

	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	/**
	 * Url to use.
	 */
	private URL url;
	/**
	 * If the url has an redirection, 'redirect' will catch it !
	 */
	private URL redirect = null;

	//=======================================================================

	public DownloadUtils() {
		super();
	}
	//=======================================================================

	public DownloadUtils(URL url) {
		this.url = url;
	}
	//=======================================================================

	/**
	 * Download a file from a given url.
	 * 
	 * @return The file that has been downloaded.
	 * @throws IOException
	 */
	public File downloadFile() throws IOException {

		try {
			String outputFilename = getFileNameFromURL();

			// Let's play with redirections. 
			URL tempURL = new URL(url.toString());
			DownloadUtils util = new DownloadUtils(tempURL);
			while(util.checkForRedirect()) {
				url = util.getRedirectURL();
				util = new DownloadUtils(url);
				outputFilename = util.getFileNameFromURL();
			}

			File outputFile = new File(outputFilename);
			return downloadFile(outputFile);
		}
		catch (IOException ioe) {
			throw new IOException();
		}
	}
	//=======================================================================

	/**
	 * Download a file from a given url.<br />
	 * Default timeout for the connection is 30 seconds.<br />
	 * Default timeout for reading the url is 1 hour.<br />
	 * 
	 * @param outputFile Where to save the file.
	 * @return The file that has been downloaded.
	 * @throws IOException
	 */
	public File downloadFile(File outputFile) throws IOException {

		try {	
			Logger logger          = LoggerFactory.getLogger(getClass());
			logger.info("Downloading file ...");

			File downloadedFile    = File.createTempFile(outputFile.getName(), ".download", outputFile.getParentFile());
			URLConnection urlc     = url.openConnection();
			String osName          = System.getProperty("os.name");
			String osVersion       = System.getProperty("os.version");
			String osArch          = System.getProperty("os.arch");
			String javaVersion     = System.getProperty("java.version");
			String userAgent       = "WaqtSalat/1.0 (" + osName + "; U; " + osArch + "; " + osVersion + "; " + Locale.getDefault() +") Java/" + javaVersion;
			urlc.setRequestProperty("User-Agent", userAgent);
			String contentType     = urlc.getContentType();
			String contentEncoding = urlc.getContentEncoding();
			int contentLength      = urlc.getContentLength();
			urlc.setConnectTimeout(30000);
			urlc.setReadTimeout(3600000);

			logger.debug("User-Agent               : {}", userAgent);
			logger.debug("Content type             : {}", contentType);
			logger.debug("Content length           : {}", contentLength);
			logger.debug("Content encoding         : {}", contentEncoding);
			logger.debug("Connection timeout (sec) : {}", urlc.getConnectTimeout()/1000);
			logger.debug("Read timeout (sec)       : {}", urlc.getReadTimeout()/1000);
			logger.debug("Temporary file           : {}", downloadedFile.getAbsolutePath());
			logger.debug("Remote file              : {}", url.toString());

			BufferedOutputStream bos = new BufferedOutputStream (new FileOutputStream(downloadedFile));

			File outputFileTemp = File.createTempFile(outputFile.getName(), ".temp", outputFile.getParentFile());
			outputFileTemp.delete();

			try {
				int copied = IOUtils.copy(urlc.getInputStream(), bos);
				logger.debug("Bytes copied             : {}", copied);
				if(copied != contentLength) {
					logger.error("Data did not match, error occured during the download!");
				}

				bos.write(2048);
				bos.close();

				if (outputFile.exists()) {
					FileUtils.moveFile(outputFile, outputFileTemp);
				}

				FileUtils.moveFile(downloadedFile, outputFile);
				logger.info("Download finished successfully ! '{}'",outputFile.getAbsolutePath());
			}
			catch(IOException ioe) {
				if (!outputFile.exists() && outputFileTemp.exists()) {
					FileUtils.moveFile(outputFileTemp, outputFile);
				}
				if (bos != null)
					bos.close();
				ioe.printStackTrace();
				throw new IOException();
			}
			finally {
				if (bos != null)
					bos.close();
				if (downloadedFile.exists())
					downloadedFile.delete();
				if (outputFileTemp.exists())
					outputFileTemp.delete();
			}
			return outputFile;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			throw new IOException("The download failed.");
		}
	}
	//=======================================================================

	/**
	 * Retreives response-headers from an url.
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public void getInfosHeader() throws IOException {
		try {
			logger.trace("===== GET URL HEADERS =====");

			URLConnection urlc = url.openConnection();
			Iterator<?> headers       = urlc.getHeaderFields().entrySet().iterator();

			while(headers.hasNext()) {
				Map.Entry entry = (Map.Entry) headers.next();
				logger.trace("{} ---> {}", entry.getKey(), entry.getValue());
			}

			logger.trace("=== FINISHED GETTING URL HEADERS ===");
		}
		catch (IOException ioe) {
			throw new IOException("Error while retreiving headers for url: " + url.toString());
		}
	}
	//=======================================================================

	/**
	 * Check whether or not an URL has a redirection.
	 * @return Return true if a redirection is found, false otherwise.
	 * @throws IOException
	 */
	public boolean checkForRedirect() throws IOException {

		try {
			URLConnection urlc           = url.openConnection();
			HttpURLConnection httpUrlCon = HttpURLConnection.class.cast(urlc);
			httpUrlCon.setInstanceFollowRedirects(false);

			// Let's check the response code.
			if(httpUrlCon.getResponseCode() != 301 && httpUrlCon.getResponseCode() != 302) {
				return false; // no redirection
			}

			redirect = new URL(httpUrlCon.getHeaderField("Location"));
			logger.debug("Following '{}'", redirect.toString());
			return true;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			throw new IOException("Checking for redirections FAILED.");
		}
	}
	//=======================================================================

	/**
	 * Get the url which is the redirection.
	 * @return Return the url of the redirection.
	 */
	public URL getRedirectURL() {
		return redirect;
	}
	//=======================================================================

	/**
	 * Get the url which is the redirection.
	 * @return Return the url of the redirection in a String format.
	 */
	public String getRedirectString() {
		return redirect.toString();
	}
	//=======================================================================

	/**
	 * @return Return the filename from a given url.
	 */
	public String getFileNameFromURL() {
		String outputFilename;
		outputFilename = url.toString().replaceFirst("/*$", ""); // remove trailing slashes
		outputFilename = outputFilename.replaceFirst(".*/", ""); // get the filename
		return outputFilename;
	}

}
