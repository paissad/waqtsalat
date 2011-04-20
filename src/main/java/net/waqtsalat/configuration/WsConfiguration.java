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

package net.waqtsalat.configuration;

import java.io.File;

import net.waqtsalat.IpAddress;
import net.waqtsalat.Pray;
import net.waqtsalat.WaqtSalat;
import net.waqtsalat.utils.GeoipUtils;
import net.waqtsalat.utils.Utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WsConfiguration {

    private static final String     KEY_AUTOMATIC                      = "automatic";
    private static final String     KEY_SITE                           = "site";
    private static final String     KEY_IP_ADDRESS                     = "ip_address";
    private static final String     KEY_LATITUDE                       = "latitude";
    private static final String     KEY_LONGITUDE                      = "longitude";
    private static final String     KEY_PLAY                           = "play";
    private static final String     KEY_MUEZZIN_CALL_SOUND             = "muezzin_call_sound";
    private static final String     KEY_DAEMON                         = "daemon";
    private static final String     KEY_SERVER_HOSTNAME                = "hostname";
    private static final String     KEY_SERVER_PORT                    = "port";
    private static final String     KEY_PROXY_SERVER_PORT              = "proxy";
    private static final String     KEY_GEOIP_DATABASE                 = "geoip_database";
    private static final String     KEY_LANGUAGE                       = "language";

    private static final int        DEFAULT_PORT                       = 6001;
    private static final int        DEFAULT_PROXY_PORT                 = -1;

    private static final String     DEFAULT_CONFIGURATION_FILENAME     = "waqtsalat.conf";
    private static final String     DEFAULT_CONFIGURATION_PATH_MAC     = System
                                                                               .getProperty("user.home")
                                                                               + "/Library/Application Support/WaqtSalat/";
    private static final String     DEFAULT_CONFIGURATION_PATH_WINDOWS = System
                                                                               .getProperty("user.home")
                                                                               + File.separator;
    private static final String     DEFAULT_CONFIGURATION_PATH_LINUX_1 = System
                                                                               .getProperty("user.home")
                                                                               + "/.";
    private static final String     DEFAULT_CONFIGURATION_PATH_LINUX_2 = "/etc/";

    private static String           _confFileName                      = "";
    private PropertiesConfiguration configuration                      = new PropertiesConfiguration();

    // =======================================================================

    public WsConfiguration(String confFileName) throws ConfigurationException {
        _confFileName = (confFileName == null)
                ? "" : confFileName;

        if (_confFileName.isEmpty()) {
            // WaqtSalat.logger.warn("The configuration filename cannot be empty or null !");
            setDefaultConfFileName();
        }

        if (new File(_confFileName).exists())
            configuration.load(_confFileName);
        else
            WaqtSalat.logger.warn("Configuration file does not exist: '{}'",
                    _confFileName);
    }

    // =======================================================================

    public String setDefaultConfFileName() {
        Utils platform = new Utils();
        if (platform.isLinux()) {
            if (new File(DEFAULT_CONFIGURATION_PATH_LINUX_1
                    + DEFAULT_CONFIGURATION_FILENAME).exists())
                _confFileName = DEFAULT_CONFIGURATION_PATH_LINUX_1;
            else if (new File(DEFAULT_CONFIGURATION_PATH_LINUX_2
                    + DEFAULT_CONFIGURATION_FILENAME).exists())
                _confFileName = DEFAULT_CONFIGURATION_PATH_LINUX_2;
        } else if (platform.isWindows()
                && new File(DEFAULT_CONFIGURATION_PATH_WINDOWS
                        + DEFAULT_CONFIGURATION_FILENAME).exists()) {
            _confFileName = DEFAULT_CONFIGURATION_PATH_WINDOWS;
        } else if (platform.isMac()
                && new File(DEFAULT_CONFIGURATION_PATH_MAC
                        + DEFAULT_CONFIGURATION_FILENAME).exists()) {
            _confFileName = DEFAULT_CONFIGURATION_PATH_MAC;
        }

        _confFileName += DEFAULT_CONFIGURATION_FILENAME;
        return _confFileName;
    }

    // =======================================================================

    private String getString(String key, String def) {
        String value = configuration.getString(key, def);
        if (value != null)
            value = value.trim();
        return value;
    }

    // =======================================================================

    private int getInt(String key, int def) {
        try {
            return configuration.getInt(key, def);
        } catch (ConversionException e) {
            return def;
        }
    }

    // =======================================================================

    private double getDouble(String key, double def) {
        try {
            return configuration.getDouble(key, def);
        } catch (ConversionException e) {
            return def;
        }
    }

    // =======================================================================

    private boolean getBoolean(String key, boolean def) {
        try {
            return configuration.getBoolean(key, def);
        } catch (ConversionException e) {
            return def;
        }
    }

    // =======================================================================

    public String getConfFileName() {
        return _confFileName;
    }

    public boolean isAutomatic() {
        return getBoolean(KEY_AUTOMATIC, true);
    }

    public void setAutomatic(boolean value) {
        configuration.setProperty(KEY_AUTOMATIC, value);
    }

    public String getSite() {
        return getString(KEY_SITE, new IpAddress().getDefaultMethodName());
    }

    public void setSite(String value) {
        configuration.setProperty(KEY_SITE, value);
    }

    public String getIpAddress() {
        return getString(KEY_IP_ADDRESS, null);
    }

    public void setIpAddress(String value) {
        configuration.setProperty(KEY_IP_ADDRESS, value);
    }

    public double getLatitude() {
        return getDouble(KEY_LATITUDE, -1);
    }

    public void setLatitude(double value) {
        configuration.setProperty(KEY_LATITUDE, value);
    }

    public double getLongitude() {
        return getDouble(KEY_LONGITUDE, -1);
    }

    public void setLongitude(double value) {
        configuration.setProperty(KEY_LONGITUDE, value);
    }

    public boolean isPlay() {
        return getBoolean(KEY_PLAY, false);
    }

    public void setPlay(boolean value) {
        configuration.setProperty(KEY_PLAY, value);
    }

    public String getMuezzinCallSound() {
        Pray pray = new Pray();
        return getString(KEY_MUEZZIN_CALL_SOUND, pray.getMuezzinSound());
    }

    public boolean isDaemon() {
        return getBoolean(KEY_DAEMON, false);
    }

    public void setDaemon(boolean value) {
        configuration.setProperty(KEY_DAEMON, value);
    }

    public String getServerHostname() {
        return getString(KEY_SERVER_HOSTNAME, null);
    }

    public void setServerHostname(String value) {
        configuration.setProperty(KEY_SERVER_HOSTNAME, value);
    }

    public int getServerPort() {
        return getInt(KEY_SERVER_PORT, DEFAULT_PORT);
    }

    public void setServerPort(int value) {
        configuration.setProperty(KEY_SERVER_PORT, value);
    }

    public int getProxyServerPort() {
        return getInt(KEY_PROXY_SERVER_PORT, DEFAULT_PROXY_PORT);
    }

    public void setProxyServerPort(int value) {
        configuration.setProperty(KEY_PROXY_SERVER_PORT, value);
    }

    public String getGeoipDatabase() {
        return getString(KEY_GEOIP_DATABASE,
                GeoipUtils.GEOIP_DATABASE_FULL_PATH);
    }

    public void setGeoipDatabase(String value) {
        configuration.setProperty(KEY_GEOIP_DATABASE, value);
    }

    public String getLanguage() {
        return getString(KEY_LANGUAGE, null);
    }

    public void setLanguage(String value) {
        configuration.setProperty(KEY_LANGUAGE, value);
    }
    
    // =======================================================================

}
