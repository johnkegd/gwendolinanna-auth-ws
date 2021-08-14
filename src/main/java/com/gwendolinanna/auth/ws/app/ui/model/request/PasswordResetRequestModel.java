package com.gwendolinanna.auth.ws.app.ui.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Setter
@Getter
public class PasswordResetRequestModel {
    private String email;
}
