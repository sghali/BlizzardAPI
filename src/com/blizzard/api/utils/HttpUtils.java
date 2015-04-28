package com.blizzard.api.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * @desc This class performs all HTTP operations
 * @author sghali
 * 
 */

@SuppressWarnings("deprecation")
public class HttpUtils {
	private static Logger logger = Logger.getLogger(HttpUtils.class);
	private static String AUTH = "BNET";
	private static String AUTH_VALUE = null;
	private static HttpClient httpClient = null;
	private static HttpResponse response = null;
	private static String httpInputRequest;

	/**
	 * returns httpInputRequest
	 * 
	 * @return
	 */
	public static String getHttpInputRequest() {
		return httpInputRequest;
	}

	/**
	 * sets httpInputRequest
	 * 
	 * @param httpInputRequest
	 */
	public static void setHttpInputRequest(String httpInputRequest) {
		HttpUtils.httpInputRequest = httpInputRequest;
	}

	/**
	 * This method performs HTTP - Get operation
	 * 
	 * @param serverURL
	 * @param endPointURL
	 * @param GetText
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */

	public static HttpResponse doGet(String serverURL, String QueryString,
			String endPointURL, String getText,String urlExtn) throws HttpException,
			IOException, URISyntaxException {
		try {
			httpClient = new DefaultHttpClient();
			if (QueryString.length() > 0) {
				if (QueryString.contains("?")) {
					endPointURL = endPointURL
							+ QueryString.substring(0, 1)
							+ URLEncoder.encode(QueryString.substring(1),
									"UTF-8");
					QueryString = QueryString.substring(1);
				}
			}
			if(!urlExtn.isEmpty()){
				endPointURL = endPointURL+urlExtn;
			}
			HttpGet httpGet = new HttpGet(serverURL + endPointURL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content-type", "application/json"));
			AUTH_VALUE = ServerUtilities.getAuthenticationHeader("GET",
					urlExtn);
			AUTH_VALUE = AUTH +" "+AUTH_VALUE;
			//httpGet.addHeader("Authorization", AUTH_VALUE);
			httpGet.addHeader("Content-Type", "application/json");
			List<String> headers = new ArrayList<String>();
			Header[] requestHeaders = httpGet.getAllHeaders();
			for (Header header : requestHeaders) {
				headers.add(header.toString());
			}
			HttpUtils.setHttpInputRequest(httpGet.getRequestLine().toString()
					+ headers);
			response = httpClient.execute(httpGet);
			return response;
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return response;
	}

	/**
	 * This method performs HTTP Post operation
	 * 
	 * @param serverURL
	 * @param endPointURL
	 * @param postText
	 * @return
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */

	public static HttpResponse doPost(String serverURL, String QueryString,
			String endPointURL, String postText,String urlExtn) throws URISyntaxException,
			HttpException, IOException {
		try {
			httpClient = new DefaultHttpClient();
			if (QueryString.length() > 0) {
				if (QueryString.contains("?")) {
					endPointURL = endPointURL
							+ QueryString.substring(0, 1)
							+ URLEncoder.encode(QueryString.substring(1),
									"UTF-8");
				}
			}
			HttpPost httpPost = new HttpPost(serverURL + endPointURL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content-type", "application/json"));
			AUTH_VALUE = ServerUtilities.getAuthenticationHeader("POST",
					urlExtn);
			AUTH_VALUE = AUTH +" "+AUTH_VALUE;
			//httpPost.addHeader("Authorization", AUTH_VALUE);
			StringEntity input = new StringEntity(postText);
			input.setContentType("application/json");
			httpPost.setEntity(input);
			httpPost.addHeader("content-type", "application/json");
			List<String> headers = new ArrayList<String>();
			Header[] requestHeaders = httpPost.getAllHeaders();
			for (Header header : requestHeaders) {
				headers.add(header.toString());
			}
			HttpUtils.setHttpInputRequest(httpPost.getRequestLine().toString()
					+ headers);
			response = httpClient.execute(httpPost);
			return response;
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (NullPointerException e) {
			logger.error(e);
		}
		return response;
	}

	/**
	 * This method performs HTTP Put operation
	 * 
	 * @param serverURL
	 * @param endPointURL
	 * @param putText
	 * @return
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */

	public static HttpResponse doPut(String serverURL, String QueryString,
			String endPointURL, final String putText,String urlExtn)
			throws URISyntaxException, HttpException, IOException {
		try {
			httpClient = new DefaultHttpClient();
			if (QueryString.length() > 0) {
				if (QueryString.contains("?")) {
					endPointURL = endPointURL
							+ QueryString.substring(0, 1)
							+ URLEncoder.encode(QueryString.substring(1),
									"UTF-8");
				}
			}
			HttpPut httpPut = new HttpPut(serverURL + endPointURL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content-type", "application/json"));
			AUTH_VALUE = ServerUtilities.getAuthenticationHeader("POST",
					urlExtn);
			AUTH_VALUE = AUTH +" "+AUTH_VALUE;
			httpPut.addHeader("Authorization", AUTH_VALUE);
			StringEntity input = new StringEntity(putText);
			input.setContentType("application/json");
			httpPut.setEntity(input);
			httpPut.addHeader("Content-Type", "application/json");
			List<String> headers = new ArrayList<String>();
			Header[] requestHeaders = httpPut.getAllHeaders();
			for (Header header : requestHeaders) {
				headers.add(header.toString());
			}
			HttpUtils.setHttpInputRequest(httpPut.getRequestLine().toString()
					+ headers);
			response = httpClient.execute(httpPut);
			return response;
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return response;
	}

	/**
	 * This method performs HTTP Delete operation
	 * 
	 * @param serverURL
	 * @param endPointURL
	 * @param inputarg
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */

	public static HttpResponse doDelete(String serverURL,String QueryString, String endPointURL,
			String inputarg,String urlExtn) throws HttpException,
			IOException, URISyntaxException {
		try {
			httpClient = new DefaultHttpClient();
			if (QueryString.length() > 0) {
				if (QueryString.contains("?")) {
					endPointURL = endPointURL
							+ QueryString.substring(0, 1)
							+ URLEncoder.encode(QueryString.substring(1),
									"UTF-8");
				}
			}
			HttpDelete httpDelete = new HttpDelete(serverURL + endPointURL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content-type", "application/json"));
			AUTH_VALUE = ServerUtilities.getAuthenticationHeader("POST",
					urlExtn);
			AUTH_VALUE = AUTH +" "+AUTH_VALUE;
			httpDelete.addHeader("Authorization", AUTH_VALUE);
			httpDelete.addHeader("Content-Type", "application/json");
			List<String> headers = new ArrayList<String>();
			Header[] requestHeaders = httpDelete.getAllHeaders();
			for (Header header : requestHeaders) {
				headers.add(header.toString());
			}
			HttpUtils.setHttpInputRequest(httpDelete.getRequestLine()
					.toString() + headers);
			response = httpClient.execute(httpDelete);
			return response;
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return response;
	}

}
