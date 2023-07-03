package com.src.jwtsecurity.constants;

public interface CypherConstants {

	public static final String INSTANCE = "AES/GCM/NoPadding";

	public static final String AES = "AES";

	public static final int AUTH_LENGTH = 128;

	public static final int AES_KEY_SIZE = 256;

	public static final int GCM_LENGTH = 12;

	public static final String SECRET_KEY = "secret_key";

	public static final String CURRENT_USERNAME = "current_username";

	public static final String SALT_ONCE = "salt_once";
}
