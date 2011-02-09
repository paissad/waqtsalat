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

package net.waqtsalat.configuration;

import jargs.gnu.CmdLineParser;

import java.util.Iterator;
import java.util.Map;

import net.waqtsalat.IpAddress;
import net.waqtsalat.WaqtSalat;

import org.apache.commons.configuration.ConfigurationException;

/**
 * Parse command line and retreive options correctly.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WsParseCommandLine {

	// https://github.com/purcell/jargs/blob/master/src/jargs/examples/gnu/OptionTest.java

	private static String[] _args;
	private static final CmdLineParser parser = new CmdLineParser();
	private WsConfiguration conf;

	private CmdLineParser.Option helpOpt;
	private CmdLineParser.Option debugOpt;
	private CmdLineParser.Option verboseOpt;
	private CmdLineParser.Option confFileNameOpt; // Configuration file.
	private CmdLineParser.Option automaticOpt; // If we want to compute
												// automatically the prayers
												// time without specifying
												// anything.
	private CmdLineParser.Option latOpt; // Latitude to use.
	private CmdLineParser.Option longOpt; // Longitude to use.
	private CmdLineParser.Option ipOpt; // Ip address to use.
	private CmdLineParser.Option siteOpt;
	private CmdLineParser.Option playOpt;
	private CmdLineParser.Option daemonOpt;
	private CmdLineParser.Option serverHostOpt;
	private CmdLineParser.Option serverPortOpt;

	private static boolean _help;
	private static boolean _debug;
	private static Boolean _verbose;
	private static String _confFileName;
	private static boolean _automatic;
	private static double _latitude;
	private static double _longitude;
	private static String _ip;
	private static String _site;
	private static boolean _play;
	private static boolean _daemon;
	private static String _serverHost;
	private static int _serverPort;

	private static int _verboseLevel = 0;

	// =======================================================================

	public WsParseCommandLine(String[] args) {

		assert args != null;
		_args = args;
		String[] argsTmp = _args;

		helpOpt = parser.addBooleanOption('h', "help");
		debugOpt = parser.addBooleanOption('g', "debug");
		verboseOpt = parser.addBooleanOption('v', "verbose");
		confFileNameOpt = parser.addStringOption('c', "conf");
		automaticOpt = parser.addBooleanOption('a', "automatic");
		latOpt = parser.addDoubleOption("lat");
		longOpt = parser.addDoubleOption("long");
		ipOpt = parser.addStringOption('i', "ip");
		siteOpt = parser.addStringOption('s', "site");
		playOpt = parser.addBooleanOption("play");
		daemonOpt = parser.addBooleanOption('d', "daemon");
		serverHostOpt = parser.addStringOption("addr");
		serverPortOpt = parser.addIntegerOption('p', "port");

		// Let's do a 1st parse for the only purpose to get the configuration
		// filename.
		try {
			parser.parse(argsTmp);
		} catch (CmdLineParser.OptionException e) {
			WaqtSalat.logger.error("Error while parsing arguments: '{}'",
					e.getMessage());
			printUsage();
			System.exit(2);
		}
		_confFileName = (String) parser.getOptionValue(confFileNameOpt); // _confFileName
																			// =
																			// null;
																			// if
																			// -c
																			// or
																			// --conf
																			// are
																			// not
																			// set
																			// !
		try {
			conf = new WsConfiguration(_confFileName);
			_confFileName = conf.getConfFileName(); // _confFileName will get
													// default value
													// "waqtsalat.conf" if
													// _confFileName has a
													// previous null value.
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return;
		}

		// Let's do the 2nd parse ...
		try {
			parser.parse(_args);
		} catch (CmdLineParser.OptionException e) {
			WaqtSalat.logger.error("Error while parsing arguments: '{}'",
					e.getMessage());
			printUsage();
			System.exit(2);
		}

		_help = ((Boolean) parser.getOptionValue(helpOpt, Boolean.FALSE))
				.booleanValue();
		_debug = ((Boolean) parser.getOptionValue(debugOpt, Boolean.FALSE))
				.booleanValue();

		// Use the option from command line (if specified), otherwise get the
		// value from configuration file, else use default value.
		_automatic = ((Boolean) parser.getOptionValue(automaticOpt,
				conf.isAutomatic())).booleanValue();
		_latitude = ((Double) parser.getOptionValue(latOpt, conf.getLatitude()))
				.doubleValue();
		_longitude = ((Double) parser.getOptionValue(longOpt,
				conf.getLongitude())).doubleValue();
		_ip = (String) parser.getOptionValue(ipOpt, conf.getIpAddress());
		_site = (String) parser.getOptionValue(siteOpt, conf.getSite());
		_play = ((Boolean) parser.getOptionValue(playOpt, conf.isPlay()))
				.booleanValue();
		_daemon = ((Boolean) parser.getOptionValue(daemonOpt, conf.isDaemon()))
				.booleanValue();
		_serverHost = (String) parser.getOptionValue(serverHostOpt,
				conf.getServerHostname());
		_serverPort = ((Integer) parser.getOptionValue(serverPortOpt,
				conf.getServerPort())).intValue();

		while (true && _verboseLevel < 2) {
			_verbose = (Boolean) parser.getOptionValue(verboseOpt);

			if (_verbose == null)
				break;
			else
				_verboseLevel++;
		}

		// printOptions();
	}

	// =======================================================================

	/**
	 * Mini help about how to use the program.
	 */
	public void printUsage() {
		System.err
				.println(""
						+ "Usage: \n"
						+ "     waqtsalat [-g,--debug] [-v,--verbose] [-a,--automatic] [-c,--conf <confFile>] [-d,--daemon]\n"
						+ "                    [lat <latitude> long <longitude>] [-i,--ip <ipAddress>]\n"
						+ "                    [--addr <serverHost>] [-p,--port <port>] [-s,--site <site>] [-h,--help] \n\n"

						+ "     -h,--help            : Print this help and exit.\n"
						+ "     -v,--verbose         : Enable verbose mode.\n"
						+ "     -g,--debug           : Enable debug mode.\n"
						+ "     --play               : Run the application in backgound in order to play the muezzin call at each pray time.\n"
						+ "     -a,--automatic       : Enable automatic mode, this means that the program will try to \n"
						+ "                            retreive automatically your city via your ip address and then compute the pray times.\n"
						+ "     -c,--conf <confFile> : Configuration file to use for the program.\n"
						+ "     --lat <latitude>     : If you do not want to use the automatic mode, you can manually specify a latitude \n"
						+ "                            and then a longitude in order to compute the pray times.\n"
						+ "     --long <longitude>   : Longitude to use in order to compute the pray times. The latitude has to be specified via --lat option too.\n"
						+ "     -i,--ip <ipAddress>  : Ip address to use in order to compute the pray times.\n"
						+ "                            Thus, no need to specify latitude or longitude or automatic options.\n"
						+ "     -d,--daemon          : Run the application in server mode.\n"
						+ "     --addr <serverHost>  : The ip address of the server if daemon mode is enabled.\n"
						+ "     -p,--port <port>     : The port to use when the application is run in server mode. Default is 6001.\n"
						+ "     -s,--site <site>     : The website to use from where the public ip address is retreived.\n"
						+ "                            The default is "
						+ new IpAddress().getDefaultMethodName() + ".\n"
						+ "                            Available methods are:");
		// Now, let's print the available methods/sites.
		Map<String, String> methods = new IpAddress().getAvailableMethods();
		Iterator<String> it = methods.keySet().iterator();
		while (it.hasNext()) {
			String methodName = it.next();
			System.err.println("" + "                              - "
					+ methodName + " (" + methods.get(methodName) + ")");
		}
		// Now, let's give some examples of use.
		System.err
				.println(""
						+ "      Example of use:\n"
						+ "             waqtsalat\n"
						+ "             waqtsalat -a\n"
						+ "             waqtsalat -c /path/to/my/waqtsalat.conf --daemon --automatic\n"
						+ "             waqtsalat --lat 43.032 --long 76.2223423 --play\n");
	}

	// =======================================================================

	/**
	 * Useful only for debugging ...
	 */
	@SuppressWarnings("unused")
	private void printOptions() {
		System.out.println("" + "\nHelp              : " + _help
				+ "\nDebug             : " + _debug + "\nConfiguration file: "
				+ _confFileName + "\nVerbosity         : " + _verboseLevel
				+ "\nSite to use       : " + _site + "\nPlay              : "
				+ _play + "\nIp address        : " + _ip
				+ "\nlatitude          : " + _latitude
				+ "\nlongitude         : " + _longitude
				+ "\nDaemon            : " + _daemon + "\nServer address    : "
				+ _serverHost + "\nServer port       : " + _serverPort + "\n");

	}

	// =======================================================================

	// GETTERS AND SETTERS --------------------------------------------------

	public String[] getArgs() {
		return _args;
	}

	public void setArgs(String[] args) {
		_args = args;
	}

	public boolean isHelp() {
		return _help;
	}

	public void setHelp(boolean help) {
		_help = help;
	}

	public boolean isDebug() {
		return _debug;
	}

	public void setDebug(boolean debug) {
		_debug = debug;
	}

	public boolean isVerbose() {
		if (_verbose == null)
			return false;
		return _verbose.booleanValue();
	}

	public void setVerbose(boolean verbose) {
		_verbose = verbose;
	}

	public boolean isAutomatic() {
		return _automatic;
	}

	public void setAutomatic(boolean automatic) {
		_automatic = automatic;
	}

	public String getConfFile() {
		return _confFileName;
	}

	public void setConfFile(String confFileName) {
		_confFileName = confFileName;
	}

	public double getLatitude() {
		return _latitude;
	}

	public void setLatitude(double latitude) {
		_latitude = latitude;
	}

	public double getLongitude() {
		return _longitude;
	}

	public void setLongitude(double longitude) {
		_longitude = longitude;
	}

	public String getIp() {
		return _ip;
	}

	public void setIp(String ip) {
		_ip = ip;
	}

	public String getSite() {
		return _site;
	}

	public void setSite(String site) {
		_site = site;
	}

	public boolean isPlay() {
		return _play;
	}

	public void setPlay(boolean play) {
		_play = play;
	}

	public boolean isDaemon() {
		return _daemon;
	}

	public void setDaemon(boolean value) {
		_daemon = value;
	}

	public String getServerAddr() {
		return _serverHost;
	}

	public void setServerAddr(String serverHost) {
		_serverHost = serverHost;
	}

	public int getPort() {
		return _serverPort;
	}

	public void setPort(int port) {
		_serverPort = port;
	}

	public int getVerboseLevel() {
		return _verboseLevel;
	}

	public void setVerboseLevel(int verboseLevel) {
		_verboseLevel = verboseLevel;
	}
	// =======================================================================

}
