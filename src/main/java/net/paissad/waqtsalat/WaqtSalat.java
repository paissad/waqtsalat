/*
 * WaqtSalat, for indicating the muslim prayers times in most cities.
 * Copyright (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat;

import static net.paissad.waqtsalat.WSConstants.EXIT_ERROR;
import static net.paissad.waqtsalat.WSConstants.EXIT_SUCCESS;

import java.io.File;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.logging.LogColorConverter;
import net.paissad.waqtsalat.logging.LogDirDefiner;
import net.paissad.waqtsalat.logging.LogFileNameDefiner;
import net.paissad.waqtsalat.logging.LogReloader;

public class WaqtSalat {

    private static Logger       logger     = LoggerFactory.getLogger(WaqtSalat.class);

    private static File         logfile    = null;
    private static File         configFile = null;
    private static List<String> arguments  = null;

    // _________________________________________________________________________

    public static void main(String[] args) {

        processOptionsAndArguments(args);
        
        logger.trace("trace ...");
        logger.debug("debug ...");
        logger.info("info ...");
        logger.warn("warn ...");
        logger.error("error ...");
    }

    // _________________________________________________________________________

    /**
     * Reads the options and arguments specified from the command line.
     * 
     * @param args
     *            - The command line arguments.
     */
    private static void processOptionsAndArguments(String... args) {
        WSOptions options = new WSOptions();
        CmdLineParser cmdParser = new CmdLineParser(options);

        try {
            cmdParser.parseArgument(args);

            // If the '-h, --help' option is specified.
            printHelpIfSpecifiedAndExit(options);

            // If the '-v, --version' options is specified.
            printVersionIfSpecifiedAndExit(options);

            // If '--color' is specified, the STDOUT output will be colorized.
            LogColorConverter.setUseColor(options.isColor());

            File logfileFromCmdLine = options.getLogfile();
            if (logfileFromCmdLine != null) {
                setLogfile(logfileFromCmdLine);
            }

            File confFileFromCmdLine = options.getConfigFile();
            if (confFileFromCmdLine != null) {
                setConfigFile(confFileFromCmdLine);
            }

            List<String> argsFromCmdLine = options.getArguments();
            if (argsFromCmdLine != null) {
                for (String arg : argsFromCmdLine)
                    System.out.println(arg); // XXX
                setArguments(argsFromCmdLine);
            }

        } catch (CmdLineException cle) {
            /*
             * If the '-v' or '-h' options are specified, a CmlLineException
             * should not be thrown for the only purpose that a required option
             * such as '-c' is not specified.
             */
            printHelpIfSpecifiedAndExit(options);
            printVersionIfSpecifiedAndExit(options);
            /*
             * At this step, no '-h' or '-v' options was specified while the
             * CmdLineException was thrown ... so we must print the error
             * message to STDERR and the help to STDOUT !
             */
            System.err.println(cle.getMessage());
            options.printHelpToStdout();
            System.exit(EXIT_ERROR);
        }
    }

    private static void printHelpIfSpecifiedAndExit(WSOptions options) {
        if (options.isHelp()) {
            options.printHelpToStdout();
            System.exit(EXIT_SUCCESS);
        }
    }

    private static void printVersionIfSpecifiedAndExit(WSOptions options) {
        if (options.isVersion()) {
            options.printVersionToStdout();
            System.exit(EXIT_SUCCESS);
        }
    }

    // _________________________________________________________________________

    private static void updatePathOfLogFile() {
        File f = getLogfile();
        if (f != null) {
            File logDir = f.getParentFile();
            logDir = (logDir == null) ? new File(".") : logDir;
            String logFileName = f.getName();
            LogDirDefiner.setLogDir(logDir);
            LogFileNameDefiner.setLogFileName(logFileName);
            LogReloader.reload();
        }
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    private static void setLogfile(File logfile) {
        WaqtSalat.logfile = logfile;
    }

    private static File getLogfile() {
        return logfile;
    }

    private static void setConfigFile(File configFile) {
        WaqtSalat.configFile = configFile;
    }

    private static File getConfigFile() {
        return configFile;
    }

    private static void setArguments(List<String> arguments) {
        WaqtSalat.arguments = arguments;
    }

    private static List<String> getArguments() {
        return arguments;
    }

    // _________________________________________________________________________

    private static void init() {
        ShutdownHook hook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(hook);
    }

    // _________________________________________________________________________

    /**
     * This hook (thread) should contain all processes that should be launched
     * during the shutdown of the application.
     * 
     * @author Papa Issa DIAKHATE (paissad)
     * 
     */
    private static class ShutdownHook extends Thread {
        @Override
        public void run() {
            logger.info("Freeing resources ...");
            System.gc();
            logger.info("Bye bye !");
        }
    }

    // _________________________________________________________________________

}
