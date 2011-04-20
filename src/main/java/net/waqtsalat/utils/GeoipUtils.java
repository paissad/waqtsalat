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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.maxmind.geoip.LookupService;

/**
 * Some utilities such as looking for updates, or download of GeoIP database.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class GeoipUtils {

    private static final String FS                          = File.separator;
    private static final String GEOIP_SAVE_PATH             = "extras" + FS
                                                                    + "geoip";

    public static final String  GEOIP_DATABASE_FILENAME     = "GeoLiteCity.dat";
    public static final String  GEOIP_DATABASE_FULL_PATH    = GEOIP_SAVE_PATH
                                                                    + FS
                                                                    + GEOIP_DATABASE_FILENAME;
    public static final String  GEOIP_DATABASE_UPDATE_URL   = "http://geolite.maxmind.com/download/geoip/database/"
                                                                    + GEOIP_DATABASE_FILENAME
                                                                    + ".gz";

    public static final String  GEOIP_WORLDCITIES_FILENAME  = "worldcitiespop.txt";
    public static final String  GEOIP_WORLDCITIES_FULL_PATH = GEOIP_SAVE_PATH
                                                                    + FS
                                                                    + GEOIP_WORLDCITIES_FILENAME;
    public static final String  GEOIP_WORLDCITIES_URL       = "http://www.maxmind.com/download/worldcities/"
                                                                    + GEOIP_WORLDCITIES_FILENAME
                                                                    + ".gz";

    public static final int     GEOIP_OPTIONS               = LookupService.GEOIP_MEMORY_CACHE;
    public static final double  LATITUDE_MAKKAH             = 21.42738;
    public static final double  LONGITUDE_MAKKAH            = 39.81484;

    /**
     * The 'DB' (DataBase) type is for GeoIp Database (GeoLiteCity.dat)<br>
     * The 'WC' (World-Cities) type is for the GeoIp worldcities.txt file.
     * 
     * @author Papa Issa DIAKHATE (<a
     *         href="mailto:paissad@gmail.com">paissad</a>)
     */
    public static enum GEOIP_TYPE {
        DB,
        WC
    };

    // =======================================================================

    /**
     * Sole constructor.
     */
    public GeoipUtils() {
    }

    // =======================================================================

    /**
     * Update one GeoIp entity. (GeoLiteCity.dat or worldcitiespop.txt)
     * 
     * @param type
     *            A type which is a value of {@link GEOIP_TYPE}.
     * @throws BadGeoipTypeException
     *             When the specified type in argument is unknown.
     */
    public void updateGeoip(GEOIP_TYPE type) throws BadGeoipTypeException {
        if (isUpdateAvailable(type))
            download(type);
    }

    // =======================================================================

    /**
     * Download one type of GeoIp stuff (GeoLiteCity.dat, worldcitiespop.txt)
     * 
     * @param type
     *            A type which is a value of {@link GEOIP_TYPE}.
     * @throws BadGeoipTypeException
     *             When the specified type in argument is unknown.
     * @throws BadGeoipTypeException
     */
    public void download(GEOIP_TYPE type) throws BadGeoipTypeException {
        if (isCorrectType(type)) {
            DownloadUtils util = null;
            File file_downloaded = null;
            File file_decompressed = null;

            try {
                long remoteFileTimestamp = getRemoteGeoipTimestamp(type);
                if (type.equals(GEOIP_TYPE.DB)) {
                    util = new DownloadUtils(new URL(GEOIP_DATABASE_UPDATE_URL));
                } else if (type.equals(GEOIP_TYPE.WC)) {
                    util = new DownloadUtils(new URL(GEOIP_WORLDCITIES_URL));
                }
                file_downloaded = util.download(new File(GEOIP_SAVE_PATH + FS
                        + util.getFileNameFromURL()));
                file_decompressed = new UncompressUtils(file_downloaded).uncompressSmart();
                file_decompressed.setLastModified(remoteFileTimestamp);
                if (file_downloaded.exists())
                    file_downloaded.delete();
            } catch (IOException ioe) {
                logger.error("Download FAILED. ({})", file_downloaded.getName());
                ioe.printStackTrace();
            }
        } else {
            throw new BadGeoipTypeException("Unknown GeoIp type ...");
        }
    }

    // =======================================================================

    /**
     * Checks whether or not an update is available for the the specified GeoIp
     * entity (Database or World-Cities).
     * 
     * @param type
     *            A type which is a value of {@link GEOIP_TYPE}.
     * @return <code>true</code> if an update is available, <code>false</code>
     *         otherwise.
     * @throws BadGeoipTypeException
     */
    public boolean isUpdateAvailable(GEOIP_TYPE type)
            throws BadGeoipTypeException {
        if (isCorrectType(type)) {
            boolean update = new Utils().checkUpdate(
                    getLocalGeoipTimestamp(type), getRemoteGeoipTimestamp(type));

            if (update)
                logger.info("An update is available for ({}).", getCorrectGeoipFile(type).getName());
            else
                logger.info("No update available yet for ({}).", getCorrectGeoipFile(type).getName());
            return update;
        } else {
            throw new BadGeoipTypeException("Unknown GeoIp type ...");
        }
    };

    // =======================================================================

    /**
     * Get the timestamp of the local GeoIP file (GeoLiteCity.dat or
     * worldcitiespop.txt).
     * 
     * @param type
     *            A type which is a value of {@link GEOIP_TYPE}.
     * @return The timestamp of the local file (GeoLiteCity.dat or
     *         worldcitiespop.txt)
     * @throws BadGeoipTypeException
     */
    public static long getLocalGeoipTimestamp(GEOIP_TYPE type)
            throws BadGeoipTypeException {
        long _error = -1L;
        if (isCorrectType(type)) {
            File geoipFile = getCorrectGeoipFile(type);
            if (geoipFile.exists()) {
                try {
                    return new Utils().getLocalFileTimestamp(geoipFile);
                } catch (IOException e) {
                    logger.error(
                            "Error while retrieving the timestamp of the local file'{}'",
                            geoipFile.getAbsolutePath());
                    e.printStackTrace();
                }
            }
            return _error;
        } else {
            throw new BadGeoipTypeException("Unknown GeoIp type ...");
        }
    }

    // =======================================================================

    /**
     * Get the timestamp of a remote GeoIP file (GeoLiteCity.dat.gz or
     * worldcitiespop.txt.gz)
     * 
     * @param type
     *            A type which is a value of {@link GEOIP_TYPE}.
     * @return The timestamp of the remote file.
     * @throws BadGeoipTypeException
     */
    public static long getRemoteGeoipTimestamp(GEOIP_TYPE type)
            throws BadGeoipTypeException {
        long _error = -1L;
        if (isCorrectType(type)) {
            try {
                if (type.equals(GEOIP_TYPE.DB))
                    return new Utils().getRemoteFileTimestamp(new URL(GEOIP_DATABASE_UPDATE_URL));
                else if (type.equals(GEOIP_TYPE.WC))
                    return new Utils().getRemoteFileTimestamp(new URL(GEOIP_WORLDCITIES_URL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return _error;
        } else {
            throw new BadGeoipTypeException("Unknown GeoIp type ...");
        }
    }

    // =======================================================================

    /**
     * Checks whether or not a specified type is correct or not.
     * 
     * @return Return <code>true</code> if type is a know type,
     *         <code>false</code> otherwise.
     */
    private static boolean isCorrectType(GEOIP_TYPE type) {
        GEOIP_TYPE[] allowed_types = GEOIP_TYPE.values();
        for (int i = 0; i < allowed_types.length; i++) {
            if (type.equals(allowed_types[i]))
                return true;
        }
        return false;
    }

    // =======================================================================

    private static File getCorrectGeoipFile(GEOIP_TYPE type)
            throws BadGeoipTypeException {

        if (type.equals(GEOIP_TYPE.DB))
            return new File(GEOIP_SAVE_PATH + FS + GEOIP_DATABASE_FILENAME);
        else if (type.equals(GEOIP_TYPE.WC))
            return new File(GEOIP_SAVE_PATH + FS + GEOIP_WORLDCITIES_FILENAME);
        else {
            throw new BadGeoipTypeException("Unknown GeoIp type ...");
        }
    }

    // =======================================================================

    /**
     * Exception thrown when an error occurs with {@link GeoipUtils}.<br>
     * Mostly, when a specified {@link GEOIP_TYPE} type is does not exist.
     */
    public static class BadGeoipTypeException extends Exception {

        private static final long serialVersionUID = 1L;

        private BadGeoipTypeException() {
            super();
            System.err.println("Error in GeoIpUtils ...");
        }

        private BadGeoipTypeException(String message) {
            super(message);
            System.err.println(message);
        }

    }

    // =======================================================================

}
