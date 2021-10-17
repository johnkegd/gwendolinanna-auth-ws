package com.gwendolinanna.auth.ws.app.shared.dto;

import com.amazonaws.regions.Regions;


/**
 * @author Johnkegd
 * @author GwendolinAnna
 */

public class EmailTemplate {
    private String textBody;
    private String htmlBody;
    private String subject;
    private String emailSender;
    private String emailRecipient;
    private Regions region;


    public String getTextBody() {
        return textBody;
    }
    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailSender() {
        return emailSender;
    }
    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }
    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public Regions getRegion() {
        return region;
    }
    public void setRegion(Regions region) {
        this.region = region;
    }
}
