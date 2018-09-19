package com.revature.security.jwt;

import java.security.Key;

public interface KeyProvider {

	/**
	 * @author William Gentry
	 */
	
	Key getPublicKey();
	Key getPrivateKey();
}
