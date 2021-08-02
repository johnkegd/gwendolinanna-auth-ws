package com.gwendolinanna.ws.auth.app.ui.model.response;

import java.util.Date;

/**
 * @author Johnkegd
 */
public class ErrorMessage {

    private Date dateTimeout;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(Date dateTimeout, String message) {
        this.dateTimeout = dateTimeout;
        this.message = message;
    }

    public Date getDateTimeout() {
        return dateTimeout;
    }

    public void setDateTimeout(Date dateTimeout) {
        this.dateTimeout = dateTimeout;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
