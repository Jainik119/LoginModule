package com.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class UserMaster{


	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name ="USERNAME")
	private String username;
	
	@Column(name ="PASSWORD")
	private String password;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "TOKEN")
	private String token;
	
	@Column(name = "EXP_DATE")
	private Date exp_date;
	
	@Column(name = "SEC_KEY")
	private String secKey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}	
}
