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

package net.waqtsalat.utils.osx;

import java.io.IOException;

import static net.waqtsalat.utils.Utils.printErrorProcess;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Growl {

	private static final String GROWL_NOTIFIER_SCRIPT = "macosx/growlNotifier.applescript";

	// =======================================================================

	/**
	 * Sends a notification to Growl where the window's title is as the same as
	 * the name of the application.
	 * 
	 * @param appName
	 *            The name of the application.
	 * @param message
	 * @return An <code>int</code> which is the exit status of the command
	 *         executed ! By convention, a value of 0 means that the command
	 *         terminated successfully, and failed otherwise.
	 * @see #sendNotification(String, String, String) The message to display.
	 * @throws Exception
	 * 
	 */
	public int sendNotification(String appName, String message) throws Exception {
		return sendNotification(appName, message, appName);
	}

	// =======================================================================

	/**
	 * Sends a notification to Growl.
	 * 
	 * @param appName
	 *            The name of the application.
	 * @param message
	 *            The message to display.
	 * @param windowTitle
	 *            The title of the window.
	 * @return An <code>int</code> which is the exit status of the command
	 *         executed ! By convention, a value of 0 means that the command
	 *         terminated successfully, and failed otherwise.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int sendNotification(String appName, String message, String windowTitle) throws IOException, InterruptedException {
		Process p = null;
		int exitStatus = -1;
		try {
			String[] command = { 
					"osascript",
					GROWL_NOTIFIER_SCRIPT,
					appName,
					message,
					windowTitle
			};
			p  = Runtime.getRuntime().exec(command, null, null);
			exitStatus = p.waitFor();
			return exitStatus;
		}
		finally {
			if ( exitStatus != 0 && p != null ) {
				printErrorProcess(p);
			}
			p = null;
		}
	}

	// =======================================================================

}
