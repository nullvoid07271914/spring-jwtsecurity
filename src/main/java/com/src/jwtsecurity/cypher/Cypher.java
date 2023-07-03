package com.src.jwtsecurity.cypher;

import javax.servlet.http.HttpSession;

public interface Cypher {

	public String encrypt(HttpSession session, String token) throws Exception;

	public String decrypt(HttpSession session, String token) throws Exception;
}
