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

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

import net.waqtsalat.MuezzinCallDaemon.BadSizePrayTimesArray;
import net.waqtsalat.configuration.WsParseCommandLine;
import net.waqtsalat.utils.GeoipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

public class WaqtSalat implements Observer {

	public static Logger logger = LoggerFactory.getLogger(WaqtSalat.class);
	private static ArrayList<String> _prayerTimes = new ArrayList<String>();
	private static ComputePrayTimes _computePrayTimes = new ComputePrayTimes();
	private static Object stateLock = new Object();
	private static boolean quiet = false;

	// =======================================================================

	public static void main(String[] args) throws IOException {
		new WaqtSalat(args);
	}
	// =======================================================================

	{
		_computePrayTimes.addObserver(this);
	}
	// =======================================================================

	public WaqtSalat(String[] args) throws IOException {

		WsParseCommandLine parser = new WsParseCommandLine(args);
		boolean _help = parser.isHelp();
		int _verboseLevel = parser.getVerboseLevel();
		boolean _auto = parser.isAutomatic();
		String _ip = parser.getIp();
		double _latitude = parser.getLatitude();
		double _longitude = parser.getLongitude();
		boolean _play = parser.isPlay();

		// Geoip Location informations ...
		Map<String, Object> geoipLocDatas = new LinkedHashMap<String, Object>(); 

		// Print the help if is specified and then exit.
		if (_help) {
			parser.printUsage();
			System.exit(0);
		}

		if (_longitude != -1 || _latitude != -1) { // latitude & longitude have
			// the priority over
			// automatic mode.
			if (_longitude == -1 || _latitude == -1) {
				logger.error("You must set both longitude and latitudes !!!");
				parser.printUsage();
				System.exit(1);
			}
		} else if (_ip != null || _auto) {
			if (_ip != null && !_ip.isEmpty()) { // then come automatic mode ...
				logger.info("Using forced ip address: {}", _ip);
			} else if (_auto) {
				logger.info("Public Ip Address ...");
				_ip = new IpAddress().retreiveIpAddress();
				logger.debug("Ip Address: {}", _ip);
				if (_ip.equals("-1"))
					logger.error("The Ip address cannot be '-1', an error occured.");
			}

			Location location = new LookupService(GeoipUtils.GEOIP_DATABASE_COMPLETE_PATH).getLocation(_ip);

			geoipLocDatas.put("City", location.city);
			geoipLocDatas.put("Country", location.countryName);
			geoipLocDatas.put("Country_Code", location.countryCode);
			geoipLocDatas.put("Latitude", location.latitude);
			geoipLocDatas.put("Longitude", location.longitude);
			geoipLocDatas.put("Region", location.region);
			geoipLocDatas.put("Time_Zone",
					timeZone.timeZoneByCountryAndRegion(
							(String)geoipLocDatas.get("Country_Code"), (String)geoipLocDatas.get("Region")));
			geoipLocDatas.put("Area_Code", location.area_code);
			geoipLocDatas.put("Postal_Code", location.postalCode);
			geoipLocDatas.put("Dma_Code", location.dma_code);
			geoipLocDatas.put("Metro_Code", location.metro_code);

			_latitude  = ((Float) geoipLocDatas.get("Latitude")).doubleValue();
			_longitude = ((Float) geoipLocDatas.get("Longitude")).doubleValue();

			if (_verboseLevel > 1) { // Print the Geoip informations ...
				String format1 = "| %1$-20s | %2$-22s |\n";
				String format2 = "| %1$-20s : %2$-22s |\n";
				String s = "";
				s += String.format("+-----------------------------------------------+\n");
				s += String.format(
						format1,
						Messages.getString("output.GEOIP_DATAS"),
						Messages.getString("output.VALUES"));
				s += String.format("+-----------------------------------------------+\n");
				Set<String> st = geoipLocDatas.keySet();
				Iterator<String> it = st.iterator();
				while(it.hasNext()) {
					String key = it.next();
					s += String.format(format2, Messages.getString(key), geoipLocDatas.get(key));
				}

				s += String.format("+-----------------------------------------------+");
				System.out.println(s);
			}
		} // End of Geoip stuff ...

		// TODO: do not forget to set timezone to either default or automatic from ip address!
		//ComputePrayTimes _computePrayTimes = new ComputePrayTimes(_latitude, _longitude, 1);
		_computePrayTimes.addObserver(this);
		Thread _thread1 = new Thread(_computePrayTimes, "Prayer Times Computer");
		_thread1.start();
		synchronized (stateLock) {
			quiet = true;
			_computePrayTimes.setLatitude(_latitude);
			_computePrayTimes.setLongitude(_longitude);
			quiet = false;
		}

		printPrays();

		// Play the muezzin call at each pray time.
		if (_play) {
			MuezzinCallDaemon muezzinCallDaemon;
			try {
				muezzinCallDaemon = new MuezzinCallDaemon(_prayerTimes);
				_computePrayTimes.addObserver(muezzinCallDaemon);
				muezzinCallDaemon.start();

				if (_verboseLevel > 0) {
					System.out.print("\nMuezzin call daemon informations ...");
					System.out.println(muezzinCallDaemon);
				}

			} catch (BadSizePrayTimesArray e) {
				logger.error("Error while creating/launching muezzin call daemon !!!");
				e.printStackTrace();
			}
		}
		else {
			_computePrayTimes.stop();
		}
	}
	// =======================================================================

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void update(Observable o, Object prayerTimes) {
		_prayerTimes = (ArrayList<String>) prayerTimes;
		if(!quiet)
			printPrays();
	}
	// =======================================================================

	/**
	 * Prints a table containing the prayers times.
	 */
	public void printPrays() {
		int dateStyle = DateFormat.LONG;
		int timeStyle = DateFormat.LONG;
		Locale aLocale = Locale.getDefault();
		synchronized (stateLock) {
		}System.out.println(DateFormat.getDateTimeInstance(dateStyle, timeStyle, aLocale).format(new Date()));
		System.out.println("+=================================+");
		System.out.println(String.format(
				"| %-20s | %-8s |", 
				Messages.getString("output.PRAYS"), 
				Messages.getString("output.TIMES")));
		System.out.println("+=================================+");
		for (int i = 0; i < _prayerTimes.size(); i++)
			System.out.println(String.format("| %-20s : %-8s |",
					ComputePrayTimes.getPrayernames().get(i), _prayerTimes.get(i)));
		System.out.println("+=================================+");

	}
}
