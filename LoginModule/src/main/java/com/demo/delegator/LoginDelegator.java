package com.demo.delegator;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import com.demo.service.LoginService;

@Component
public class LoginDelegator {

	@Autowired
	private LoginService loginService;
	
	public void updateUserMaster(UserMaster user) {
		loginService.updateUserMaster(user);
	}

	public UserMaster findUserById(String username) {
		return loginService.findUserById(username);
	}

	public UserMaster getUserKey(String authToken) {
		return loginService.getUserKey(authToken);
	}

	public boolean validUser(String username, String password) {
		return loginService.validUser(username, password);
	}
	
	public RestLoginBO parseToken(String token) {
		return loginService.parseToken(token);
	}

	public SecretKey getSecretKey(String authToken) {
		return loginService.getSecretKey(authToken);
	}

	public String generateToken(RestLoginBO login) throws Exception {
		return loginService.generateToken(login);
	}

	public String generateOtp(String tokenUserid) {
		return loginService.generateOtp(tokenUserid);
	}
}
