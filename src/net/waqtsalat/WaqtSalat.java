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

import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.timeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaqtSalat {

	public static final String GEOIP_DATABASE_FILE = "GeoLiteCity.dat";
	public static final int GEOIP_OPTIONS = LookupService.GEOIP_MEMORY_CACHE;

	Logger logger = LoggerFactory.getLogger(getClass());

	IpAddress ipAddress = null;
	Location loc        = null;

	public WaqtSalat(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public WaqtSalat(Location loc) {
		this.loc = loc;
	}

	public WaqtSalat(IpAddress ipAddress, Location loc) {
		this.ipAddress = ipAddress;
		this.loc       = loc;
	}

	//	public WaqtSalat(String ip, File geoipDatabaseFile) {
	//		this.ip = ip;
	//		try{
	//			final LookupService service = new LookupService(geoipDatabaseFile);
	//		}
	//		catch(IOException ioe) { ioe.getMessage(); }
	//	}

	public static void main(String[] args) throws IOException {

		//WaqtSalat ws = new WaqtSalat(ipAddress,location);

		String ip          = new IpAddress().retreiveIpAddress();
		Location location  = new LookupService(GEOIP_DATABASE_FILE).getLocation(ip);

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

		Location attacker = new LookupService(GEOIP_DATABASE_FILE).getLocation("81.251.93.229");

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


		//		double latitude = -37.823689;
		//		double longitude = 145.121597;
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

		ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
				latitude, longitude, timezone);
		ArrayList<String> prayerNames = prayers.getTimeNames();

		for (int i = 0; i < prayerTimes.size(); i++) {
			System.out.println(prayerNames.get(i) + "\t: " + prayerTimes.get(i));
		}
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

}


