package com.demo;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${spring.mail.protocol}")
	private String protocol;

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private Integer port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String starttls;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("PUT", "DELETE", "GET",
				"POST");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("WEB-INF/pages/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public JavaMailSenderImpl mailConfig(){
		JavaMailSenderImpl mail = new JavaMailSenderImpl();
		mail.setHost(host);
		mail.setPort(port);
		mail.setUsername(username);
		mail.setPassword(password);
		Properties p = new Properties();
		p.put("mail.smtp.auth", auth);
		p.put("mail.smtp.starttls.enable", starttls);
		p.put("mail.transport.protocol", protocol);
		p.put("mail.debug", "true");
		mail.setJavaMailProperties(p);
		return mail;
	}
}