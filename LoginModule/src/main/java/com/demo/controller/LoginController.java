package com.demo.controller;

import java.nio.charset.StandardCharsets;
import com.itextpdf.xmp.impl.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.delegator.LoginDelegator;
import com.demo.entity.UserMaster;
import com.demo.entity.rest.RestLoginBO;
import com.demo.service.EmailService;

@RestController
@RequestMapping(value = "/user")
public class LoginController {
	
	@Autowired
	private LoginDelegator loginDelegator;
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers="Accept=application/json", produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> login(@RequestBody RestLoginBO loginBO) throws Exception{
		
		Map<String, Object> payLoad = new HashMap<>();		
		byte[] byteUname = Base64.decode(loginBO.getUsername().getBytes(StandardCharsets.UTF_8));
		byte[] bytePwd = Base64.decode(loginBO.getPassword().getBytes(StandardCharsets.UTF_8));
		String username = new String(byteUname, "UTF-8");
		String pwd = new String(bytePwd, "UTF-8");
		RestLoginBO logintoken = new RestLoginBO();
		logintoken.setUsername(username);
		logintoken.setPassword(pwd);
		if (loginDelegator.validUser(logintoken.getUsername(), logintoken.getPassword())) {
			UserMaster master = loginDelegator.findUserById(logintoken.getUsername());
			String tokenOfUser = loginDelegator.generateToken(logintoken);
			emailService.sendMail(logintoken.getUsername());
			payLoad.put("auth_token", tokenOfUser);
			payLoad.put("response", "true");
			payLoad.put("role", master.getRole());
			payLoad.put("message", "Valid User successfully login...!!");
		}else{
			payLoad.put("auth_token", "");
			payLoad.put("response", "false");
			payLoad.put("message", "woops... Username And Password is not match");
		}
		return payLoad;
	}
}
