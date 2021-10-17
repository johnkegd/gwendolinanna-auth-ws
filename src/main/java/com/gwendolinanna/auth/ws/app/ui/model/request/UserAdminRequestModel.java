package com.gwendolinanna.auth.ws.app.ui.model.request;

/**
 * @author Johnkegd
 * @author GwendolinAnna
 */

public class UserAdminRequestModel {
    
    private UserDetailsRequestModel userDetails;
    private String icon;
    private String userName;
    private String website;
    private String description;


    public UserDetailsRequestModel getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDetailsRequestModel userDetails) {
        this.userDetails = userDetails;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
