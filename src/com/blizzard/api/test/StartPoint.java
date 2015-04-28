package com.blizzard.api.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.blizzard.api.constants.GlobalConstants;
import com.blizzard.api.jaxb.Testsuite;
import com.blizzard.api.utils.JaxbUtil;
import com.blizzard.api.utils.LogUtils;

/**
 * @desc this class contains main method and is starting of the API Framework
 * @author sghali
 * 
 */

public class StartPoint {
	private static Logger logger = Logger.getLogger(StartPoint.class);
	public static List<Testsuite> testsuites_For_Execution = new ArrayList<Testsuite>();
	static LogUtils log = null;
	public static Testsuite testsuite ;

	/**
	 * @desc This method performs initial setup Verify whether directory
	 *       available or not Verify the size of the files in the directory Loop
	 *       all the files and add suites based on runmode into list
	 * 
	 */
	public static void setUp() {
		try {
			log = new LogUtils(GlobalConstants.LOG4J_PATH);
			logger.info("Configuration log4j is successful");
			// check if dir exists
			File inputDir = new File(GlobalConstants.INPUT_XML_PATH);
			if (!inputDir.exists() || !inputDir.isDirectory()) {
				logger.error("Dir does not exist. Program will exit");
				System.exit(0); // exit
			}
			// check number of files in the dir
			File[] inputFiles = inputDir.listFiles();
			if (inputFiles == null || inputFiles.length == 0) {
				logger.error("No Files in the Dir hence exit the program");
				System.exit(0); // exit
			}
			// Loop all the files and find total test suites for execution
			for (File inputFile : inputFiles) {
				 testsuite = (Testsuite) JaxbUtil.unMarshal(
						inputFile.getAbsolutePath(), Testsuite.class);
				if (testsuite != null) {
					if ("Y".equalsIgnoreCase(testsuite.getRunmode())) {
						testsuites_For_Execution.add(testsuite);
					}
				}
			}
			// if no suites for execution then return
			if (testsuites_For_Execution.size() == 0) {
				logger.info("No suites for execution hence exit the program");
				System.exit(0);
			}
			logger.info(" [" + testsuites_For_Execution.size()
					+ "] suite/s for execution");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			setUp();
			logger.info("Initialization successful");
			TestEngine teObj = new TestEngine();
			logger.info("Starting execution");
			teObj.execute();
			tearDown();
			logger.info("Execution is completed");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Currently there are no tear down operations, in future for such operation below method can be used
	 */
	private static void tearDown() {
		// do nothing
	}

	
}