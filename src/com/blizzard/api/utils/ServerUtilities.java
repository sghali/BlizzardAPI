package com.blizzard.api.utils;

import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;


/**
 * This class is used to authenticate the API Servers Creating AUTH value
 * 
 * @author sghali
 * 
 */
public class ServerUtilities {
	private static Logger logger=(Logger) Logger.getLogger(ServerUtilities.class);
	public static String public_key;
	public static String sig_key;

	/**
	 * Build an authentication header string
	 * 
	 * @param httpMethod
	 *            uppercase HTTP request method (e.g. GET, POST)
	 * @param body
	 * @return
	 */

	public static String getAuthenticationHeader(String httpMethod, String urlPath) {
		String authHeader = Symbols.EMPTY.getSymbol();
		String time = getTimeStampInSeconds();
		String signature;
		String StringToSign;
		try {
			StringToSign = httpMethod + "\n" +
					time + "\n" +
					urlPath + "\n";
			signature = getDigitalSignature(StringToSign);
			authHeader = getPublicKey() + ":" + signature;
		} catch (SignatureException e) {
			logger.error(e);
		}
		return authHeader;
	}

	/**
	 * Get the digital signature to be a part of the authentication header.
	 * 
	 * @param httpMethod
	 *            uppercase HTTP request method (e.g. GET, POST)
	 * @param timeStamp
	 *            use {@link #getTimeStampInSeconds()}
	 * @param urlPath
	 * @return digital signature
	 * @throws java.security.SignatureException
	 **/

	public static String getDigitalSignature(String StringToSign)
			throws SignatureException {

		Cryptor cryptor = null;

		if (cryptor == null) {
			cryptor = new Cryptor();
		}
		String tempvalue = cryptor.hash(StringToSign, getSignatureKey());
		return tempvalue;
	}

	/**
	 * Get a signature hash string
	 * 
	 * @param httpMethod
	 *            uppercase HTTP request method (e.g. GET, POST)
	 * @param timeStamp
	 *            use {@link #getTimeStampInSeconds()}
	 * @param jsonParamString
	 *            JSON-encoded parameter string
	 * @return a hashed signature string
	 * */

	private static String getSignatureString(String httpMethod,
			String timeStamp, String jsonParamString) {

		return httpMethod + "&" + getPublicKey() + "&" + timeStamp + "&"
				+ jsonParamString;
	}

	/**
	 * Get the current time in seconds * * @return
	 **/

	public static String getTimeStampInSeconds() {
		Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}

	public static void setPublicKey(String value) {
		public_key = value;
	}

	public static void setSignatureKey(String value) {
		sig_key = value;
	}

	/**
	 * Get the access key from the session instance * * @return access key
	 **/

	public static String getPublicKey() {

		/*
		 * The public  key is set with registration of application
		 * initialization.
		 */
		return public_key;
	}

	/**
	 * Get the signature key from the session instance
	 * 
	 * @return signature key
	 * */

	public static String getSignatureKey() {

		/*
		 * The signature key initialization.
		 */

		return sig_key;
	}
}
