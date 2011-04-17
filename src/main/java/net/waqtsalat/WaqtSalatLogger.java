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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.slf4j.LoggerFactory;

/**
 * Creates dynamically a logger.<br>
 * Got inspiration from this <a href=
 * "http://robertmaldon.blogspot.com/2007/09/programmatically-configuring-log4j-and.html"
 * >link</a>.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WaqtSalatLogger {

	private static Level _level;
	private static String _pattern;
	private static boolean _appendToLog;

	// ======================================================================

	static {
		_level = Level.DEBUG;
		// http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/EnhancedPatternLayout.html
		_pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p - [%t] (%F: %M: %-4L) - %m%n";
		_appendToLog = false;
	}

	// ======================================================================

	/**
	 * Initialisation of the the logger using the <code>WARN</code>
	 * {@link Level}. This method should be called before the creation of an
	 * logger (via SLF4J example).
	 * 
	 * @param logFileName
	 * 
	 * @return The logger created.
	 */
	public static org.slf4j.Logger initLog4j(String logFileName) {
		return initLog4j(_level, logFileName);
	}

	// ======================================================================

	/**
	 * Initialisation of the the logger using the specified {@link Level}. This
	 * method should be called before the creation of an logger (via SLF4J
	 * example).
	 * 
	 * @param level
	 *            The level to use when creating the logger.
	 * @param logFileName
	 * @return The logger created.
	 */
	public static org.slf4j.Logger initLog4j(Level level, String logFileName) {
		Logger rootLogger = Logger.getRootLogger();

		if (!rootLogger.getAllAppenders().hasMoreElements()) {
			rootLogger.setLevel(level); // console
			rootLogger.addAppender(
					new ConsoleAppender(
							new EnhancedPatternLayout(
									_pattern), "System.out"));
			try {
				rootLogger.addAppender( // file
						new FileAppender(
								new EnhancedPatternLayout(_pattern), logFileName, _appendToLog));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.err.println("Already has at least one logger ...");
		}
		org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(WaqtSalatLogger.class);
		return slf4jLogger;

		/*
		This is the equivalent of creating and putting the file log4j.properties whose content
		would be something like this:

		-------------------------------------------------------------------------------------------------------------
		log4j.rootLogger=WARN, logFile

		log4j.appender.stdout=org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.layout=org.apache.log4j.EnhancedPatternLayout
		log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p - [%t] (%F: %M: %-4L) - %m%n

		log4j.appender.logFile=org.apache.log4j.FileAppender
		log4j.appender.logFile.File=waqtsalat.log
		log4j.appender.logFile.layout=org.apache.log4j.EnhancedPatternLayout
		log4j.appender.logFile.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p - [%t] (%F: %M: %-4L) - %m%n
		-------------------------------------------------------------------------------------------------------------
		 */
	}
}
