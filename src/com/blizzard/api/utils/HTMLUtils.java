package com.blizzard.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.blizzard.api.constants.GlobalConstants;

/**
 * @author sghali
 * 
 */
public class HTMLUtils {
	private static Logger logger = Logger.getLogger(HTMLUtils.class);
	private static OutputStream htmlfile;
	private static PrintStream out;
	private static String html_folder = GlobalConstants.TEST_OUTPUT_FOLDER;
	private static String blizzardAPI_folder = GlobalConstants.BLIZZARD_FOLDER;
	private static boolean flag;
	private static String cssFile = html_folder + "styles/style.css";
	private static String currentDateAndTime = getCurrentDateAndTime();
	private static String currentFolder = blizzardAPI_folder + "test-results_"
			+ currentDateAndTime;

	/**
	 * Captures current date and time
	 * 
	 * @return
	 */
	public static String getCurrentDateAndTime() {
		String datetime = null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
			datetime = format.format(new Date());
			return datetime;
		} catch (Exception e) {
			logger.error(e);
		}
		return datetime;
	}

	/**
	 * Creating new HTML Folder
	 * 
	 * @return null
	 */

	public static boolean createHTMLFolder() {
		try {
			File dir = new File(currentFolder);
			if (dir.exists()) {
				flag = true;
				return flag;
			} else {
				dir.mkdir();
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			logger.error("Error while Creating a directory..." + e);
		}
		return flag;
	}

	/**
	 * This method is used to create Header of Index file
	 * 
	 * @return
	 */
	public static boolean createHeaderFile() {
		try {
			htmlfile = new FileOutputStream(new File(currentFolder
					+ "//header.html"));
		out = new PrintStream(htmlfile);
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<link rel='stylesheet' href=" + cssFile + ">");
		out.println("<div class=\"outerContainer\">");
		out.println("<div class=\"innerContainer\">");
		out.println(" <div >");
		out.println("<p style='display:inline-block;font-size:30px;'> ******* API Results</p>");
		out.println(" </div>");
		out.println(" </div>");
		out.println(" </div>");
		out.println("</HEAD>");
		out.println("</HTML>");
		flag = true;
		return flag;
	} catch (Exception e) {
		logger.error(e);
		}
		return flag;
	}

	/**
	 * This method creates file which is having List names of all suites
	 * 
	 * @return
	 */
	public static boolean createListFile() {
		try {
			htmlfile = new FileOutputStream(new File(currentFolder
					+ "//navigation.html"));
			out = new PrintStream(htmlfile);
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<link rel='stylesheet' href=" + cssFile + ">");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"menu\">");
			out.println("<h3 align=Left> Test Suites</h3>");
			out.println("<a  href=\"Items API.html\" target=\"content\" style=\"float:left;\">Items</a><br>");
			out.println("</div>");
			out.println("</body></html>");
			flag = true;
			return flag;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * This method creates Index File of all the Html files
	 * 
	 * @return
	 */
	public static boolean createIndexFile() {
		try {
			if (!createHeaderFile()) {
				createHeaderFile();
			}
			if (!createListFile()) {
				createListFile();
			}
			htmlfile = new FileOutputStream(new File(currentFolder
					+ "//index.html"));
			out = new PrintStream(htmlfile);
			String title = "Blizzard API Results";
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<link rel='stylesheet' href=" + cssFile + ">");
			out.println("<TITLE>" + title + "</TITLE>");
			out.println("</HEAD>");
			out.println("<frameset rows=\"10%,80%\" border=\"0\">");
			out.println("<frame name=\"header\" src=\"header.html\">");
			out.println("<frameset cols=\"15%,*\">");
			out.println("<frame name=\"navigation\" src=\"navigation.html\">");
			out.println("<frame name=\"content\" src=\"\">");
			out.println("</frameset>");
			out.println("<frame name=\"footer\" src=\"\">");
			out.println("</frameset>");
			out.println("</HTML>");
			flag = true;
			return flag;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * @param filename
	 * @param setname
	 * @return
	 */
	public static void createHtmlFile(String filename,String setname) {
		try {
			File dir = new File(currentFolder + "//" + filename);
			if (!dir.exists()) {
				dir.mkdir();
			}
			htmlfile = new FileOutputStream(new File(currentFolder + "//"
					+ filename + "//" + filename + ".html"), true);
			out = new PrintStream(htmlfile);
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<link rel='stylesheet' href=" + cssFile + ">");
			out.println("</HEAD>");
			out.println("<div id=\"header\"\">");
			out.println("<h3 style=\"margin-bottom:0;\">" + setname
					+ "_Results</h3></div>");
			out.println("<div id=\"space1\" style=\"height:20px\"></div>");
			out.println("</div>");
			out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 width=\"100%\">");
			out.println("<TR bgcolor=\"#B5B6BB\">");

			out.println("<TD>");
			out.println("TC Name");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Description");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Input");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Request URL");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Status Code");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Response");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Result");
			out.println("</TD>");

			out.println("<TD>");
			out.println("Comments");
			out.println("</TD>");

			out.flush();
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * This method close the print stream into html file
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean closeFile(String filename) {
		try {
			htmlfile = new FileOutputStream(new File(currentFolder + "//"
					+ filename + ".html"), true);
			htmlfile.close();
			flag = true;
			return flag;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * @param testcasename
	 * @param httpmethod
	 * @param input
	 * @param statuscode
	 * @param output
	 * @param result
	 * @param comments
	 * @throws IOException
	 */
	public static void setHTMLData(String testcasename, String description,
			String input, String Request, int statuscode, String output,
			String result, String comments) throws IOException {
		try {
			out.println("<TR>");
			out.println("<TD>");
			out.println("" + testcasename + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + description + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + input + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + Request + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + statuscode + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + output + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + result + "");
			out.println("</TD>");

			out.println("<TD>");
			out.println("" + comments + "");
			out.println("</TD>");

			out.println("</TR>");
			out.flush();
			// addClosingTags(htmlfile);
		} catch (Exception e) {
			logger.error(e, e);
		}

	}

	/**
	 * closing the html tags
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static void addClosingTags() {
		try {
			out.println("</div></TABLE></BODY></HTML>");
			out.flush();
			//createIndexFile();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void createReport(String suitname, int paas, int fail,
			int skip) {
		try {
			htmlfile = new FileOutputStream(new File(currentFolder + "//"
					+ suitname + "//" + suitname + "_report.html"), true);
			out = new PrintStream(htmlfile);
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("</HEAD>");
			out.println("<BODY>");
			// Giving space
			out.println("<div id=\"space1\" style=\"height:20px\"></div>");
			// Creating Status table
			out.println("<TABLE BORDER=\"5\"    WIDTH=\"30%\"   Align=\"center\">");
			out.println("<TR>");
			out.println("<TH COLSPAN=\"2\"><H3>" + suitname + "</H3></TH></TR>");
			out.println("<TR ALIGN=\"CENTER\" bgcolor=\"#3F6826\">");
			out.println("<TD>Passed</TD>");
			out.println("<TD>" + paas + "</TD></TR>");

			out.println("<TR ALIGN=\"CENTER\" bgcolor=\"#FF0000\">");
			out.println("<TD>Failed</TD>");
			out.println("<TD>" + fail + "</TD></TR>");

			out.println("<TR ALIGN=\"CENTER\" bgcolor=\"FFFF00\">");
			out.println("<TD>Skipped</TD>");
			out.println("<TD>" + skip + "</TD></TR>");
			out.println("</TABLE>");
			// Giving space
			out.println("<div id=\"space1\" style=\"height:20px\"></div>");

			out.println("</BODY>");
			out.println("</HTML>");
			createSuiteReport(suitname);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void createSuiteReport(String suitname) {
		try {
			htmlfile = new FileOutputStream(new File(currentFolder + "//"
					+ suitname + "//" + suitname + "_final.html"), true);
			out = new PrintStream(htmlfile);
			out.println("<HTML>");
			out.println("<frameset rows=\"20%,80%\" border=\"0\">");
			out.println("<frame name=\"headerReport\" src=" + suitname
					+ "_report.html>");
			out.println("<frame name=\"DataReport\" src=" + suitname + ".html>");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param currentSuitName
	 */
	public static boolean createSuiteFolder(String currentSuitName) {
		try {
			File dir = new File(currentFolder+"//"+currentSuitName);
			if (dir.exists()) {
				flag = true;
				return flag;
			} else {
				dir.mkdir();
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			logger.error("Error while Creating a directory..." + e);
		}
		return flag;
		
		
	}

}
