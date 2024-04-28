package com.example.sqlassignment;

public class Password {

    private String appName;
    private String password;
    private String url;

    public Password(String appName, String password, String url) {
        this.appName = appName;
        this.password = password;
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}