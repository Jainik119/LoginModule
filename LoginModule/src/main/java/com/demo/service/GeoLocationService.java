package com.demo.service;

import java.io.IOException;

import com.demo.entity.ServerLocation;

public interface GeoLocationService {
	public ServerLocation getLocation(String ipAddress);
	public String getClientIp() throws IOException;
}
