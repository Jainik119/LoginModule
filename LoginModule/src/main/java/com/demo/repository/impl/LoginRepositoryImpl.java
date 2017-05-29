package com.demo.repository.impl;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import com.demo.repository.LoginRepository;
import com.demo.util.AESEncryption;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Repository("loginRepository")
public class LoginRepositoryImpl implements LoginRepository{

	static final Logger logger = Logger.getLogger(LoginRepositoryImpl.class);
	
	public static SecretKey secret = null;

	AESEncryption aesEncry = new AESEncryption();

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("static-access")
	public LoginRepositoryImpl() {
		try {secret = aesEncry.getSecretEncryptionKey();} catch (Exception e) {	e.printStackTrace();}
	}
	
	@Override
	public void updateUserMaster(UserMaster user) {
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public UserMaster findUserById(String username) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserMaster.class);
		c.add(Restrictions.eq("username", username));
		UserMaster user = (UserMaster) c.uniqueResult();
		return user;
	}

	@Override
	public UserMaster getUserKey(String authToken) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserMaster.class);
		c.add(Restrictions.eq("token", authToken));
		UserMaster user = (UserMaster) c.uniqueResult();
		return user;
	}

	@Override
	public boolean validUser(String username, String password) {
		boolean flag = false;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserMaster.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.eq("password", password));
		UserMaster user = (UserMaster) c.uniqueResult();
		if (user != null) {
			flag = true;
		}
		return flag;
	}
	
	@Override
	public String generateOtp(String tokenUserid) {
		String verifycode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
		logger.info(verifycode);
		return verifycode;
	}
	
	@Override
	public String generateToken(RestLoginBO login) throws Exception {
		String username = aesEncry.EncryptToken(login.getUsername());
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("username", username);
		String authToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
		Date expire = new Date(System.currentTimeMillis() + 500 * 60 * 1000);
		Timestamp expiration_time = new Timestamp(expire.getTime());
		UserMaster user = findUserById(login.getUsername());
		user.setExp_date(expiration_time);
		user.setSecKey(Base64.getEncoder().encodeToString(secret.getEncoded()).toString());
		user.setToken(authToken);
		updateUserMaster(user);
		return authToken;
	}
	
	@Override
	public SecretKey getSecretKey(String authToken) {
		Timestamp actualTimeStampDate = null;
		Date actual = new Date();
		actualTimeStampDate = new Timestamp(actual.getTime());
		UserMaster userSecKey = getUserKey(authToken); 
		SecretKey originalKey = null;
		if (userSecKey.getExp_date().getTime() > actualTimeStampDate.getTime()) {
			byte[] decodedKey = Base64.getDecoder().decode(userSecKey.getSecKey());
			originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		} else {
			originalKey = null;
		}
		return originalKey;
	}

	@Override
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
