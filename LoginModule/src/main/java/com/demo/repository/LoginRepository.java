package com.demo.repository;

import javax.crypto.SecretKey;

import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;

public interface LoginRepository {

	public void updateUserMaster(UserMaster user);

	public UserMaster findUserById(String username);

	public UserMaster getUserKey(String authToken);

	public boolean validUser(String username, String password);

	public RestLoginBO parseToken(String token);

	public SecretKey getSecretKey(String authToken);

	public String generateToken(RestLoginBO login) throws Exception;

	public String generateOtp(String tokenUserid);
}
