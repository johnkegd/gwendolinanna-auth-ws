package com.gwendolinanna.ws.auth.app.ui.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johnkegd
 */
@Getter
@Setter
public class LoginRequestModel {
    private String email;
    private String password;
}
