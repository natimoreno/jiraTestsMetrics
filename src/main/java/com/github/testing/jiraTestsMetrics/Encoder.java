/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * 
 * @author nmoreno
 *
 */
public class Encoder {

	/**
	 * Encode string
	 * @param string
	 * @return encoded string
	 */
	public static String encode(String string){
		
        final byte[] authBytes = string.getBytes(StandardCharsets.UTF_8);
        final String encoded = Base64.getEncoder().encodeToString(authBytes);
		return encoded;
	}
	
}
