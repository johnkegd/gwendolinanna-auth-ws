package com.gwendolinanna.auth.ws.app.shared.dto;

import com.amazonaws.regions.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Getter
@Setter
public class EmailTemplate {
    private String textBody;
    private String htmlBody;
    private String subject;
    private String emailSender;
    private String emailRecipient;
    private Regions region;
}
