package com.blizzard.api.utils;

import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class is used to log in console and file
 * 
 */
public class LogUtils {
	private static final Logger logger;

	static {
		logger = Logger.getLogger("LogUtil");
	}

	private static String LOG4J_XML;

	public LogUtils(String logLoc) {
		LOG4J_XML = logLoc + "log4j.xml";
		System.out.println("Configuring log4j...");
		try {
			DOMConfigurator.configure(LOG4J_XML);
		} catch (FactoryConfigurationError fExep) {
			System.out.println(" Error while Configurating log4j.");
		} catch (Exception fileExep) {
			System.out.println(LOG4J_XML + " file could be missing.");
		}
	}

	/**
	 * Returns the Logger
	 * 
	 * @param name
	 *            String
	 * @return Logger Logger
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name);
	}

	/**
	 * Appends the message to info
	 * 
	 * @param msg
	 *            String
	 */
	public void info(String msg) {
		logger.info(msg);

	}

	/**
	 * Appends the message to the error
	 * 
	 * @param msg
	 *            String
	 */
	public void error(String msg) {
		logger.error(msg);

	}

}
