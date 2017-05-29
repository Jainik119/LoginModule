package com.demo.service.impl;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import com.demo.repository.LoginRepository;
import com.demo.service.LoginService;

@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginRepository loginRepository;
	
	@Override
	public void updateUserMaster(UserMaster user) {
		loginRepository.updateUserMaster(user);
	}

	@Override
	public UserMaster findUserById(String username) {
		return loginRepository.findUserById(username);
	}

	@Override
	public UserMaster getUserKey(String authToken) {
		return loginRepository.getUserKey(authToken);
	}

	@Override
	public boolean validUser(String username, String password) {
		return loginRepository.validUser(username, password);
	}

	@Override
	public RestLoginBO parseToken(String token) {
		return loginRepository.parseToken(token);
	}

	@Override
	public SecretKey getSecretKey(String authToken) {
		return loginRepository.getSecretKey(authToken);
	}

	@Override
	public String generateToken(RestLoginBO login) throws Exception {
		return loginRepository.generateToken(login);
	}

	@Override
	public String generateOtp(String tokenUserid) {
		return loginRepository.generateOtp(tokenUserid);
	}

}
