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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.waqtsalat.WSConstants;

/**
 * @author Papa Issa DIAKHATE (paissad)
 */
public class DownloadHelper extends Observable {

    private static Logger    logger      = LoggerFactory.getLogger(DownloadHelper.class);

    private static final int BUFFER_SIZE = 4096;

    private DownloadState    state;
    private int              totalBytes;
    private int              bytesDownloaded;
    private boolean          isCancelled;

    // _________________________________________________________________________
    // Constructors ...

    public DownloadHelper() {
        this.totalBytes = 0;
        this.bytesDownloaded = 0;
        this.state = DownloadState.UNKNOWN;
        this.isCancelled = false;
    }

    // _________________________________________________________________________

    public enum DownloadState {
        IN_PROGRESS,
        CANCELED,
        FINISHED,
        ERROR,
        UNKNOWN,
    };

    // _________________________________________________________________________

    /**
     * <p>
     * Download a resource from the specified url and save it to the specified
     * file.
     * </p>
     * <b>Note</b>: If you plan to cancel the download at any time, then this
     * method should be embed into it's own thread.
     * <p>
     * <b>Example</b>:
     * 
     * <pre>
     *  final DownloadHelper downloader = new DownloadHelper();
     *  Thread t = new Thread() {
     *      public void run() {
     *          try {
     *              downloader.download("http://domain.com/file.zip", new File("/tmp/output.zip"));
     *          } catch (Exception e) {
     *              ...
     *          }
     *      }
     *  };
     *  t.start();
     *  // Waits 3 seconds and then cancels the download.
     *  Thread.sleep(3 * 1000L);
     *  downloader.cancel();
     *  ...
     * </pre>
     * 
     * </p>
     * 
     * @param url
     *            - The url of the file to download.
     * @param file
     *            - The downloaded file (where it will be stored).
     * @throws IOException
     * @throws HttpException
     */
    public void download(final String url, final File file) throws HttpException, IOException {

        GetMethod method = null;
        InputStream responseBody = null;
        OutputStream out = null;

        try {
            final boolean fileExisted = file.exists();

            HttpClient client = new HttpClient();
            method = new GetMethod(url);
            method.setFollowRedirects(true);
            method.setRequestHeader("User-Agent", WSConstants.WS_USER_AGENT);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            method.getParams().setParameter(HttpMethodParams.WARN_EXTRA_INPUT, Boolean.TRUE);

            // Execute the method.
            int responseCode = client.executeMethod(method);
            if (responseCode != HttpStatus.SC_OK) {
                logger.error("Http method failed : {}.", method.getStatusLine().toString());
            }

            // Read the response body.
            responseBody = method.getResponseBodyAsStream();

            // Let's try to compute the amount of total bytes of the file to
            // download.
            URL u = new URL(url);
            URLConnection urlc = u.openConnection();
            this.totalBytes = urlc.getContentLength();
            long lastModified = urlc.getLastModified();

            // The OutputStream for the 'downloaded' file.
            out = new BufferedOutputStream(new FileOutputStream(file));

            this.updateState(DownloadState.IN_PROGRESS);

            byte[] data = new byte[BUFFER_SIZE];
            int length;
            while ((length = responseBody.read(data, 0, BUFFER_SIZE)) != -1 && !isCancelled) {
                out.write(data, 0, length);
                this.bytesDownloaded += length;
                setChanged();
                notifyObservers(getBytesDownloaded());
            }

            if (isCancelled) {
                this.updateState(DownloadState.CANCELED);
                logger.info("The download has been cancelled.");

            } else {
                // The download was not cancelled.
                out.flush();
                if (lastModified > 0) {
                    file.setLastModified(lastModified);
                }

                if (bytesDownloaded != totalBytes) {
                    logger.warn("The expected bytes to download is {}, but got {}.",
                            getTotalBytes(), getBytesDownloaded());
                    this.updateState(DownloadState.ERROR);
                }

                this.updateState(DownloadState.FINISHED);
                logger.info("Download of '{}' finished successfully.", url);
            }

            // If the file did not exist before the download but does exit now,
            // we must remove it if an error occurred or if the download was
            // cancelled.
            if (getState() == DownloadState.CANCELED || getState() == DownloadState.ERROR) {
                if (!fileExisted && file.exists()) {
                    FileUtils.forceDelete(file);
                }
            }

        } catch (HttpException he) {
            this.updateState(DownloadState.ERROR);
            logger.error("Fatal protocol violation : " + he);
            throw new HttpException("Error while downloading from the url '" + url + "'", he);

        } catch (IOException ioe) {
            this.updateState(DownloadState.ERROR);
            logger.error("Fatal transport error : " + ioe);
            throw new IOException(ioe);

        } finally {
            if (method != null)
                method.releaseConnection();
            if (responseBody != null)
                responseBody.close();
            if (out != null)
                out.close();
        }
    }

    // _________________________________________________________________________

    private void updateState(DownloadState newState) {
        this.state = newState;
        setChanged();
        notifyObservers(getBytesDownloaded());
    }

    /**
     * Cancels the download.
     */
    public void cancel() {
        this.isCancelled = true;
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    public DownloadState getState() {
        return state;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public int getBytesDownloaded() {
        return bytesDownloaded;
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! XXX
     */
    @Deprecated
    public static void main(String[] args) throws HttpException, IOException, InterruptedException {

        final String url = "http://sourceforge.net/projects/mod-security/files/modsecurity-apache/2.6.1/modsecurity-apache_2.6.1.tar.gz/download";
        final File file = new File("test.tar.gz");

        final DownloadHelper downloader = new DownloadHelper();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    downloader.download(url, file);
                } catch (Exception e) {
                    logger.error("Download failed ... ", e);
                }
            }
        };
        t.start();
        // Waits 3 seconds and then cancels the download.
        Thread.sleep(3 * 1000L);
        downloader.cancel();
    }
}
