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

package net.paissad.waqtsalat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class CommonUtils {

    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {
    }

    /**
     * Returns the filename extension without the dot.
     * 
     * @param filename
     * @return The extension with the dot included, or <code>null</code> if the
     *         filename is null, or an empty String if the file has no
     *         extension.
     */
    public static String getFilenameExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\.[^\\.]*$");
        Matcher matcher = pattern.matcher(filename);
        int count = matcher.groupCount();
        return (matcher.find()) ? matcher.group(count) : "";
    }

    // _________________________________________________________________________

    /**
     * @param bytes
     * @param si
     *            - If <code>true</code> then use 1000 unit, 1024 otherwise.
     * @return A String representation of the file size.
     * 
     */
    public static String humanReadableByteCount(final long bytes, final boolean si) {
        int unit = si ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // _________________________________________________________________________

    /**
     * @param url
     *            - The url of the resource we want to get its timestamp.
     * @return The timestamp (last modification date) of the specified
     *         url/resource, -1L if an error occurred, 0L if not known.
     */
    public static long getRemoteTimestamp(final String url) {
        try {
            URL u = new URL(url);
            URLConnection urlc = u.openConnection();
            return urlc.getLastModified();

        } catch (Exception e) {
            return -1L;
        }
    }

    // _________________________________________________________________________

    /**
     * Test whether or not we have an internet connection.
     * <p>
     * <span style="color: rgb(51, 204, 0);">This routine is not reliable at
     * all, but can suit for most common cases.</span>
     * </p>
     * 
     * @return <tt>true</tt> if internet connection is available, <tt>false</tt>
     *         otherwise.
     */
    public static boolean isInternetReachable() {
        try {
            URL url = new URL("http://google.com");
            URLConnection urlc = url.openConnection();
            urlc.setConnectTimeout(20 * 1000); // timeout of 20 sec
            urlc.getContent();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // _________________________________________________________________________

    /**
     * @return The IP address of the machine which is running the application,
     *         -1 if no ip is found.
     * @throws IOException
     */
    public static String retreiveIpAddress() throws IOException {

        if (!CommonUtils.isInternetReachable()) {
            logger.warn("It seems like you don't have an internet connection.");
        }

        try {
            URL url = new URL("http://checkip.dyndns.org");

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipRegex = " *(((\\d{1,3}\\.){3}\\d{1,3}))";
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = bf.readLine()) != null) {
                sb.append(inputLine);
            }
            Pattern pattern = Pattern.compile(ipRegex);
            Matcher matcher = pattern.matcher(sb.toString());

            return (matcher.find()) ? matcher.group(2) : "-1";

        } catch (IOException ioe) {
            throw new IOException("Error while retreiving public ip address !");
        }
    }

    // _________________________________________________________________________

    /**
     * @param aLocale
     *            The {@link Locale} to use.
     * @return A {@link Map} containing all ISO country codes as keys and
     *         all countries names as values.
     */
    public static Map<String, String> getCountries(Locale aLocale) {

        Map<String, String> countries = new HashMap<String, String>();
        String[] allISOCountries = Locale.getISOCountries();
        String lang = aLocale.getLanguage();
        for (String country : allISOCountries) {
            String name = new Locale(lang, country).getDisplayCountry(aLocale);
            countries.put(country, name);
        }
        return countries;
    }

    // _________________________________________________________________________

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

    // ======================================================================

    /**
     * This is a convenient method for converting duration time/timestamp into
     * a more human readable form.
     * <p>
     * <b>Example</b>:
     * 
     * <pre>
     * formatDuration("mm:ss", 329) => 05:29
     * </pre>
     * 
     * </p>
     * 
     * @param duration
     *            The duration or timestamp in milliseconds.
     * @return The String representation of the duration.
     * 
     */
    public static String formatDuration(final long duration) {
        final String format = "mm:ss";
        final SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(duration));
    }

    /**
     * Retrieve all available time zones IDs and sort them.
     * 
     * @return The sorted array of all time zones.
     */
    public static String[] getAllTimezonesSorted() {
        String[] tmz = TimeZone.getAvailableIDs();
        Collections.sort(Arrays.asList(tmz));
        return tmz;
    }

    // _________________________________________________________________________

    /**
     * Gets all native audio types supported by the system.
     * 
     * @return An array of <tt>String</tt> containing the supported audio
     *         formats.
     */
    public static List<String> getSupportedAudioTargetTypes() {

        List<String> list = new ArrayList<String>();
        for (AudioFileFormat.Type t : AudioSystem.getAudioFileTypes()) {
            list.add("." + t.getExtension().toLowerCase());
        }
        return list;
    }

    // _________________________________________________________________________

}
