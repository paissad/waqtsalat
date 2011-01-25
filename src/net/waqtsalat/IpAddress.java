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

package net.waqtsalat;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates informations about the Ip Address.<br />
 * Contains methods to retreive automatically the Ip Address.
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>) 
 */
public class IpAddress {

	// Different available methods/sites to get the public ip address !
	public static final String PAISSAD         = "http://paissad.net/myip";
	public static final String DYNDNS          = "http://checkip.dyndns.org";
	public static final String WHAT_IS_MY_IP   = "http://www.whatismyip.com/automation/n09230945.asp";
	private static final String DEFAULT_METHOD = DYNDNS;

	Logger logger = LoggerFactory.getLogger(getClass());

	private String ipAddress     = "-1";
	private String method        = DEFAULT_METHOD;

	public IpAddress() {
	}
	//=======================================================================

	/**
	 * @param method The method used for retreiving the public ip address.
	 */
	public IpAddress(String method) {
		this.method = method;
	}
	//=======================================================================

	/**
	 * @param ipAddress Ip address.
	 * @param method The method used for retreiving the public ip address.
	 */
	public IpAddress(String ipAddress, String method) {
		this.ipAddress = ipAddress;
		this.method    = method;
	}
	//=======================================================================

	/**
	 * Get the current ip address of the object.
	 * @return Return a String representing the current ip address of the object.
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	//=======================================================================

	/**
	 * Force/Change the ip address of the current object.
	 * @param ipAddress Ip Address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	//=======================================================================

	/**
	 * Get the current method used to retreive the ip address of this object. 
	 * @return Return a String representing the current method used for this object.
	 */
	public String getMethod() {
		return method;
	}
	//=======================================================================

	/**
	 * Force/Change the method to use while retreiving the ip address from a site.<br />
	 * Default method is DYNDNS.
	 * @param method The method used for retreiving the public ip address.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	//=======================================================================

	/**
	 * @return Return a String representing the current ip address of the object.
	 */
	public String toString() {
		return getIpAddress();
	}
	//=======================================================================

	/**
	 * Retreive the public ip address of the object from a known site/method !<br />
	 * Known methods/sites are:
	 * <ul>
	 * <li>PAISSAD    - <a href="http://paissad.net/myip">http://paissad.net/myip</a></li>
	 * <li>WHATISMYIP - <a href="http://www.whatismyip.com/automation/n09230945.asp">http://www.whatismyip.com/automation/n09230945.asp</a></li>
	 * <li>DYNDNS     - <a href="http://checkip.dyndns.org">http://checkip.dyndns.org</a></li>
	 * </ul>
	 * @return Return a String representing the ip address retreived with the specified method.
	 * @throws IOException
	 */
	public String retreiveIpAddress() throws IOException {
		try{
			String ip = "-1"; 
			URL url = new URL(this.method);

			BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
			String ipRegex   = " *(((\\d{1,3}\\.){3}\\d{1,3}))";
			StringBuffer sb  = new StringBuffer();
			String inputLine;

			while((inputLine = bf.readLine()) != null) {
				sb.append(inputLine);
			}
			Pattern pattern = Pattern.compile(ipRegex);
			Matcher matcher = pattern.matcher(sb.toString());

			if(matcher.find())
				ip = matcher.group(2);
			else
				logger.error("No Ip Address ...");

			this.ipAddress = ip;
			return this.ipAddress;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			throw new IOException();
		}
	}
	//=======================================================================

}