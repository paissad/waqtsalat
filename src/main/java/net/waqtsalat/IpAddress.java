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

package net.waqtsalat;

import static net.waqtsalat.WaqtSalat.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.waqtsalat.utils.Utils;

/**
 * Encapsulates informations about the Ip Address.<br>
 * Contains methods to retrieve automatically the Ip Address.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class IpAddress {

    /** <a href="http://paissad.net/myip">http://paissad.net/myip</a> */
    public static final String PAISSAD        = "http://paissad.net/myip";

    /** <a href="http://checkip.dyndns.org">http://checkip.dyndns.org</a> */
    public static final String DYNDNS         = "http://checkip.dyndns.org";

    /**
     * <a href="http://www.whatismyip.com/automation/n09230945.asp">http://www.
     * whatismyip.com/automation/n09230945.asp</a>
     */
    public static final String WHAT_IS_MY_IP  = "http://www.whatismyip.com/automation/n09230945.asp";

    /**
     * Default method used to retrieve the ip address. Default method is DYNDNS.
     */
    public static String       DEFAULT_METHOD = DYNDNS;

    private String             ip             = "-1";
    private String             method         = DEFAULT_METHOD;

    // =======================================================================

    public IpAddress() {
    }

    // =======================================================================

    /**
     * @param method
     *            The method to use to retrieve the public ip address.
     * @see #DEFAULT_METHOD
     * @see #DYNDNS
     * @see #PAISSAD
     * @see #WHAT_IS_MY_IP
     */
    public IpAddress(String method) {
        assert isValidMethod();
        this.method = method;
    }

    // =======================================================================

    /**
     * Retrieves the public ip address of the object from a known method.<br>
     * Known methods are:
     * <ul>
     * <li>PAISSAD - <a
     * href="http://paissad.net/myip">http://paissad.net/myip</a></li>
     * <li>WHAT_IS_MY_IP - <a
     * href="http://www.whatismyip.com/automation/n09230945.asp"
     * >http://www.whatismyip.com/automation/n09230945.asp</a></li>
     * <li>DYNDNS - <a
     * href="http://checkip.dyndns.org">http://checkip.dyndns.org</a></li>
     * </ul>
     * 
     * @return A <code>String</code> representing the ip address retrieved with
     *         the specified method.
     * @throws IOException
     */
    public String retreiveIpAddress() throws IOException {

        Utils connection = new Utils();
        if (!connection.isInternetReachable())
            WaqtSalat.logger.warn("It seems like you do not have an internet connection.");

        try {
            URL url = new URL(this.method);

            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            String ipRegex = " *(((\\d{1,3}\\.){3}\\d{1,3}))";
            StringBuffer sb = new StringBuffer();
            String inputLine;

            while ((inputLine = bf.readLine()) != null) {
                sb.append(inputLine);
            }

            Pattern pattern = Pattern.compile(ipRegex);
            Matcher matcher = pattern.matcher(sb.toString());

            if (matcher.find())
                ip = matcher.group(2);
            else
                logger.error("No Ip Address ...");

            return this.ip;
        } catch (IOException ioe) {
            throw new IOException("Error while retreiving public ip address !");
        }
    }

    // =======================================================================

    /**
     * Get the current ip address of the object.
     * 
     * @return Return a String representing the current ip address of the
     *         object.
     */
    public String getIpAddress() {
        return this.ip;
    }

    // =======================================================================

    /**
     * Forces/Changes the ip address of the current object.
     * 
     * @param ip
     *            Ip Address to set to.
     */
    public void setIpAddress(String ip) {
        this.ip = ip;
    }

    // =======================================================================

    /**
     * Get the current method used to retrieve the ip address of this object.
     * 
     * @return A <code>String</code> representing the current method used for
     *         this object.
     */
    public String getMethod() {
        return this.method;
    }

    // =======================================================================

    /**
     * Forces/Changes the method to use in order to retrieve the ip address from
     * a site.<br>
     * 
     * @param method
     *            The method used for retrieving the public ip address.
     */
    public void setMethod(String method) {
        assert isValidMethod();
        this.method = method;
    }

    // =======================================================================

    /**
     * @return <code>true</code> if the method name specified for this object is
     *         a valid one, <code>false</code> otherwise.
     */
    public boolean isValidMethod() {
        Set<String> availableMethods = getAvailableMethods().keySet();
        return availableMethods.contains(this.method);
    }

    // =======================================================================

    /**
     * Get the available methods we can use to retrieve the public ip addresses.
     * 
     * @return A {@link Map} containing all available method names with their
     *         related values.
     */
    public Map<String, String> getAvailableMethods() {
        Map<String, String> methods = new HashMap<String, String>();
        methods.put("DYNDNS", DYNDNS);
        methods.put("PAISSAD", PAISSAD);
        methods.put("WHAT_IS_MY_IP", WHAT_IS_MY_IP);
        return methods;
    }

    // =======================================================================

    /**
     * @return A <code>String</code> representation of the current ip address of
     *         the object.
     */
    public String toString() {
        return getIpAddress();
    }

    // =======================================================================

    /**
     * Get the name of the default method, not the value but the method name !<br>
     * 
     * @return The name of the default method.
     */
    public String getDefaultMethodName() {
        for (Field f : this.getClass().getFields()) {
            try {
                if (f.get(this).equals(DEFAULT_METHOD))
                    return f.getName();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    // =======================================================================

}
