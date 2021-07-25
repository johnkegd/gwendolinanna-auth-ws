package com.gwendolinanna.ws.auth.app.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.gwendolinanna.ws.auth.app.shared.dto.EmailTemplate;
import com.gwendolinanna.ws.auth.app.shared.dto.UserDto;

/**
 * @author Johnkegd
 */
public class AmazonSES {


    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon SES.
    static final String FROM = "johnkegd@gmail.com";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    static final String TO = "recipient@example.com";

    // The configuration set to use for this email. If you do not want to use a
    // configuration set, comment the following variable and the
    // .withConfigurationSetName(CONFIGSET); argument below.
    static final String CONFIGSET = "ConfigSet";

    // The subject line for the email.
    static final String SUBJECT = "One last step to complete your registration";

    // The HTML body for the email.
    static final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "<p>Thank you for registerin with in our application. To complete the registration process and be able to log in, please click on the following link: </p>"
            + "<a href='http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
            + "Final step to complete your registration</a>";

    // The email body for recipients with non-HTML email clients.
    static final String TEXTBODY = "Please verify your email address."
            + "Thank you for registerin with in our application. To complete the registration process and be able to log in, please click on the following link: "
            + "http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue";


    public static void verifiyEmail(UserDto userDto) {
        String htmlBodytWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setEmailRecipient(userDto.getEmail());
        emailTemplate.setSubject(SUBJECT);
        emailTemplate.setEmailSender(FROM);
        emailTemplate.setTextBody(textBodyWithToken);
        emailTemplate.setHtmlBody(htmlBodytWithToken);
        emailTemplate.setRegion(Regions.EU_WEST_1);

        sendEmail(emailTemplate);

    }

    public static boolean sendPasswordResetRequest(UserDto userDto) {
        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setEmailRecipient(userDto.getEmail());
        emailTemplate.setEmailSender(FROM);
        emailTemplate.setSubject("Password reset");
        emailTemplate.setRegion(Regions.EU_WEST_1);

        return sendEmail(emailTemplate);
    }

    private static boolean sendEmail(EmailTemplate emailTemplate) {
        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(emailTemplate.getRegion()).build();

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(emailTemplate.getEmailRecipient()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(emailTemplate.getHtmlBody()))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(emailTemplate.getTextBody())))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(emailTemplate.getSubject())))
                    .withSource(emailTemplate.getEmailSender());
            // Comment or remove the next line if you are not using a
            // configuration set
            // .withConfigurationSetName(CONFIGSET);

            SendEmailResult result = client.sendEmail(request);

            if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {
                System.out.println("Email sent!");
                return true;
            }
        } catch (Exception e) {
            System.out.println("The email was not sent. Error message: "
                    + e.getMessage());
            return false;
        }
        return false;
    }

}
