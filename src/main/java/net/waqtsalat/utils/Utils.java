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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Contains some utilities that many other classes may use<br>
 * Such as checking the OS, updates, date/time formatting ...
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Utils {

    /**
     * Get the timestamp of a remote file from a given url.
     * 
     * @param url
     *            ({@link URL}) Url to use for the check.
     * @return The timestamp of the remote file.
     * @throws IOException
     */
    public long getRemoteFileTimestamp(URL url) throws IOException {
        try {
            logger.trace("Trying to get the timestamp of remote file '{}'", url.toString());
            URLConnection urlc = url.openConnection();
            long timestamp = urlc.getLastModified();
            logger.trace("Remote timestamp is: {}", new Date(timestamp));
            return timestamp;
        } catch (IOException ioe) {
            throw new IOException("Error while retreiving the timestamp of the remote file!");
        }
    }

    // =======================================================================

    /**
     * Get the local timestamp of a local file.
     * 
     * @param file
     *            File to check the the last modification time (timestamp).
     * @throws IOException
     * @return The timestamp of the last modification time.
     */
    public long getLocalFileTimestamp(File file) throws IOException {
        try {
            logger.trace("Trying to get the timestamp of file '{}'.",
                    file.getAbsolutePath());
            assertFileIsValid(file);
            long timestamp = file.lastModified();
            logger.trace("Local timestamp is: {}", new Date(timestamp));
            return timestamp;
        } catch (IOException ioe) {
            throw new IOException("Error while retreiving the timestamp of '"
                    + file.getAbsolutePath() + "' !");
        }
    }

    // =======================================================================

    /**
     * Check whether or not a file is valid and readable.
     * 
     * @param file
     *            File to check whether it is valid or not.
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

    // =======================================================================

    /**
     * Check whether or not an update is available for a file by comparing the
     * timestamps of the files.
     * 
     * @param localTimestamp
     *            Local timestamp
     * @param remoteTimestamp
     *            Remote timestamp (candidate for the update)
     * @return <code>True</code> if remoteTimestamp greater than localTimestamp,
     *         <code>false</code> otherwise.
     */
    public boolean checkUpdate(long localTimestamp, long remoteTimestamp) {
        return (localTimestamp < remoteTimestamp)
                ? true : false;
    }

    // =======================================================================

    /**
     * Compare 2 versions lexicographically.
     * 
     * @param localVersion
     *            Local version of the software.
     * @param remoteVersion
     *            Remote version of the software.
     * @return <code>True</code> if a remote version is newer,
     *         <code>false</code> otherwise.
     */
    public boolean checkUpdate(String localVersion, String remoteVersion) {
        return (localVersion.compareTo(remoteVersion) < 0)
                ? true : false;
    }

    // ======================================================================

    /**
     * Returns whether we're running under Windows 95/98/ME.
     * 
     * @return <code>true</code> if running an Windows 95/98/ME,
     *         <code>false</code> otherwise.
     */

    public boolean isOldWindows() {
        String os = System.getProperty("os.name");

        return isWindows()
                && (System.getProperty("os.version").compareTo("5.0") < 0) &&
                !os.toLowerCase().startsWith("windows nt");
    }

    // ======================================================================

    /**
     * @return <code>true</code> if the platform is windows, <code>false</code>
     *         otherwise.
     */
    public boolean isWindows() {
        String os = System.getProperty("os.name");
        return (os != null) && os.toLowerCase().startsWith("windows");
    }

    // ======================================================================

    /**
     * @return <code>true</code> if the platform is Linux, <code>false</code>
     *         otherwise.
     */
    public boolean isLinux() {
        String os = System.getProperty("os.name");
        return (os != null) && os.toLowerCase().startsWith("linux");
    }

    // ======================================================================

    /**
     * @return <code>true</code> if the platform is Mac, <code>false</code>
     *         otherwise.
     */
    public boolean isMac() {
        String os = System.getProperty("os.name");
        return (os != null) && os.toLowerCase().startsWith("mac");
    }

    // ======================================================================

    /**
     * Returns whether we're running under Mac OS X.
     * 
     * @return <code>true</code> if running Mac OS X, <code>false</code>
     *         otherwise.
     */

    public boolean isMacOSX() {
        String os = System.getProperty("os.name");
        return (os != null) && os.toLowerCase().startsWith("mac os x");
    }

    // ======================================================================

    /**
     * Returns whether we're running under Solaris.
     * 
     * @return <code>true</code> if running Solaris, <code>false</code>
     *         otherwise.
     */

    public boolean isSolaris() {
        String os = System.getProperty("os.name");
        return (os != null) && (os.toLowerCase().startsWith("solaris") ||
                os.toLowerCase().startsWith("sunos"));
    }

    // ======================================================================

    /**
     * Test whether or not we have an internet connection.<br>
     * <span style="color: rgb(51, 204, 0);">This routine is not reliable at
     * all, but can suit for most common cases.</span>
     * 
     * @return <code>true</code> if internet connection is available,
     *         <code>false</code> otherwise.
     */
    public boolean isInternetReachable() { // TODO: Try to improve this method
                                           // ...
        try {
            URL url = new URL("http://google.com");
            URLConnection urlc = url.openConnection();
            urlc.setConnectTimeout(20 * 1000); // timeout of 20 sec
            urlc.getContent();
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return true;
    }

    // ======================================================================

    /**
     * Retrieve all available time zones IDs and sort them.
     * 
     * @return The sorted array of all time zones.
     */
    public static String[] getAllTimezonesSorted() {
        String[] tmz = TimeZone.getAvailableIDs();
        for (int i = 0; i < tmz.length; i++) {
            for (int j = i; j < tmz.length; j++) {
                if (tmz[j].compareTo(tmz[i]) < 0) {
                    String temp = tmz[j];
                    tmz[j] = tmz[i];
                    tmz[i] = temp;
                }
            }
        }
        return tmz;
    }

    // ======================================================================

    public static ArrayList<String> getAllTimezonesListSorted() {
        String[] array = getAllTimezonesSorted();
        ArrayList<String> tmz = new ArrayList<String>();
        for (int i = 0; i < array.length; i++)
            tmz.add(array[i]);
        return tmz;
    }

    // ======================================================================

    /**
     * @return A {@link HashMap} containing all ISO country codes as keys and
     *         all countries names as values using the US Locale.
     */
    public static HashMap<String, String> getCountriesCodeAndName() {
        return getCountriesCodeAndName(Locale.US);
    }

    // ======================================================================

    /**
     * @param aLocale
     *            The {@link Locale} to use.
     * @return A {@link HashMap} containing all ISO country codes as keys and
     *         all countries names as values.
     */
    public static HashMap<String, String>
            getCountriesCodeAndName(Locale aLocale) {
        HashMap<String, String> countries = new HashMap<String, String>();
        String[] all_ISOCountries = Locale.getISOCountries();

        String lang = aLocale.getLanguage();
        for (int i = 0; i < all_ISOCountries.length; i++) {
            String cc = all_ISOCountries[i];
            String countryName = new Locale(lang, cc).getDisplayCountry(aLocale);
            countries.put(cc, countryName);
            // System.out.println(cc + ", " + countryName);
        }
        return countries;
    }

    // ======================================================================

    /**
     * Gets the extension of a file.
     * 
     * @param file
     *            The <code>File</code> to use.
     * @return A <code>String</code> which represents the extension of the given
     *         file, returns <code>null</code> if the file is null or not valid,
     *         or if the dot is at the end of the filename.
     * 
     */
    public static String getExtension(File file) {
        if (file == null) {
            return null;
        } else {
            String fileName = file.getName();
            int indexOflastDot = fileName.lastIndexOf('.');
            if ((indexOflastDot > 0)
                    && (indexOflastDot < fileName.length() - 1)) {
                return fileName.substring(indexOflastDot + 1);
            }
            return null;
        }
    }

    // ======================================================================

    /**
     * Checks whether or not an object is into an array.<br>
     * Be aware that the types into array and the type of the object to search
     * must be the same type.
     * 
     * @param obj
     *            The object to search into the array.
     * @param array
     *            The array where to search the object.
     * @return <code>true</code> if the the object <code>obj</code> is into
     *         <code>array</code>.
     * 
     */
    public static boolean isIntoArray(Object obj, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (obj.equals(array[i]))
                return true;
        }
        return false;
    }

    // ======================================================================

    /**
     * This is a convenience method for converting duration time/timestamp into
     * a more human readable form.<br>
     * For example, the following will convert 329 to 05:29 if we do:<br>
     * formatDuration("mm:ss", 329);
     * 
     * @param format
     *            The pattern to use formatting the duration. This argument
     *            follows the same rules as pattern in {@link SimpleDateFormat}.
     *            Example: "HH:mm:ss"
     * @param duration
     *            The duration or timestamp in milliseconds ...
     * @return The String representation of the duration.
     * 
     */
    public static String formatDuration(String format, long duration) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(duration));
    }

    // ======================================================================

    /**
     * Got it from this <a href=
     * "http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880"
     * >link</a>.
     * 
     * @param bytes
     * @param si
     * @return A String representation of the filesize.
     * 
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si
                ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        // String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" :
        // "i");
        String pre = (si
                ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // ======================================================================

    /**
     * Prints the error message of a process if it does have an error message.
     * 
     * @param p
     *            The {@link Process} to display its error.
     * @throws IOException
     */
    public static void printErrorProcess(Process p) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        StringBuilder errMsg = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            errMsg.append(s + "\n");
        }
        logger.warn("The warning/error message is :\n{}", errMsg);
    }

    // ======================================================================
}
