package com.user.app.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *  A logger class to write the values in log file.
 *
 */
public class AppLogger {

	final static String GLOBAL_LOGGER_NAME = "usermanagement";
	final static Logger logger = LogManager.getLogger(GLOBAL_LOGGER_NAME);

	public static Logger getLogger() {
		return logger;
	}

}

