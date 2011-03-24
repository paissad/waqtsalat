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

package net.waqtsalat.utils;

import static net.waqtsalat.WaqtSalat.logger;
import static net.waqtsalat.utils.Utils.printErrorProcess;

import java.io.IOException;

/**
 * This class contains some convenience methods to start the application during login.<br>
 * Currently, it works only for MacOSX, maybe the Windows System will be added in the future too !
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class StartupUtils {

	private static final String appPath_MACOSX = "/Applications/WaqtSalat.app";
	//private static final String appPath_WIN32  = ""; // FIXME: to implement ...

	/**
	 * This method allow the application to start at login (obviously, it does
	 * not work on all systems !)
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void startAppAtLogin() throws IOException, InterruptedException {
		Utils sys = new Utils();
		if (sys.isMac()) {
			startAppAtLogin_MACOSX();
		} else if (sys.isWindows()) {
			startAppAtLogin_WIN32();
		} else {
			// Not possible for other operating systems :/
			logger.error("Login startup feature not supported in this system !");
		}
	}

	/**
	 * Remove the application from the startup if the it was already registered
	 * to start at login.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void removeStartupLogin() throws IOException, InterruptedException {
		Utils sys = new Utils();
		if (sys.isMac()) {
			removeLoginStartup_MACOSX();
		} else if (sys.isWindows()) {
			removeLoginStartup_WIN32();
		} else {
			// Not supported ...
		}
	}

	// ======================================================================

	/*
	 * NOTE FOR MACOSX:
	 * 
	 * We are using an simple AppleScript line, but it was also possible
	 * to use the MacOSX command 'defaults'. But, since the manpage of this last one emitted
	 * a warning saying that we should not use 'defaults' command because it will
	 * be depreciated in future release of Apple Mac Oses.
	 * 
	 * /usr/bin/defaults write loginwindow AutoLaunchedApplicationDictionary -array-add '{Hide=1;Path="/Applications/WaqtSalat.app";}'
	 * 
	 * Info: here is the link where i grabbed the tip:
	 * http://www.macgeekery.com/tips/cli/adding_items_to_login_items_from_the_cli
	 * 
	 * Paissad, 
	 */

	private static void startAppAtLogin_MACOSX() throws IOException, InterruptedException {
		Process p = null;
		try {
			String[] command = {
					"/usr/bin/osascript",
					"-e", "tell application \"System Events\"\n",
					"-e", "if \"" + appPath_MACOSX + "\" is not in (path of every login item) then\n",
					"-e", "make new login item with properties {path:\"" + appPath_MACOSX + "\", hidden:false} at end\n",
					"-e", "end if\n",
					"-e", "end tell\n"
			};

			p = Runtime.getRuntime().exec(command, null, null);
			int exitStatus =  p.waitFor();
			if (exitStatus != 0) {
				logger.warn("It seems like the creation of login startup property failed, the exit status is ({}).", exitStatus);
				logger.warn("Please check into your system settings if the application is present in startup items ...");
				printErrorProcess(p);
			} else {
				logger.info("Now, the application will start at login.");
			}
		}
		finally {
			p = null;
		}
	}

	// ======================================================================

	private static void removeLoginStartup_MACOSX() throws IOException, InterruptedException {
		Process p = null;
		try {
			String command[] = {
					"/usr/bin/osascript",
					"-e", "tell application \"System Events\"\n",
					"-e", "if \"" + appPath_MACOSX + "\" is in (path of every login item) then\n",
					"-e", "delete (every login item whose path is \"" + appPath_MACOSX + "\")\n",
					"-e", "end if\n",
					"-e", "end tell\n"
			};
			p = Runtime.getRuntime().exec(command, null, null);
			int exitStatus = p.waitFor();
			if (exitStatus != 0) {
				logger.warn("It seems like the removal of startup property failed, the exit status is ({})", exitStatus);
				printErrorProcess(p);
			} else {
				logger.info("The application is removed from startup items at login.");
			}
		}
		finally {
			p = null;
		}
	}

	// ======================================================================

	private static void startAppAtLogin_WIN32() {
		// TODO: implement it for windows ...
	}

	// ======================================================================

	private static void removeLoginStartup_WIN32() {
		// TODO: implement if for windows ...
	}

	// ======================================================================

}
