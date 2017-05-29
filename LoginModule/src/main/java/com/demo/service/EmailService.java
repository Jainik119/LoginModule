package com.demo.service;

import java.io.IOException;
import javax.mail.MessagingException;

public interface EmailService {
	public void sendMail(String username) throws MessagingException, IOException;
}