package com.blizzard.api.utils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.apache.log4j.Logger;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * This class used for to validate JSON
 * 
 * @author sghali
 * 
 */
public class JsonUtils {
	private static Logger logger = Logger.getLogger(JsonUtils.class);
	private static int flag = 0;
	private static boolean status = false;
	private static String result = null;

	// private static int resultNumber = 0;

	/**
	 * This method validate the input as a JSON string
	 * 
	 * @param strJson
	 * @return
	 */
	public static boolean isJSONValid(String strJson) {
		boolean valid = false;
		JsonParser jsonParser = null;
		try {
			if (jsonParser == null) {
				jsonParser = new JsonParser();
			}

			jsonParser.parse(strJson);
			valid = true;

		} catch (JsonParseException e) {
			logger.info(e);
			valid = false;
		}
		return valid;
	}

	/**
	 * This method validates the Key & Value pair in a JSON string
	 * 
	 * @param tree
	 * @param key
	 * @return
	 */
	public static boolean validateJsonObject(JsonValue tree, String key) {
		try {
			switch (tree.getValueType()) {
			// For Object value type
			case OBJECT:
				// Create JSON object
				JsonObject object = (JsonObject) tree;
				// Loop all the Key sets
				for (String name : object.keySet())
					validateJsonObject(object.get(name), name);
				break;
			// For Array value type
			case ARRAY:
				JsonArray array = (JsonArray) tree;
				for (JsonValue val : array)
					validateJsonObject(val, null);
				break;
			// For String value type
			case STRING:
				JsonString st = (JsonString) tree;
				if (st.getString().length() > 0) {
					flag++;
				}
				break;
			// For Number value type
			case NUMBER:
				JsonNumber num = (JsonNumber) tree;
				if (num.toString().length() > 0) {
					flag++;
				}
				break;
			// For True value type
			case TRUE:
				break;
			// For False value type
			case FALSE:
				break;
			// For null value type
			case NULL:
				break;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		if (flag == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method compares two JSON objects
	 * 
	 * @param expJsonObj
	 * @param expKey
	 * @param actJsonObj
	 * @param actKey
	 * @return true if two JSON objects are same otherwise false
	 */
	public static boolean comparetwoJsonObjects(JsonValue expJsonObj,
			String expKey, JsonValue actJsonObj, String actKey) {
		try {
			switch (expJsonObj.getValueType()) {
			// For Object value type
			case OBJECT:
				// Create JSON object based on the expected json output
				JsonObject expObject = (JsonObject) expJsonObj;
				// Create JSON object based on the actual json output
				JsonObject actObject = (JsonObject) actJsonObj;
				// Loop all expected object key sets
				for (String expname : expObject.keySet()) {
					// Loop all actual object Key sets
					for (String actname : actObject.keySet()) {
						if (expname.equalsIgnoreCase(actname)) {
							if(comparetwoJsonObjects(expObject.get(expname),
									expname, actObject.get(actname), actname)){
							flag++;
							}else{
								return false;
							}
						}
					}
				}
				break;
			// For Array value type
			case ARRAY:
				JsonArray exparray = (JsonArray) expJsonObj;
				JsonArray actarray = (JsonArray) actJsonObj;
				for (JsonValue expval : exparray) {
					for (JsonValue actval : actarray) {
						comparetwoJsonObjects(expval, null, actval, null);
					}
				}
				break;
			// For String value type
			case STRING:
				JsonString expStringValue = (JsonString) expJsonObj;
				//JsonString actStringValue = (JsonString) actJsonObj;
				if (expStringValue.getString().equalsIgnoreCase(
						actJsonObj.toString())) {
					flag++;
				}
				break;
			// For Number value type
			case NUMBER:
				JsonNumber expnum = (JsonNumber) expJsonObj;
				JsonNumber actnum = (JsonNumber) actJsonObj;
				if (expnum.toString().equalsIgnoreCase(actnum.toString())) {
					flag++;
				}
				break;
			// For True value type
			case TRUE:
				break;
			// For False value type
			case FALSE:
				break;
			// For Null value type
			case NULL:
				break;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		if (flag == 0) {
			return false;
		} else {
			return true;
		}

	}

	public static boolean checkForSpecialCharacters(JsonValue tree, String value) {
		try {
			switch (tree.getValueType()) {
			// For Object value type
			case OBJECT:
				// Create JSON object
				JsonObject object = (JsonObject) tree;
				// Loop all the Key sets
				for (String name : object.keySet())
					checkForSpecialCharacters(object.get(name), name);
				break;
			// For Array value type
			case ARRAY:
				JsonArray array = (JsonArray) tree;
				for (JsonValue val : array)
					checkForSpecialCharacters(val, null);
				break;
			// For String value type
			case STRING:
				JsonString st = (JsonString) tree;
				if (st.getString().startsWith("$")) {
					status = true;
				}
				break;
			// For Number value type
			case NUMBER:
				break;
			// For True value type
			case TRUE:
				break;
			// For False value type
			case FALSE:
				break;
			// For null value type
			case NULL:
				break;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return status;
	}

	public static String getKeyValue(JsonValue tree, String key,
			String expString) {
		try {
			switch (tree.getValueType()) {
			// For Object value type
			case OBJECT:
				// Create JSON object
				JsonObject object = (JsonObject) tree;
				// Loop all the Key sets
				for (String name : object.keySet())
					getKeyValue(object.get(name), name, expString);
				break;
			// For Array value type
			case ARRAY:
				JsonArray array = (JsonArray) tree;
				for (JsonValue val : array)
					getKeyValue(val, "", expString);
				break;
			// For String value type
			case STRING:
				if (key.equalsIgnoreCase(expString)) {
					JsonString st = (JsonString) tree;
					result = st.getString();
				}
				break;
			// For Number value type
			case NUMBER:
				if (key.equalsIgnoreCase(expString)) {
					JsonNumber st = (JsonNumber) tree;
					result = st.toString();
				}
				break;
			// For True value type
			case TRUE:
				break;
			// For False value type
			case FALSE:
				break;
			// For null value type
			case NULL:
				break;
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	public static Map<String, Object> getChildObjectInJSON(String text) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			JsonReader jsonreader = Json.createReader(new StringReader(text));
			JsonObject object = (JsonObject) jsonreader.readObject();
			switch (object.getValueType()) {
			case OBJECT:
				for (String name : object.keySet()) {
					JsonValue jsonvalue = object.get(name);
					String KeyFieldName = name;
					dataMap.put(KeyFieldName, jsonvalue);
					return dataMap;
				}
				break;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

}
