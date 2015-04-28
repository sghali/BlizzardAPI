package com.blizzard.api.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.blizzard.api.jaxb.Testsuite;
import com.blizzard.api.jaxb.Testsuite.Testset;
import com.blizzard.api.jaxb.Testsuite.Testset.Testcase;
import com.blizzard.api.jaxb.Testsuite.Testset.Testcase.Param;
import com.blizzard.api.utils.HTMLUtils;
import com.blizzard.api.utils.ServerUtilities;

/**
 * @desc this class reads the input xml file and sets the data in to respective
 *       classes and initialize test case run
 * @author sghali
 * 
 */
public class TestEngine {
	private static Logger logger = Logger.getLogger(TestEngine.class);

	public TestcaseData tcd;
	public TestcaseRun tcr = new TestcaseRun();;
	public static Map<String, String> testcasesMap = new HashMap<String, String>();
	private String currentSuitName = null;

	/**
	 * this method reads the input xml file and sets the data in to respective
	 * classes and initialize test case run
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException {
		// Create HTML Folder for report generation
		HTMLUtils.createHTMLFolder();
		// Loop for each suite in the list
		for (Testsuite testsuite : StartPoint.testsuites_For_Execution) {
			int passCount = 0;
			int failCount = 0;
			int skipCount = 0;
			int totalCount = 0;
		
			currentSuitName = testsuite.getName();
			// Generate suite result folder
			HTMLUtils.createSuiteFolder(currentSuitName);
			// Get the list of sets
			List<Testset> testsets = testsuite.getTestset();
			// Get the total number of test cases in each test set
			for (Testset testSet : testsets) {
				List<Testcase> testcases = testSet.getTestcase();
				totalCount += testcases.size();
			}
			List<Testset> testsets_For_Execution = new ArrayList<Testset>();
			// Add the sets into list for which runmode is Y
			for (Testset testset : testsets) {
				if ("Y".equalsIgnoreCase(testset.getRunmode())) {
					testsets_For_Execution.add(testset);
				}
			}
			// If no test sets added in the list then return
			if (testsets_For_Execution.size() == 0) {
				logger.info("No Test set for execution");
				return;
			}
			logger.info("Total " + testsets_For_Execution.size()
					+ " sets for execution in [" + testsuite.getName()
					+ "] suite");
			// Get the baseURL
			String baseURL = testsuite.getBaseurl();
			// set the public key
			ServerUtilities.setPublicKey(testsuite.getPublicKey());
			// set the SigKey
			ServerUtilities.setSignatureKey(testsuite.getSigkey());
			// Loop for each test set in the list
			for (Testset testSet : testsets_For_Execution) {
				logger.info("Total " + testSet.getTestcase().size()
						+ " test cases for execution in " + testSet.getName()
						+ " set");
				// Create HTML File
				HTMLUtils.createHtmlFile(currentSuitName, testSet.getName());
				//log about the current set execution details
				logger.info("Started executing Test Set :" + testSet.getName());
				//logger.info(testSet.getDesc());
				// Loop for each test case in the test set
				
				for (Testcase testcase : testSet.getTestcase()) {
					if (testcase == null) {
						logger.info("No Test case added for Test set ["
								+ testSet.getName() + "]");
						continue;
					}
					// Create object for TestcaseData
					tcd = new TestcaseData();
					// set the test case Id
					tcd.setTcId(testcase.getId());
					// set the test case Name
					tcd.setTcName(testcase.getName());
					// set the desc of test case
					tcd.setTcDesc(testcase.getDesc());
					// set the Query String
					tcd.setQueryString(testcase.getQueryString());
					// set the end point URL
					tcd.setEndpointUrl(testcase.getEndpointurl());
					// set the url Extn
					tcd.setUrlExtn(testcase.getUrlextn());
					// set the Http method
					tcd.setHttpMethod(testcase.getHttpmethod());
					// read all params in the test case and add into map
					for (Param param : testcase.getParam()) {
						testcasesMap.put(param.getId(), param.getValue());
					}
					boolean tcStatus = tcr.runTC(baseURL, tcd,testsuite);
					if (tcStatus) {
						logger.info("Successfully performed "+"'"+ testcase.getHttpmethod()+"'"+"operation on" + " '" +testcase.getDesc() +"'");
						passCount++;
					} else {
						logger.info("Failed to  perform "+"'"+ testcase.getHttpmethod()+"'"+"operation on" + " '" + testcase.getDesc() +"'");
						failCount++;
					}
				}
				// close the HTML
				HTMLUtils.addClosingTags();
			}
			skipCount = totalCount - (passCount + failCount);
			// Create HTML Suite Report
			HTMLUtils.createReport(currentSuitName, passCount, failCount,
					skipCount);
		}
	}
}
