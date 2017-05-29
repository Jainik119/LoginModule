package com.demo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.demo.entity.ServerLocation;
import com.demo.service.EmailService;
import com.demo.service.GeoLocationService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private GeoLocationService serverLocation;

	
	@Value(value = "classpath:static/logininformationmailtemplet.html")
	private Resource htmlfile;
	
	public String convertStreamToString(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = null;
		StringBuffer messageBody = new StringBuffer();
		try {
			while ((line = reader.readLine()) != null) {
				messageBody.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String htmlMsg = messageBody.toString();

		return htmlMsg;

	}
	
	
    public void sendMail(String username) throws MessagingException, IOException {
    	ServerLocation location = serverLocation.getLocation(serverLocation.getClientIp());
		String htmlMsg = convertStreamToString(htmlfile.getInputStream());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		helper.setFrom("jgbakaraniya@gmail.com");
		helper.setTo("jainik.bakraniya@aditmicrosys.com");
		helper.setSubject("New sign-in from "+location.getBrowserName()+" on "+location.getOsName());
		htmlMsg = htmlMsg.replace("{browser}", location.getBrowserName());
		htmlMsg = htmlMsg.replace("{os}", location.getOsName());
		htmlMsg = htmlMsg.replace("{username}", username);
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		htmlMsg = htmlMsg.replace("{date}", df.format(new Date()));
		htmlMsg = htmlMsg.replace("{city}", location.getCity());
		htmlMsg = htmlMsg.replace("{regionName}", location.getRegionName());
		htmlMsg = htmlMsg.replace("{countryName}", location.getCountryName());
		htmlMsg = htmlMsg.replace("{postalCode}", location.getPostalCode());
		htmlMsg = htmlMsg.replace("{ip}", location.getIpAddress());
		htmlMsg = htmlMsg.replace("{latitude}", location.getLatitude());
		htmlMsg = htmlMsg.replace("{longitude}", location.getLongitude());
		mimeMessage.setContent(htmlMsg, "text/html");
        javaMailSender.send(mimeMessage);
    }

}
