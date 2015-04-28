package com.blizzard.api.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Used to encrypt and decrypt data
 * */
public class Cryptor {
	private static final String HASH_ALGORITHM = "HmacSHA1";

	/*
	 
	 * @param stringToHash
	 * 
	 * @param secreatKey
	 * 
	 * @return
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 * @throws InvalidKeyException
	 */

	public String hash(final String stringToHash, final String secretKey)
			throws SignatureException {
		byte[] signatureBytes = secretKey != null ? secretKey.getBytes() : null;
		try {
			Key sk = new SecretKeySpec(signatureBytes, HASH_ALGORITHM);
			Mac mac = Mac.getInstance(sk.getAlgorithm());
			mac.init(sk);
			byte[] hmac = mac.doFinal(stringToHash.getBytes());
			// base64-encode the hmac
			//result = Encoding.EncodeBase64(hmac);
			return toHexString(hmac);
		} catch (NoSuchAlgorithmException nsae) {
			// throw an exception or pick a different encryption method
			throw new SignatureException(
					"error building signature, no such algorithm in device "
							+ Cryptor.HASH_ALGORITHM);
		} catch (InvalidKeyException ike) {
			throw new SignatureException(
					"error building signature, invalid key "
							+ Cryptor.HASH_ALGORITHM);
		}
	}

	/**
	 * Convert a byte array to a hex string.
	 * 
	 * @param bytes
	 *            array to convert to a string
	 * @return a hex string
	 **/

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		Formatter formatter = new Formatter(sb);
		for (byte b : bytes) {
			formatter.format("%02x", b);

		}
		String returnMe = sb.toString();
		formatter.close();
		return returnMe;
	}

}
