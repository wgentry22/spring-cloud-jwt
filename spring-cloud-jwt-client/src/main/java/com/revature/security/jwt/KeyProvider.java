package com.revature.security.jwt;

import java.security.Key;

public interface KeyProvider {

	Key getPublicKey();
	Key getPrivateKey();
}
