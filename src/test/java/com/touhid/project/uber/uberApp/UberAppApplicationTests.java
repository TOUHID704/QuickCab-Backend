package com.touhid.project.uber.uberApp;

import com.touhid.project.uber.uberApp.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private EmailService emailService;

	@Test
	void contextLoads() {
		// Ensures that the application context loads successfully.
	}

//	@Test
//	void sendEmail() {
//		emailService.sendEmail(
//				"shahidsayed399@gmail.com",
//				"Testing this is the subject",
//				"Testing this is the body"
//		);
//	}

//	@Test
//	void sendMultipleEmails() {
//		String[] emails = {
//				"user1@gmail.com",
//				"user2@gmail.com",
//				"user3@gmail.com"
//		};
//
//		emailService.sendEmails(emails, "Common Subject", "Common Body");
//	}
}
