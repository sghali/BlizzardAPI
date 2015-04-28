package com.blizzard.api.test;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.blizzard.api.jaxb.Testsuite;
import com.blizzard.api.utils.HTMLUtils;
import com.blizzard.api.utils.HttpUtils;
import com.blizzard.api.utils.JsonUtils;
import com.blizzard.api.utils.Symbols;


/**
 * @desc This class is having runTC method which executes the test case
 * @author sghali
 * 
 */
public class TestcaseRun {
	private static Logger logger = Logger.getLogger(TestcaseRun.class);
	private String inputText;
	private HttpResponse httpResponse;
	private int statuscode_Input;
	private int statuscode_Output;
	private boolean isResponseValid;
	private static Map<Integer, String> testOutputData = new HashMap<Integer, String>();
	JsonReader jsonreader = null;
	private static Map<String, Object> map = new HashMap<String, Object>();
	private static Map<String, Object> dataMap = new HashMap<String, Object>();
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	

	/**
	 * To run the test case
	 * 
	 * @param url
	 *            - base URL of the suite
	 * @param tcd
	 *            - TestcaseData Object
	 */
	public boolean runTC(String url, TestcaseData tcd,Testsuite testSuite)
			throws JsonParsingException {
		boolean flag = false;
		// Read all the data related to test case
		String baseURL = url;
		int tcId = tcd.getTcId();
		String tcName = tcd.getTcName();
		String tcDesc = tcd.getTcDesc();
		String tcHttpMethod = tcd.getHttpMethod();
		String tcEndPointURL = tcd.getEndpointUrl();
		statuscode_Input = Integer.parseInt(TestEngine.testcasesMap
				.get("statuscode"));
		String tcExpOutput = TestEngine.testcasesMap.get("output").trim();
		inputText = TestEngine.testcasesMap.get("input");
		if (inputText == null) {
			inputText = Symbols.EMPTY.getSymbol();
		}
		/*Currently  we are not using any of the below lines of code but in future for any of requests
		based on input of other requests in input text below code will be helpful*/
		if (!inputText.equalsIgnoreCase(Symbols.EMPTY.getSymbol())) {
			try {
				jsonreader = Json.createReader(new StringReader(inputText));
				JsonObject jsonobj = jsonreader.readObject();
				// Verify if Input text is having any special characters
				boolean isPresent = JsonUtils.checkForSpecialCharacters(
						jsonobj, "");
				if (isPresent) {
					map = objectMapper.readValue(inputText,
							new TypeReference<HashMap<String, Object>>() {
							});
					if (map.size() >= 1) {
						for (String key : map.keySet()) {
							if (map.get(key).toString().startsWith("$")) {
								List<String> data = stripSpecialCharacters(map
										.get(key).toString());
								List<String> replaceArray = new ArrayList<String>();
								if (data.size() > 2) {
									for (int i = 0; i < data.size(); i = i + 2) {
										String mapValue = testOutputData
												.get(Integer.parseInt(data
														.get(i)));
										if (mapValue == null
												|| mapValue.length() == 0) {
											try {
												HTMLUtils.setHTMLData(tcName,
														tcDesc, inputText, "",
														0, "", "Fail",
														"Unable to retrieve "
																+ key
																+ " from ["
																+ data.get(0)
																+ "] test cas");
												return flag;
											} catch (IOException e) {
												logger.error(e);
											}
										}
										jsonreader = Json
												.createReader(new StringReader(
														mapValue));
										JsonObject jsontemp = jsonreader
												.readObject();
										String replaceValue = JsonUtils
												.getKeyValue(jsontemp, "", "id");
										replaceArray.add(replaceValue);
									}
									String[] temp = replaceArray.toString()
											.split("\\[");
									String[] temp1 = temp[1].split("\\]");
									map.put(key, temp1[0]);
								} else {
									String mapValue = testOutputData
											.get(Integer.parseInt(data.get(0)));
									if (mapValue == null
											|| mapValue.length() == 0) {
										try {
											HTMLUtils.setHTMLData(
													tcName,
													tcDesc,
													inputText,
													"",
													0,
													"",
													"Fail",
													"Unable to retrieve " + key
															+ " from ["
															+ data.get(0)
															+ "] test cas");
											return flag;
										} catch (IOException e) {
											logger.error(e);
										}
									}
									jsonreader = Json
											.createReader(new StringReader(
													mapValue));
									JsonObject jsontemp = jsonreader
											.readObject();
									String replaceValue = JsonUtils
											.getKeyValue(jsontemp, "",
													data.get(1));
									map.put(key, replaceValue);
								}
							}
						}
						inputText = objectMapper.writeValueAsString(map);
					} else {
						dataMap = JsonUtils.getChildObjectInJSON(inputText);
						Set<String> set = dataMap.keySet();
						for (String name : set) {
							map = objectMapper
									.readValue(
											dataMap.get(name).toString(),
											new TypeReference<HashMap<String, String>>() {
											});
							for (String key : map.keySet()) {
								if (map.get(key).toString().startsWith("$")) {
									List<String> data = stripSpecialCharacters(map
											.get(key).toString());
									String mapValue = testOutputData
											.get(Integer.parseInt(data.get(0)));
									if (mapValue == null
											|| mapValue.length() == 0) {
										try {
											HTMLUtils.setHTMLData(
													tcName,
													tcDesc,
													inputText,
													"",
													0,
													"",
													"Fail",
													"Unable to retrieve " + key
															+ " from ["
															+ data.get(0)
															+ "] test cas");
											return flag;
										} catch (IOException e) {
											logger.error(e);
										}
									}
									jsonreader = Json
											.createReader(new StringReader(
													mapValue));
									JsonObject jsontemp = jsonreader
											.readObject();
									String replaceValue = JsonUtils
											.getKeyValue(jsontemp, "", "id");
									map.put(key, replaceValue);
								}
							}
							dataMap.put(name, map);
						}
						inputText = objectMapper.writeValueAsString(dataMap);
					}
				}
			} catch (JsonParseException e1) {
				logger.error(e1);
			} catch (JsonMappingException e1) {
				logger.error(e1);
			} catch (IOException e1) {
				logger.error(e1);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		String urlExtn = tcd.getUrlExtn();
		String idNode = null;
		int[] urlExtns =null;
		if (urlExtn != Symbols.EMPTY.getSymbol()) {
			if(urlExtn.contains(Symbols.FORWARDSLASH.getSymbol())){		
				tcEndPointURL = tcd.getEndpointUrl();
			}else{
			// Clear all special characters in the urlExtn
			List<String> data = stripSpecialCharacters(urlExtn);
			// Get the value from the map based on the first item from data list
			String mapValue = testOutputData.get(Integer.parseInt(data.get(0)));
			if (mapValue == null || mapValue.length() == 0) {
				try {
					HTMLUtils.setHTMLData(tcName, tcDesc, inputText, "", 0, "",
							"Fail", "Unable to retrieve value of ID from ["
									+ data.get(0) + "] test case");

				} catch (IOException e) {
					logger.error(e);
				}
			}
			// Create object for ObjectMapper class
			objectMapper = new ObjectMapper();
			// Create object for JsonNode object
			JsonNode rootNode;
			try {
				// read the json string
				rootNode = objectMapper.readTree(mapValue);
				JsonNode temp = rootNode.findValue(data.get(1));
				if(temp.size()>1){
					urlExtns = new int[temp.size()];
					for(int i=0;i<temp.size();i++){
						urlExtns[i]=temp.get(i).getIntValue();
					}
				}
			
				else{
				// remove special characters
				 idNode = temp.toString().replace(
						Symbols.DOUBLEQUOTE.getSymbol(),
						Symbols.EMPTY.getSymbol());
				// append the idNode to teEndPointURL
				tcEndPointURL = tcd.getEndpointUrl()
						+ Symbols.FORWARDSLASH.getSymbol() + idNode;
				}
			
			} catch (JsonProcessingException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (NullPointerException e) {
				logger.error(e);
			}
		}
		}
		String tcQuery = tcd.getQueryString();

		// Execute POST request
		if (tcHttpMethod.equalsIgnoreCase("POST")) {
			logger.info("Executing POST request - ");
			try {
				httpResponse = HttpUtils.doPost(baseURL, tcQuery,
						tcEndPointURL,inputText,urlExtn);
				flag=validateHttpResponse(httpResponse, tcName ,tcDesc, inputText , statuscode_Input, tcExpOutput, tcId );
			} catch (URISyntaxException e) {
				logger.error(e);
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
		} else if (tcHttpMethod.equalsIgnoreCase("GET")) {
			logger.info("Executing GET request - ");
			try {
				httpResponse = HttpUtils.doGet(baseURL, tcQuery, tcEndPointURL,
						inputText,urlExtn);
				flag=validateHttpResponse(httpResponse, tcName ,tcDesc, inputText , statuscode_Input, tcExpOutput, tcId );
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (URISyntaxException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
			// Execute PUT request
		} else if (tcHttpMethod.equalsIgnoreCase("PUT")) {
			logger.info("Executing PUT request - ");
			try {
				httpResponse = HttpUtils.doPut(baseURL, tcQuery, tcEndPointURL,
						inputText,urlExtn);
			   flag=validateHttpResponse(httpResponse, tcName ,tcDesc, inputText , statuscode_Input, tcExpOutput, tcId );
			} catch (URISyntaxException e) {
				logger.error(e);
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
			// Execute DELETE request
		} else if (tcHttpMethod.equalsIgnoreCase("DELETE")) {
			logger.info("Executing DELETE request - ");
			try {
				httpResponse = HttpUtils.doDelete(baseURL, tcQuery,
						tcEndPointURL, inputText,urlExtn);
				flag=validateHttpResponse(httpResponse, tcName , tcDesc,inputText , statuscode_Input, tcExpOutput, tcId );
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (URISyntaxException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
		}else if (tcHttpMethod.equalsIgnoreCase("MULTIPLE_GETS")) {
		logger.info("Executing Multiple GET requests - ");
		try {
			// Wait for the value to update in API
			if(urlExtns.length==0){
				flag=false;
			}else{
			boolean[] allResponses = new boolean[urlExtns.length];
			for(int j=0;j< urlExtns.length;j++){
				//tcEndPointURL =tcEndPointURL+"/"+urlExtns[j];
				logger.info("Performing GET Operation on " +tcEndPointURL+ " for"+ urlExtns[j]);
				httpResponse = HttpUtils.doGet(baseURL,tcQuery, tcEndPointURL,
						inputText,"/"+urlExtns[j]);
				flag=validateHttpResponse(httpResponse, tcName , tcDesc,inputText , statuscode_Input, tcExpOutput, tcId );
				allResponses[j]=flag;
			}
			for(int k=0;k<allResponses.length;k++){
				if(allResponses[k]==false){
					flag=false;
					break;
				}else{
					flag =true;
				}
			}
			return flag;
			}
		} catch (HttpException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (URISyntaxException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		}
		return flag;
}
			
			
			

	/**
	 * This method removes all special characters in a string
	 * 
	 * @param str
	 * @return
	 */
	public List<String> stripSpecialCharacters(String str) {
		List<String> splitString = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\w+)");
		Matcher m = p.matcher(str);
		while (m.find()) {
			splitString.add(m.group());
		}
		return splitString;
	}
	
	/**
	 * This method validate the response of the http operations
	 * 	Covers : Validating response code , response type (JSON) and expected output
	 * 
	 * @param str
	 * @return
	 */
	public boolean validateHttpResponse(HttpResponse httpResponse, String tcName , String tcDesc,String inputText ,int statuscode_Input,String tcExpOutput,int tcId  ){
	try{
		boolean isValid = false;
		if (httpResponse == null) {
			HTMLUtils.setHTMLData(tcName, "", inputText,
					HttpUtils.getHttpInputRequest(), 0, "", "Fail",
					"HttpResonse is null");
			return isValid;
		}
		// Get the Status of the request
		statuscode_Output = httpResponse.getStatusLine().getStatusCode();
		// Compare Expected status code and actual status code
		if (statuscode_Output != statuscode_Input) {
			HTMLUtils.setHTMLData(tcName, "", inputText,
					HttpUtils.getHttpInputRequest(), statuscode_Output, "",
					"Fail",
					"The expected and actual status codes are not same");
			return isValid;
		}
		HttpEntity responseEntity = httpResponse.getEntity();
		// Check if responseEntitity is null
		if (responseEntity == null) {
			HTMLUtils.setHTMLData(tcName, "", inputText,
					HttpUtils.getHttpInputRequest(), statuscode_Output, "",
					"Pass",
					"The expected,actual status codes are same and "
							+ "No Response retrunted for the request");
			isValid = true;
			return isValid;
		}
		// Get the response
		String response = null;
		// Retrieve the response
		response = EntityUtils.toString(responseEntity, "UTF-8");
		if (response.length() == 0) {
			HTMLUtils.setHTMLData(tcName, "", inputText,
					HttpUtils.getHttpInputRequest(), statuscode_Output,
					response, "Pass",
					"The expected, actual status codes are same and "
							+ "No Response retrunted for the request");
			isValid = true;
			return isValid;
		}
		// Verify if the response is valid JSON
		isResponseValid = JsonUtils.isJSONValid(response);
		if (!isResponseValid) {
			HTMLUtils
					.setHTMLData(
							tcName,
							"",
							inputText,
							HttpUtils.getHttpInputRequest(),
							statuscode_Output,
							response,
							"Fail",
							"The expected, actual status codes are same."
									+ "But the returned response is not valid JSON");
			return isValid;
		}
		if (tcExpOutput.length() == 0) {
			jsonreader = Json.createReader(new StringReader(response));
			JsonValue jsonExpValue = jsonreader.read();
			boolean isValidated = JsonUtils.validateJsonObject(
					jsonExpValue, "");
			if (!isValidated) {
				HTMLUtils.setHTMLData(tcName, "", inputText,
						HttpUtils.getHttpInputRequest(), statuscode_Output,
						response, "Fail", "Un Successful");
				return isValid;
			}
			HTMLUtils.setHTMLData(tcName, "", inputText,
					HttpUtils.getHttpInputRequest(), statuscode_Output,
					response, "Pass", "Successful");
			testOutputData.put(tcId, response);
			isValid = true;
			return isValid;
			} else {
			// Read the Expected Output
			jsonreader = Json.createReader(new StringReader(tcExpOutput));
			JsonValue expJsonVal = jsonreader.read();
			jsonreader = Json.createReader(new StringReader(response));
			JsonValue actJsonVal = jsonreader.read();
			
			boolean isPresent = JsonUtils.checkForSpecialCharacters(
					expJsonVal, "");
			if(isPresent){
				map = objectMapper.readValue(tcExpOutput,new TypeReference<HashMap<String, Object>>() {});
				for (String key : map.keySet()) {
							if (map.get(key).toString().startsWith("$")) {
									List<String> data = stripSpecialCharacters(map.get(key).toString());
									String mapValue = testOutputData.get(Integer.parseInt(data.get(0)));
									map.put(key, mapValue);
									tcExpOutput = objectMapper.writeValueAsString(map);
									jsonreader = Json.createReader(new StringReader(tcExpOutput));
									expJsonVal = jsonreader.read();
				}
				}
			}
			boolean isValidated = JsonUtils.comparetwoJsonObjects(
					expJsonVal, "", actJsonVal, "");
			if (!isValidated) {
				HTMLUtils.setHTMLData(tcName, tcDesc, inputText,
						HttpUtils.getHttpInputRequest(), statuscode_Output,
						response, "Fail", "Un Successful :The out response value is not matching with expected response:"+tcExpOutput);
				isValid = isValidated;
				return isValid;
			}
			HTMLUtils.setHTMLData(tcName, tcDesc, inputText,
					HttpUtils.getHttpInputRequest(), statuscode_Output,
					response, "Pass", "Successful");
			testOutputData.put(tcId, response);
			isValid = true;
			return isValid;
		}
	} 
	catch (ParseException e) {
		logger.error(e);
	} catch (IOException e) {
		logger.error(e);
	} catch (Throwable e) {
		logger.error(e);
	}	
	return true;
	}
	
}
