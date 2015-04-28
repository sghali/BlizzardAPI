package com.blizzard.api.test;

/**
 * This is POJO class for Test case
 * 
 * @author sghali
 * 
 */
public class TestcaseData {
	private int tcId;
	private String tcName;
	private String tcDesc;
	private String endpointUrl;
	private String urlExtn;
	private String httpMethod;
	private String queryString;
	

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Get the test case Id
	 * 
	 * @return
	 */

	public int getTcId() {
		return tcId;
	}

	/**
	 * Set the test case Id
	 * 
	 * @param tcId
	 */
	public void setTcId(int tcId) {
		this.tcId = tcId;
	}

	/**
	 * Get the test case Name
	 * 
	 * @return
	 */
	public String getTcName() {
		return tcName;
	}

	/**
	 * Set the test case Name
	 * 
	 * @param tcName
	 */
	public void setTcName(String tcName) {
		this.tcName = tcName;
	}

	/**
	 * Get the EndPoint URL
	 * 
	 * @return
	 */
	public String getEndpointUrl() {
		return endpointUrl;
	}

	/**
	 * Set the EndPoint URK
	 * 
	 * @param endpointUrl
	 */
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	/**
	 * Get the URL Extn
	 * 
	 * @return
	 */
	public String getUrlExtn() {
		return urlExtn;
	}

	/**
	 * Set the URL Extn
	 * 
	 * @param urlExtn
	 */
	public void setUrlExtn(String urlExtn) {
		this.urlExtn = urlExtn;
	}

	/**
	 * Get the HTTP Method
	 * 
	 * @return
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Set the HTTP Method
	 * 
	 * @param httpMethod
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * Gets the test case desc
	 * 
	 * @return tcDesc
	 */
	public String getTcDesc() {
		return tcDesc;
	}

	/**
	 * Sets the test case desc
	 * 
	 * @param tcDesc
	 */
	public void setTcDesc(String tcDesc) {
		this.tcDesc = tcDesc;
	}

}
