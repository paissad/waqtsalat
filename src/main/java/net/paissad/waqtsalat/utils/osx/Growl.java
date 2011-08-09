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

package net.paissad.waqtsalat.utils.osx;

import java.io.IOException;
import java.util.Arrays;

import net.paissad.waqtsalat.utils.CommonUtils;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class Growl {

    /**
     * The path where is located the AppleScript script to run.
     */
    private static final String GROWL_NOTIFIER_SCRIPT = "macosx/growlNotifier.applescript";
    private static final String OSASCRIPT_COMMAND     = "osascript";

    // =========================================================================

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

    // =========================================================================

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
    public int sendNotification(String appName, String message, String windowTitle) throws
            IOException, InterruptedException {

        Process p = null;
        int exitStatus = -1;
        try {
            String[] command = {
                    OSASCRIPT_COMMAND,
                    GROWL_NOTIFIER_SCRIPT,
                    appName,
                    message,
                    windowTitle
            };

            ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
            pb.directory(null);
            p = pb.start();
            exitStatus = p.waitFor();
            return exitStatus;

        } finally {
            if (p != null && exitStatus != 0) {
                CommonUtils.printErrorProcess(p);
            }
            p = null;
        }
    }

    // =========================================================================

    /*
     * For testing purpose only ! XXX
     */
    @Deprecated
    public static void main(String[] args) throws Exception {
        Growl growl = new Growl();
        growl.sendNotification("TextEdit", "test message for Growl from Java");
    }
}
