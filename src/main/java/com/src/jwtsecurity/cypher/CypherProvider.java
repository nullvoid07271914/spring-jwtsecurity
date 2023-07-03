package com.src.jwtsecurity.cypher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.src.jwtsecurity.constants.CypherConstants;
import com.src.jwtsecurity.utils.SecretKeyUtils;

@Service
public class CypherProvider implements Cypher, CypherConstants {

	private static final Logger logger = LoggerFactory.getLogger(CypherProvider.class);

	private static KeyGenerator generator;

	private final CypherAlgorithm algorithm;

	static {
		try {
			generator = KeyGenerator.getInstance(AES);
			generator.init(AES_KEY_SIZE);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException: {}", e);
		}
	}

	public CypherProvider() {
		algorithm = new CypherAlgorithm();
	}

	private class CypherAlgorithm {

		void initialize(HttpSession session) throws NoSuchAlgorithmException {
			SecretKey secretKey = buildSecretKey();
			byte[] saltOnce = buildSecureRandomNOnce();
			String key = SecretKeyUtils.secretKeyToString(secretKey);
			session.setAttribute(SECRET_KEY, key);
			session.setAttribute(SALT_ONCE, saltOnce);
		}

		byte[] encrypt(SecretKey key, byte[] nOnce, String token) throws Exception {
			byte[] plain = token.getBytes();
			Cipher cipher = Cipher.getInstance(INSTANCE);
			SecretKeySpec secretKey = new SecretKeySpec(key.getEncoded(), AES);
			GCMParameterSpec gcm = new GCMParameterSpec(AUTH_LENGTH, nOnce);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcm);
			byte[] encrypted = cipher.doFinal(plain);
			return encrypted;
		}

		byte[] decrypt(SecretKey key, byte[] nOnce, byte[] encrypted) throws Exception {
			Cipher cipher = Cipher.getInstance(INSTANCE);
			SecretKeySpec secretKey = new SecretKeySpec(key.getEncoded(), AES);
			GCMParameterSpec gcm = new GCMParameterSpec(AUTH_LENGTH, nOnce);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, gcm);
			byte[] decrypted = cipher.doFinal(encrypted);
			return decrypted;
		}

		SecretKey buildSecretKey() {
			return generator.generateKey();
		}

		byte[] buildSecureRandomNOnce() {
			byte[] nOnce = new byte[GCM_LENGTH];
			SecureRandom random = new SecureRandom();
			random.nextBytes(nOnce);
			return nOnce;
		}
	}

	@Override
	public String encrypt(HttpSession session, String token) throws Exception {
		algorithm.initialize(session);
		String key = (String) session.getAttribute(SECRET_KEY);
		SecretKey secretKey = SecretKeyUtils.stringToSecretKey(key);
		byte[] saltOnce = (byte[]) session.getAttribute(SALT_ONCE);
		byte[] encrypted = algorithm.encrypt(secretKey, saltOnce, token);
		return Base64.getEncoder().encodeToString(encrypted);
	}

	@Override
	public String decrypt(HttpSession session, String token) throws Exception {
		byte[] decode = Base64.getDecoder().decode(token);
		String key = (String) session.getAttribute(SECRET_KEY);
		SecretKey secretKey = SecretKeyUtils.stringToSecretKey(key);
		byte[] saltOnce = (byte[]) session.getAttribute(SALT_ONCE);
		byte[] decrypted = algorithm.decrypt(secretKey, saltOnce, decode);
		return new String(decrypted);
	}
}
