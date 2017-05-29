package com.demo.util;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.demo.delegator.LoginDelegator;
import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
public class TokenHandler {

	static final Logger logger = Logger.getLogger(TokenHandler.class);

	public static SecretKey secret = null;

	LoginDelegator loginDelegator = new LoginDelegator();
	
	AESEncryption aesEncry = new AESEncryption();

	@SuppressWarnings("static-access")
	public TokenHandler() {
		try {secret = aesEncry.getSecretEncryptionKey();} catch (Exception e) {	e.printStackTrace();}
	}
	
	public String generateOtp(String tokenUserid) {
		String verifycode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
		logger.info(verifycode);
		return verifycode;
	}
	
	public UserMaster generateToken(RestLoginBO login, UserMaster user) throws Exception {
		String username = aesEncry.EncryptToken(login.getUsername());
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("username", username);
		String authToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
		Date expire = new Date(System.currentTimeMillis() + 500 * 60 * 1000);
		Timestamp expiration_time = new Timestamp(expire.getTime());
		user.setExp_date(expiration_time);
		user.setSecKey(Base64.getEncoder().encodeToString(secret.getEncoded()).toString());
		user.setToken(authToken);
		return user;
	}
	
	public SecretKey getSecretKey(String authToken) {

		Timestamp actualTimeStampDate = null;
		Date actual = new Date();
		actualTimeStampDate = new Timestamp(actual.getTime());
		UserMaster userSecKey = loginDelegator.getUserKey(authToken); 

		SecretKey originalKey = null;

		if (userSecKey.getExp_date().getTime() > actualTimeStampDate.getTime()) {
			byte[] decodedKey = Base64.getDecoder().decode(userSecKey.getSecKey());
			originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		} else {
			originalKey = null;
		}
		return originalKey;
	}

	public RestLoginBO parseToken(String token) {
		RestLoginBO login = new RestLoginBO();

		try {
			Claims body = Jwts.parser().setSigningKey(getSecretKey(token)).parseClaimsJws(token).getBody();
			String userid = aesEncry.DecryptToken(body.get("username").toString());
			login.setUsername(userid);
		} catch (Exception e) {
			login = null;
			logger.error(e);
		}
		return login;

	}
}
