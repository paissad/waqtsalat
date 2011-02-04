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

import net.waqtsalat.MuezzinCallDaemon.BadSizePrayTimesArray;
import net.waqtsalat.PrayTime;
import net.waqtsalat.IpAddress;
import net.waqtsalat.SimplePlayer;
import net.waqtsalat.utils.DownloadUtils;
import net.waqtsalat.utils.GeoipUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.timeZone;

import jargs.gnu.CmdLineParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaqtSalat {

	public static final String GEOIP_DATABASE_FILE = "GeoLiteCity.dat";
	public static final int GEOIP_OPTIONS = LookupService.GEOIP_MEMORY_CACHE;
	public static final double LATITUDE_MAKKAH  = 21.42738;
	public static final double LONGITUDE_MAKKAH = 39.81484;

	String defaultCityName;
	IpAddress ipAddress = null;
	Location loc        = null;

	public static void main(String[] args) throws IOException {

		Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

		// https://github.com/purcell/jargs/blob/master/src/jargs/examples/gnu/OptionTest.java

		CmdLineParser parser            = new CmdLineParser();
		CmdLineParser.Option help       = parser.addBooleanOption('h', "help");
		CmdLineParser.Option debug      = parser.addBooleanOption('g', "debug");
		CmdLineParser.Option verbose    = parser.addBooleanOption('v', "verbose");
		CmdLineParser.Option automatic  = parser.addBooleanOption('a', "automatic"); // If we want to compute automatically the prayers time without specifying anything.
		CmdLineParser.Option confFile   = parser.addStringOption('c', "conf");  // Configuration file.
		CmdLineParser.Option latArg     = parser.addDoubleOption("lat");        // Latitude to use.
		CmdLineParser.Option longArg    = parser.addDoubleOption("long");       // Longitude to use.
		CmdLineParser.Option ipArg      = parser.addStringOption('i', "ip");    // Ip address to use.
		CmdLineParser.Option siteArg    = parser.addStringOption('s', "site");
		CmdLineParser.Option daemonArg  = parser.addBooleanOption('d', "daemon");

		try {
			parser.parse(args);
		}
		catch ( CmdLineParser.OptionException e ) {
			logger.error("Error while parsing arguments: '{}'", e.getMessage());
			printUsage();
			System.exit(2);
		}

		Boolean helpValue            = (Boolean) parser.getOptionValue(help, Boolean.FALSE);
		Boolean debugValue           = (Boolean) parser.getOptionValue(debug, Boolean.FALSE);
		Boolean automaticValue       = (Boolean) parser.getOptionValue(automatic, Boolean.TRUE);
		String confFileValue         = (String) parser.getOptionValue(confFile, null);
		Double latitudeValue         = (Double) parser.getOptionValue(latArg, null);
		Double longitudeValue        = (Double) parser.getOptionValue(longArg, null);
		String ipValue               = (String) parser.getOptionValue(ipArg, null);
		String siteValue             = (String) parser.getOptionValue(siteArg, null);
		Boolean daemonValue          = (Boolean) parser.getOptionValue(daemonArg, Boolean.FALSE);

		int verbosity = 0;
		while (true) {
			Boolean verboseValue = (Boolean)parser.getOptionValue(verbose);

			if (verboseValue == null)
				break;
			else
				if(verbosity < 2)   // We set the max verbosity level to 2.
					verbosity++;
				else
					break;
		}

		System.out.println("Help              : " + helpValue);
		System.out.println("Debug             : " + debugValue);
		System.out.println("Configuration file: " + confFileValue);
		System.out.println("verbosity         : " + verbosity);
		System.out.println("Site to use       : " + siteValue);
		System.out.println("Daemon            : " + daemonValue);

		// Print the help if it is specified and then exit.
		if(helpValue) {
			printUsage();
			System.exit(0);
		}

		ArrayList<String> prayerTimes = new ArrayList<String>();
		ArrayList<String> prayerNames = new ArrayList<String>();

		if(automaticValue) {
			logger.info("Automatic computing ...");
			System.out.println("what ? ...");

			String ip          = new IpAddress().retreiveIpAddress();
			logger.debug("Ip address: {}", ip);
			if(ip == "-1")
				logger.error("The ip address cannot be '-1', an error occured.");

			Location location  = new LookupService(GeoipUtils.GEOIP_DATABASE_COMPLETE_PATH).getLocation(ip);

			String cityName    = location.city;
			String countryName = location.countryName; 
			double latitude    = location.latitude;
			double longitude   = location.longitude;
			String region      = location.region;
			String countryCode = location.countryCode;
			String tmz         = timeZone.timeZoneByCountryAndRegion(countryCode, region);
			int areaCode       = location.area_code;
			String postalCode  = location.postalCode;
			int dmaCode        = location.dma_code;
			int metroCode      = location.metro_code;		

			Location attacker = new LookupService(GeoipUtils.GEOIP_DATABASE_COMPLETE_PATH).getLocation("81.251.93.229");

			System.out.println(
					"\nIp Address   = " + ip +
					"\nCity         = " + cityName +
					"\nCountry      = " + countryName +
					"\nCountry Code = " + countryCode +
					"\nLatitude     = " + latitude +
					"\nLongitude    = " + longitude +
					"\nRegion       = " + region +
					"\nTimezone     = " + tmz +
					"\nArea Code    = " + areaCode +
					"\nPostal Code  = " + postalCode +
					"\nDma Code     = " + dmaCode +
					"\nMetro Code   = " + metroCode +
					"\n---------------------------" +
					"\nDistance     = " + location.distance(attacker) +
					"\n"
			);

			double timezone = 1;
			// Test Prayer times here
			PrayTime prayers = new PrayTime();

			prayers.setTimeFormat(prayers.getTime24());
			prayers.setCalcMethod(prayers.getJafari());
			prayers.setAsrJuristic(prayers.getShafii());
			prayers.setAdjustHighLats(prayers.getAngleBased());
			int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
			prayers.tune(offsets);

			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);

			prayerTimes = prayers.getPrayerTimes(cal, latitude, longitude, timezone);
			prayerNames = prayers.getTimeNames();

			for (int i = 0; i < prayerTimes.size(); i++) {
				System.out.println(prayerNames.get(i) + "\t: " + prayerTimes.get(i));
			}
		}// End of "automatic" stuff.

		// OVERWRITE THE MUEZZIN CALL TIME LIST FOR TESTING PURPOSE !
		//prayerTimes.set(0, "17:56");
		//prayerTimes.set(2, "17:58");

		// Play the muezzin call at each pray time.
		if(daemonValue) {
			MuezzinCallDaemon muezzinCallDaemon;
			try {
				MuezzinCallDaemon.setTimes(prayerTimes);
				muezzinCallDaemon = new MuezzinCallDaemon(prayerTimes);
				muezzinCallDaemon.start();

				System.out.println(prayerTimes.toString());
				System.out.println("++++++++++++++++++++++++++++++++++++++++");
				System.out.println(muezzinCallDaemon);
				System.out.println("++++++++++++++++++++++++++++++++++++++++");

				prayerTimes.set(0, "17:56");
				prayerTimes.set(2, "17:58");

				MuezzinCallDaemon.setTimes(prayerTimes);
				System.out.println(prayerTimes.toString());
				/*
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {}
				*/
				System.out.println("++++++++++++++++++++++++++++++++++++++++");
				System.out.println(muezzinCallDaemon);
				System.out.println("++++++++++++++++++++++++++++++++++++++++");


			}catch (BadSizePrayTimesArray e) {
				logger.error("Error while creating/launching muezzin call daemon !!!");
				e.printStackTrace();
			}
		}

		logger.info("BLAH BLAH ...");
		//System.exit(0);

	}
	//=======================================================================

	public WaqtSalat(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	//=======================================================================

	public WaqtSalat(Location loc) {
		this.loc = loc;
	}
	//=======================================================================

	public WaqtSalat(IpAddress ipAddress, Location loc) {
		this.ipAddress = ipAddress;
		this.loc       = loc;
	}
	//=======================================================================

	/**
	 * Mini help about how to use the program.
	 */
	private static void printUsage() {
		System.err.println("" +
				"Usage: \n" +
				"     waqtsalat [-g,--debug] [-v,--verbose] [-a,--automatic] [-c,--conf <confFile>] [-d,--daemon]\n" +
				"                    [lat <latitude> long <longitude>] [-i,--ip <ipAddress>] [-h,--help] \n\n" +

				"     -h,--help            : Print this help and exit.\n" +
				"     -v,--verbose         : Enable verbose mode.\n" +
				"     -g,--debug           : Enable debug mode.\n" +
				"     -d,--daemon          : Enable daemon mode. Thus, play the muezzin call at each pray time.\n" +
				"     -a,--automatic       : Enable automatic mode, this means that the program will try to \n" +
				"                            retreive automatically your city via your ip address and then compute the pray times.\n" +
				"     -c,--conf <confFile> : Configuration file to use for the program.\n" +
				"     --lat <latitude>     : If you do not want to use the automatic mode, you can manually specify a latitude \n" +
				"                            and then a longitude in order to compute the pray times.\n" +
				"     --long <longitude>   : longitude to use in order to compute the pray times. The latitude has to be specified via --lat option too.\n" +
				"     -i,--ip <ipAddress>  : Ip address to use in order to compute the pray times.\n" +
				"                            Thus, no need to specify latitude or longitude or automatic options.\n" +
				"     -s,--site <site>     : The website to use from where the public ip address is retreived.\n" +
				"                            The default is DYNDNS.\n" +
				"                            Available methods are:"
		);
		// Now, let's print the available methods/sites.
		Map<String, String> methods = new IpAddress().getAvailableMethods();
		Iterator<String> it = methods.keySet().iterator();
		while(it.hasNext()) {
			String methodName = it.next();
			System.err.println("" +
					"                              - " + methodName + " (" + methods.get(methodName) + ")"
			);
		}
		// Now, let's give some examples of use.
		System.err.println("" +
				"      Example of use:\n" +
				"             waqtsalat\n" +
				"             waqtsalat -a\n" +
				"             waqtsalat -c /path/to/my/waqtsalat.conf --daemon --automatic\n"
		);
	}

}


