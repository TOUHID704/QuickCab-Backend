package com.touhid.project.uber.uberApp.servicesImpl;

import com.touhid.project.uber.uberApp.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;


    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        try {
            log.info("Sending email to {}", toEmail);
            javaMailSender.send(simpleMailMessage);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send email to " + toEmail, e);
        }
    }

    @Override
    public void sendEmails(String[] toEmails, String subject, String body) {
        for (String toEmail : toEmails) {
            try {
                log.info("Preparing to send email to {}", toEmail);

                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setTo(toEmail);
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(body);

                javaMailSender.send(simpleMailMessage);
                log.info("Email sent successfully to {}", toEmail);
            } catch (Exception e) {
                log.error("Error sending email to {}: {}", toEmail, e.getMessage(), e);
            }
        }
    }

}
