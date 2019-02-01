/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.util.List;

import com.jayway.restassured.path.json.JsonPath;

/**
 * 
 * @author nmoreno
 *
 */
public class JsonParser {

	/**
	 * Return list with value json
	 * @param json
	 * @param jsonPath
	 * @return
	 */
	public static List<String> getJsonValueList(String json, String jsonPath) {
		return com.jayway.jsonpath.JsonPath.read(json, jsonPath);
	}
	
	/**
	 * Get a String value
	 * @param json
	 * @param jsonPath to find
	 * @return String property value
	 */
	public static String getStringValue(String json, String jsonPath) {
		JsonPath jPath = new JsonPath(json);
		return jPath.getString(jsonPath);
	}

}
