package com.touhid.project.uber.uberApp.services;

public interface EmailService {

    public void sendEmail(String toEmail,String subject,String body);
    public void sendEmails(String[] toEmail,String subject,String body);
}
