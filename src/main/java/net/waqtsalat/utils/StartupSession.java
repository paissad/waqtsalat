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

package net.waqtsalat.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

/**
 * Contains some convenience methods to start the application during login.<br>
 * Currently, it works only for MacOSX and on some Windows systems too.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class StartupSession {

    private static final String APP_PATH_MACOSX = "/Applications/WaqtSalat.app";
    private static final String APP_PATH_WIN32  = "";
    
    private static Logger logger = LoggerFactory.getLogger(StartupSession.class);

    // ======================================================================

    /**
     * This class contains some utilities that permit to start the application
     * at login. It's not guaranteed that it will work for all operating
     * systems, but it should at least work for some Windows OSes, on MacOSX and
     * maybe on some Linux distributions. *
     * 
     * @see #removeStartupAtLogin()
     * @throws InterruptedException
     * @throws IOException
     */

    public static void addStartupAtLogin() throws IOException, InterruptedException {
        if (Platform.isMac()) {
            startAppAtLogin_MACOSX();
        } else if (Platform.isWindows()) {
            startAppAtLogin_WIN32();
        } else {
            logger.error("Login startup feature not supported for this system !");
        }
    }

    // ======================================================================

    /**
     * Remove the application from the startup if the it was already registered
     * to start at login.
     * 
     * @see #addStartupAtLogin()
     * @throws InterruptedException
     * @throws IOException
     */
    public static void removeStartupAtLogin() throws IOException, InterruptedException {
        if (Platform.isMac()) {
            removeLoginStartup_MACOSX();
        } else if (Platform.isWindows()) {
            removeLoginStartup_WIN32();
        } else {
            // Not supported ...
        }
    }

    // ======================================================================
    // @formatter:off
    // MACOSX:
    /*
     * NOTE FOR MACOSX:
     * 
     * We are using an simple AppleScript line, but it was also possible to use
     * the MacOSX command 'defaults'. But, since the manpage of this last one
     * emitted a warning saying that we should not use 'defaults' command
     * because it will be depreciated in future release of Apple Mac Oses.
     * 
     * /usr/bin/defaults write loginwindow AutoLaunchedApplicationDictionary-array-add '{Hide=1;Path="/Applications/WaqtSalat.app";}'
     * 
     * Info: here is the link where i grabbed the tip:
     * http://www.macgeekery.com/tips/cli/adding_items_to_login_items_from_the_cli
     * 
     * Paissad,
     */// @formatter:on

    private static void startAppAtLogin_MACOSX() throws IOException, InterruptedException {
        Process p = null;
        try {
            //@formatter:off
            String[] command = {
                    "/usr/bin/osascript",
                    "-e", "tell application \"System Events\"\n",
                    "-e", "if \"" + APP_PATH_MACOSX + "\" is not in (path of every login item) then\n",
                    "-e", "make new login item with properties {path:\"" + APP_PATH_MACOSX + "\", hidden:false} at end\n",
                    "-e", "end if\n",
                    "-e", "end tell\n"
            };
            // @formatter:on

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(null);
            p = pb.start();
            int exitStatus = p.waitFor();
            if (p != null && exitStatus != 0) {
                logger.warn("It seems like the creation of login startup property failed, the exit status is ({}).",
                        exitStatus);
                logger.warn("Please check into your system settings if the application is present in startup items ...");
                CommonUtils.printErrorProcess(p);
            } else {
                logger.info("Now, the application will start at login.");
            }
        } finally {
            p = null;
        }
    }

    private static void removeLoginStartup_MACOSX() throws IOException,
            InterruptedException {
        Process p = null;
        try {
            //@formatter:off
            String command[] = {
                    "/usr/bin/osascript",
                    "-e", "tell application \"System Events\"\n",
                    "-e", "if \"" + APP_PATH_MACOSX + "\" is in (path of every login item) then\n",
                    "-e", "delete (every login item whose path is \"" + APP_PATH_MACOSX + "\")\n",
                    "-e", "end if\n",
                    "-e", "end tell\n"
            };
            // @formatter:on
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(null);
            p = pb.start();
            int exitStatus = p.waitFor();
            if (p != null && exitStatus != 0) {
                logger.warn("It seems like the removal of startup property failed, the exit status is ({})", exitStatus);
                CommonUtils.printErrorProcess(p);
            } else {
                logger.info("The application is removed from startup items at login.");
            }
        } finally {
            p = null;
        }
    }

    // ======================================================================
    // @formatter:off
    // WINDOWS:
    /*
     * http://technet.microsoft.com/en-us/magazine/ee851671.aspx
     * http://stackoverflow.com/questions/3599444/windows-registery-how-to-add-a-java-app-to-startup-list
     */// @formatter:on

    private static void startAppAtLogin_WIN32() {
        // Run Key (user)
        String registryKey_1 = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
        String registryKey_2 = "HKCU\\Software\\Microsoft\\Windows NT\\CurrentVersion\\Windows\\Run";
    }

    private static void removeLoginStartup_WIN32() {
        // TODO: implement if for windows ...
    }

    // ======================================================================

}
