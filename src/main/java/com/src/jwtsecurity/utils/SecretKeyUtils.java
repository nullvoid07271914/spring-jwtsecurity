package com.src.jwtsecurity.utils;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.src.jwtsecurity.constants.CypherConstants;

public abstract class SecretKeyUtils {

	public static String secretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
		byte[] data = secretKey.getEncoded();
		String encodedKey = Base64.getEncoder().encodeToString(data);
		return encodedKey;
	}

	public static SecretKey stringToSecretKey(String encodedKey) {
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		int length = decodedKey.length;
		SecretKey secretKey = new SecretKeySpec(decodedKey, 0, length, CypherConstants.AES);
		return secretKey;
	}
}
