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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.waqtsalat.MuezzinCallDaemon.BadSizePrayTimesArray;
import net.waqtsalat.configuration.WsParseCommandLine;
import net.waqtsalat.utils.GeoipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

public class WaqtSalat {

	String defaultCityName;
	IpAddress ipAddress = null;
	Location loc = null;
	public static Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	public static void main(String[] args) throws IOException {

		WsParseCommandLine parser = new WsParseCommandLine(args);
		boolean _help = parser.isHelp();
		@SuppressWarnings("unused")
		// TODO
		boolean _verbose = parser.isVerbose();
		int _verboseLevel = parser.getVerboseLevel();
		boolean _auto = parser.isAutomatic();
		String _ip = parser.getIp();
		double _latitude = parser.getLatitude();
		double _longitude = parser.getLongitude();
		boolean _play = parser.isPlay();

		// TODO: remove this ...
		// System.out.println("longitude -> " + _longitude);
		// System.out.println("latitude ->  " + _latitude);

		String cityName;
		String countryName;
		String region;
		String countryCode;
		String tmz;
		int areaCode;
		String postalCode;
		int dmaCode;
		int metroCode;

		ArrayList<String> prayerTimes = new ArrayList<String>();
		ArrayList<String> prayerNames = new ArrayList<String>();

		// Print the help if is specified and then exit.
		if (_help) {
			parser.printUsage();
			System.exit(0);
		}
		// TODO: remove this ...
		/*
		 * if (_verbose) System.out.println("Verbosity level: " +
		 * _verboseLevel);
		 */

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
				logger.info("Automatic public ip address ...");
				_ip = new IpAddress().retreiveIpAddress();
				logger.debug("Ip address: {}", _ip);
				if (_ip.equals("-1"))
					logger.error("The ip address cannot be '-1', an error occured.");
			}

			Location location = new LookupService(
					GeoipUtils.GEOIP_DATABASE_COMPLETE_PATH).getLocation(_ip);

			cityName = location.city;
			countryName = location.countryName;
			_latitude = location.latitude;
			_longitude = location.longitude;
			region = location.region;
			countryCode = location.countryCode;
			tmz = timeZone.timeZoneByCountryAndRegion(countryCode, region);
			areaCode = location.area_code;
			postalCode = location.postalCode;
			dmaCode = location.dma_code;
			metroCode = location.metro_code;

			// TODO: Do not forget to remove this since it's only for debugging
			// purpose.
			if (_verboseLevel > 1)
				System.out.println(""
						+ "\n--------------------------------------"
						+ "\n| Ip Address   = "
						+ _ip
						+ "\n| City         = "
						+ cityName
						+ "\n| Country      = "
						+ countryName
						+ "\n| Country Code = "
						+ countryCode
						+ "\n| Latitude     = "
						+ _latitude
						+ "\n| Longitude    = "
						+ _longitude
						+ "\n| Region       = "
						+ region
						+ "\n| Timezone     = "
						+ tmz
						+ "\n| Area Code    = "
						+ areaCode
						+ "\n| Postal Code  = "
						+ postalCode
						+ "\n| Dma Code     = "
						+ dmaCode
						+ "\n| Metro Code   = "
						+ metroCode
						+ "\n--------------------------------------" + "\n");
			// End of TODO
		}

		double timezone = 1;
		// Test Prayer times here
		PrayTime prayers = new PrayTime();

		prayers.setTimeFormat(prayers.getTime24());
		prayers.setCalcMethod(prayers.getJafari());
		prayers.setAsrJuristic(prayers.getShafii());
		prayers.setAdjustHighLats(prayers.getAngleBased());
		int[] offsets = { 0, 0, 0, 0, 0, 0, 0 }; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
		prayers.tune(offsets);

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		prayerTimes = prayers.getPrayerTimes(cal, _latitude, _longitude,
				timezone);
		prayerNames = prayers.getTimeNames();

		System.out.println("+=========================+");
		System.out.println(String.format("| %-14s|%8s |", "Prays", "Times"));
		System.out.println("+=========================+");
		for (int i = 0; i < prayerTimes.size(); i++)
			System.out.println(String.format("| %-14s:%8s |",
					prayerNames.get(i), prayerTimes.get(i)));
		System.out.println("+=========================+");

		// Play the muezzin call at each pray time.
		if (_play) {
			MuezzinCallDaemon muezzinCallDaemon;
			try {
				MuezzinCallDaemon.setTimes(prayerTimes);
				muezzinCallDaemon = new MuezzinCallDaemon(prayerTimes);
				muezzinCallDaemon.start();

				if (_verboseLevel > 0)
					System.out.println(muezzinCallDaemon);
			} catch (BadSizePrayTimesArray e) {
				logger.error("Error while creating/launching muezzin call daemon !!!");
				e.printStackTrace();
			}
		}

		// TODO
		logger.info("BLAH BLAH ...");
		// System.exit(0);
	}

	// =======================================================================

	public WaqtSalat(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	// =======================================================================

	public WaqtSalat(Location loc) {
		this.loc = loc;
	}

	// =======================================================================

	public WaqtSalat(IpAddress ipAddress, Location loc) {
		this.ipAddress = ipAddress;
		this.loc = loc;
	}
	// =======================================================================

}
