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

package net.waqtsalat.utils;

import static net.waqtsalat.WaqtSalat.logger;

import java.io.BufferedInputStream;
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
import java.util.Observable;

import org.apache.commons.io.FileUtils;

/**
 * Contains some utilities such as download of a file from a given url (like
 * wget).<br>
 * Method to print some informations about response-header of an url.<br>
 * Method to check for redirections of an url ...
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class DownloadUtils extends Observable {

    private URL    _url;
    private URL    _redirect  = null;
    private Object _stateLock = new Object();

    private enum State {
        NOTHING_KNOWN,
        DOWNLOAD_IN_PROGRESS,
        DOWNLOAD_FINISHED,
        ERROR
    };

    private State     _state             = State.NOTHING_KNOWN;
    private Throwable _errorStateCause;
    private int       _bytesDownloaded   = 0;
    private int       _totalBytes        = 0;
    private boolean   _downloadCancelled = false;

    // =======================================================================

    public DownloadUtils() {
        super();
    }

    // =======================================================================

    public DownloadUtils(URL url) {
        _url = url;
    }

    // =======================================================================

    /**
     * Download a file from a given url.
     * 
     * @return The file that has been downloaded.
     * @throws IOException
     */
    public File download() throws IOException {

        try {
            String outputFileName = getFileNameFromURL();

            // Let's play with redirections.
            URL tempURL = new URL(_url.toString());
            DownloadUtils util = new DownloadUtils(tempURL);
            while (util.checkForRedirect()) {
                _url = util.getRedirectURL();
                util = new DownloadUtils(_url);
                outputFileName = util.getFileNameFromURL();
            }

            File outputFile = new File(outputFileName);
            return download(outputFile);
        } catch (IOException ioe) {
            setState(State.ERROR);
            throw new IOException();
        }
    }

    // =======================================================================

    public File download(String outputFileName) throws IOException {
        return download(new File(outputFileName));
    }

    // =======================================================================

    /**
     * Download a file from a given url.<br>
     * Default timeout for the connection is 30 seconds.<br>
     * Default timeout for reading the url is 1 hour.<br>
     * 
     * @param outputFile
     *            Where to save the downloaded file.
     * @return The file that has been downloaded.
     * @throws IOException
     */
    public File download(File outputFile) throws IOException {
        try {
            logger.info("Downloading file ...");
            setState(State.DOWNLOAD_IN_PROGRESS);

            File downloadedFile = File.createTempFile(outputFile.getName(), ".download", outputFile.getParentFile());
            URLConnection urlc = _url.openConnection();
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String osArch = System.getProperty("os.arch");
            String javaVersion = System.getProperty("java.version");
            String userAgent = "WaqtSalat/1.0 (" + osName + "; U; " + osArch
                    + "; " + osVersion + "; " + Locale.getDefault() + ") Java/"
                    + javaVersion;
            urlc.setRequestProperty("User-Agent", userAgent);
            String contentType = urlc.getContentType();
            String contentEncoding = urlc.getContentEncoding();
            _totalBytes = urlc.getContentLength();
            urlc.setConnectTimeout(30000);
            urlc.setReadTimeout(3600000);

            logger.debug("User-Agent               : {}", userAgent);
            logger.debug("Content type             : {}", contentType);
            logger.debug("Content length           : {}", _totalBytes);
            logger.debug("Content encoding         : {}", contentEncoding);
            logger.debug("Connection timeout (sec) : {}", urlc.getConnectTimeout() / 1000);
            logger.debug("Read timeout (sec)       : {}", urlc.getReadTimeout() / 1000);
            logger.debug("Temporary file           : {}", downloadedFile.getAbsolutePath());
            logger.debug("Remote file              : {}", _url.toString());

            BufferedInputStream bis = new BufferedInputStream(
                    urlc.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(downloadedFile));

            File outputFileTemp = File.createTempFile(outputFile.getName(),
                    ".temp", outputFile.getParentFile());
            outputFileTemp.delete();

            try {
                int BUFFER_SIZE = 4096;
                byte[] data = new byte[BUFFER_SIZE];
                int length;
                while ((length = bis.read(data)) != -1 && !_downloadCancelled) {
                    bos.write(data, 0, length);
                    _bytesDownloaded += length;
                    setChanged();
                    notifyObservers();
                }
                bos.flush();

                logger.debug("Bytes copied             : {}", _bytesDownloaded);
                if (_bytesDownloaded != _totalBytes) {
                    setState(State.ERROR);
                    logger.error("Data did not match, error occured during the download!");
                }

                if (outputFile.exists()) {
                    FileUtils.moveFile(outputFile, outputFileTemp);
                }

                FileUtils.moveFile(downloadedFile, outputFile);
                logger.info("Download finished successfully ! '{}'", outputFile.getAbsolutePath());
            } catch (IOException ioe) {
                setState(State.ERROR);
                if (!outputFile.exists() && outputFileTemp.exists()) {
                    FileUtils.moveFile(outputFileTemp, outputFile);
                }
                ioe.printStackTrace();
                throw new IOException();
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
                if (downloadedFile.exists())
                    downloadedFile.delete();
                if (outputFileTemp.exists())
                    outputFileTemp.delete();
                if (getState() != State.ERROR)
                    setState(State.DOWNLOAD_FINISHED);
            }

            return outputFile;
        } catch (IOException ioe) {
            setState(State.ERROR);
            ioe.printStackTrace();
            throw new IOException("The download failed.");
        }
    }

    // =======================================================================

    /**
     * Retrieves response-headers from an url.
     * 
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void getInfosHeader() throws IOException {
        try {
            logger.trace("===== GET URL HEADERS =====");

            URLConnection urlc = _url.openConnection();
            Iterator<?> headers = urlc.getHeaderFields().entrySet().iterator();

            while (headers.hasNext()) {
                Map.Entry entry = (Map.Entry) headers.next();
                logger.trace("{} ---> {}", entry.getKey(), entry.getValue());
            }

            logger.trace("=== FINISHED GETTING URL HEADERS ===");
        } catch (IOException ioe) {
            throw new IOException("Error while retreiving headers for url: "
                    + _url.toString());
        }
    }

    // =======================================================================

    /**
     * Check whether or not an URL has a redirection.
     * 
     * @return Return true if a redirection is found, false otherwise.
     * @throws IOException
     */
    private boolean checkForRedirect() throws IOException {

        try {
            URLConnection urlc = _url.openConnection();
            HttpURLConnection httpUrlCon = HttpURLConnection.class.cast(urlc);
            httpUrlCon.setInstanceFollowRedirects(false);

            // Let's check the response code.
            if (httpUrlCon.getResponseCode() != 301
                    && httpUrlCon.getResponseCode() != 302) {
                return false; // no redirection
            }

            _redirect = new URL(httpUrlCon.getHeaderField("Location"));
            logger.debug("Following '{}'", _redirect.toString());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Checking for redirections FAILED.");
        }
    }

    // =======================================================================

    /**
     * Get the url which is the redirection.
     * 
     * @return Return the url of the redirection.
     */
    private URL getRedirectURL() {
        return _redirect;
    }

    // =======================================================================

    /**
     * @return Return the filename from a given url after removing trailing and
     *         leading slashes.
     */
    public String getFileNameFromURL() {
        String outputFilename;
        outputFilename = _url.toString().replaceFirst("/*$", "");
        outputFilename = outputFilename.replaceFirst(".*/", "");
        return outputFilename;
    }

    // =======================================================================

    private synchronized void setState(State value) {
        synchronized (_stateLock) {
            _state = value;
            if (_state == State.DOWNLOAD_FINISHED) {
                _bytesDownloaded = _totalBytes;
            } else if (_state != State.DOWNLOAD_IN_PROGRESS) {
                _bytesDownloaded = 0;
                _totalBytes = 0;
            }
            if (_state != State.ERROR) {
                _errorStateCause = null;
            }
        }
        setChanged();
        notifyObservers();
    }

    // =======================================================================

    /**
     * @param url
     *            The url where to download the file from.
     */
    public void setUrl(URL url) {
        this._url = url;
    }

    public State getState() {
        synchronized (_stateLock) {
            return _state;
        }
    }

    public Throwable getErrorStateCause() {
        synchronized (_stateLock) {
            return _errorStateCause;
        }
    }

    public int getBytesDownloaded() {
        synchronized (_stateLock) {
            return _bytesDownloaded;
        }
    }

    public int getTotalBytes() {
        synchronized (_stateLock) {
            return _totalBytes;
        }
    }

    public void cancelDownload() {
        synchronized (_stateLock) {
            _downloadCancelled = true;
        }
    }

    public boolean isDownloadCancelled() {
        synchronized (_stateLock) {
            return _downloadCancelled;
        }
    }
    // =======================================================================

}
