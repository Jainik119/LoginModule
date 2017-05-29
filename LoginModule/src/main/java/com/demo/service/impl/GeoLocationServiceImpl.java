package com.demo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.entity.ServerLocation;
import com.demo.service.GeoLocationService;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

import eu.bitwalker.useragentutils.UserAgent;


@Component
public class GeoLocationServiceImpl implements GeoLocationService {

	private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    public String getClientIp() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        return ip;
    }
    
	@Override
	public ServerLocation getLocation(String ipAddress) {
		String dataFile = "location/GeoLiteCity.dat";
		return getLocation(ipAddress, dataFile);
	}
	
	private ServerLocation getLocation(String ipAddress, String locationDataFile) {

		ServerLocation serverLocation = null;
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		URL url = getClass().getClassLoader().getResource(locationDataFile);

		if (url == null) {
			System.err.println("location database is not found - " + locationDataFile);
		} else {
			try {
				serverLocation = new ServerLocation();
				LookupService lookup = new LookupService(url.getPath(), LookupService.GEOIP_MEMORY_CACHE);
				Location locationServices = lookup.getLocation(ipAddress);
				serverLocation.setCountryCode(locationServices.countryCode);
				serverLocation.setCountryName(locationServices.countryName);
				serverLocation.setRegion(locationServices.region);
				serverLocation.setRegionName(
						regionName.regionNameByCode(locationServices.countryCode, locationServices.region));
				serverLocation.setCity(locationServices.city);
				serverLocation.setPostalCode(locationServices.postalCode);
				serverLocation.setLatitude(String.valueOf(locationServices.latitude));
				serverLocation.setLongitude(String.valueOf(locationServices.longitude));
				serverLocation.setBrowserName(userAgent.getBrowser().getName());
				serverLocation.setBrowserVersion(userAgent.getBrowserVersion().toString());
				serverLocation.setOsName(userAgent.getOperatingSystem().toString());
				serverLocation.setIpAddress(getClientIp());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return serverLocation;

	}
}
